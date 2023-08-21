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

	public ResponseEntity<Biometria> identificar() {

		IdentifyResult idResult = bio.CaptureAndIdentify();

		this.ret = idResult.getRetCode();

		if (isAparelhoDesconectado()) {
			return ResponseEntity.status(412).build(); // 412: pre-condition failed
		}

		Biometria biometria = new Biometria();
		biometria.setNumcad((int) idResult.getId());

		CIDBio.Terminate();

		return ResponseEntity.ok(biometria);
	}
	
	public ResponseEntity<Map<String, Object>> comparar(int numcad){
		
		MatchResult matchResult =  bio.CaptureAndMatch(numcad);
		
		ret = matchResult.getRetCode();

		if (isAparelhoDesconectado()) {
			return ResponseEntity.status(412).build(); // 412: pre-condition failed
		}
		if(ret == RetCode.SUCCESS) {
			return ResponseEntity.ok(Collections.singletonMap("success", true));
		}else {
			return ResponseEntity.ok(Collections.singletonMap("success", false));
		}
	}

	public ResponseEntity<Biometria> salvarNoLeitor(Biometria biometria) {

		ret = salvarBiometria(biometria);
		
		if (isAparelhoDesconectado()) {
			return ResponseEntity.status(412).build(); // 412: pre-condition failed
		}

		CIDBio.Terminate();

		if (ret == RetCode.SUCCESS) {
			return new ResponseEntity<Biometria>(biometria, HttpStatus.CREATED);
		}
		return new ResponseEntity<Biometria>(biometria, HttpStatus.BAD_REQUEST);

	}

	public ResponseEntity<Biometria> popularLeitor(List<Biometria> biometrias) {

		ret = deletarBiometrias();
		
		if (isAparelhoDesconectado()) {
			return ResponseEntity.status(412).build(); // 412: pre-condition failed
		}
		
		for (Biometria biometria : biometrias) {
			salvarBiometria(biometria);
		}

		CIDBio.Terminate();

		Biometria biometria = new Biometria();
		return new ResponseEntity<Biometria>(biometria, HttpStatus.CREATED);
	}

	private RetCode salvarBiometria(Biometria biometria) {
		RetCode ret = bio.SaveTemplate(biometria.getNumcad(), biometria.getDigital());
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
