package finger.jetty.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import CIDBio.CIDBio;
import CIDBio.IdentifyResult;
import CIDBio.ImageAndTemplate;
import CIDBio.MatchResult;
import CIDBio.RetCode;
import CIDBio.Template;
import finger.jetty.models.Biometria;
import finger.jetty.models.Funcionario;

@Component
public class FingerService {

	private CIDBio bio;
	private RetCode ret;

	public FingerService() {
		CIDBio.Init();

		this.bio = new CIDBio();
	}
	
	public ResponseEntity<Map<String,Object>> isAtivado(){
		return ResponseEntity.ok(Collections.singletonMap("ativado", true));
	}
	
	public ResponseEntity<Biometria> capturar() {

		ImageAndTemplate imageandTemplate = bio.CaptureImageAndTemplate();
		
		ret = imageandTemplate.getRetCode();
		
		if (isAparelhoDesconectado()) {
			return ResponseEntity.status(412).build(); // 412: pre-condition failed
		}

		Template temp = imageandTemplate.getTemplate();
		String base64 = temp.getTemplate();

		Biometria biometria = new Biometria();
		biometria.setDigital(base64);

		CIDBio.Terminate();

		return ResponseEntity.ok(biometria);
	}

	public Funcionario identificar() {

		IdentifyResult idResult = bio.CaptureAndIdentify();

		this.ret = idResult.getRetCode();

		Funcionario funcionario = new Funcionario();
		if(idResult.getId()>0) {
	        String codigo = String.valueOf(idResult.getId());
	        int tipcol = Integer.valueOf(codigo.substring(0,1));
	        int numcad = Integer.valueOf(codigo.substring(2));

	        funcionario.setTipcol(tipcol);
	        funcionario.setNumcad(numcad);

		}

		CIDBio.Terminate();

		return funcionario;
	}
	
	public boolean comparar(int tipcol, int numcad){
		
		IdentifyResult idResult = bio.CaptureAndIdentify();
		if(idResult.getId()==0) {
			return false;
		}
        String codigo = String.valueOf(idResult.getId());
        int tipcolResult = Integer.valueOf(codigo.substring(0,1));
        int numcadResult = Integer.valueOf(codigo.substring(2));
		
        return tipcolResult == tipcol && numcadResult == numcad;

		
	}

	public void salvarNoLeitor(Funcionario funcionario) throws Exception {
		deletarBiometrias(funcionario);
		for(Biometria digital: funcionario.getDigitais()) {
			ret = salvarBiometria(funcionario, digital);
		}

		CIDBio.Terminate();

		if (ret == RetCode.SUCCESS) {
			return;
		}
		throw new Exception("Erro ao inserir");

	}
	
	private void deletarBiometrias(Funcionario funcionario) {
		for(int i=1; i<10; i++) {
			String codigo = funcionario.getTipcol()+String.valueOf(i)+funcionario.getNumcad();
			bio.DeleteTemplate(Long.valueOf(codigo));
		}
	}

	public void popularLeitor(List<Funcionario> funcionarios) {

		ret = deletarBiometrias();
		
		for(Funcionario funcionario: funcionarios){
            for(Biometria digital: funcionario.getDigitais()){
            	salvarBiometria(funcionario, digital);
            }

        }
		
		CIDBio.Terminate();

	}

	private RetCode salvarBiometria(Funcionario funcionario, Biometria digital) {
		
		String codigo = String.valueOf(funcionario.getTipcol())+String.valueOf(digital.getTiptem())+String.valueOf(funcionario.getNumcad());
        
		RetCode ret = bio.SaveTemplate(Integer.valueOf(codigo), digital.getDigital());
		return ret;
	}

	public ResponseEntity<Biometria> deletarTodos() {

		ret = deletarBiometrias();
		
		if (isAparelhoDesconectado()) {
			return ResponseEntity.status(412).build(); // 412: pre-condition failed
		}

		Biometria biometria = new Biometria();

		CIDBio.Terminate();

		return ResponseEntity.ok(biometria);
	}

	private RetCode deletarBiometrias() {
		RetCode ret = bio.DeleteAllTemplates();
		return ret;
	}

	public boolean isAparelhoDesconectado() {
		if (ret == RetCode.ERROR_IO_ON_HOST || ret == RetCode.ERROR_NO_DEVICE) {
			return true;
		}
		return false;
	}

}
