package finger.jetty.web;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ch.qos.logback.core.status.Status;
import finger.jetty.models.Biometria;
import finger.jetty.models.Funcionario;
import finger.jetty.service.FingerService;

@RestController
@RequestMapping(value = "/rest", produces = {"application/json", "text/xml"})
public class FingerController {
	
	@Autowired
	FingerService fingerService;

    @Autowired	
	ApplicationContext context;

    public FingerController(ApplicationContext context) {
        this.context = context;
    }
	
	@GetMapping(value = "/capturar", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Biometria> capturar() {
		return fingerService.capturar();
	}
	
	@GetMapping(value = "/identificar", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Funcionario> identificar() {
		
		if (fingerService.isAparelhoDesconectado()) {
			return ResponseEntity.status(412).build(); // 412: pre-condition failed
		}
		
		Funcionario funcionario = fingerService.identificar();
		return ResponseEntity.ok(funcionario);
	}
	
	@GetMapping(value = "/comparar/{tipcol}/{numcad}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> comparar(@PathVariable("tipcol") int tipcol, @PathVariable("numcad") int numcad) {
		
		if (fingerService.isAparelhoDesconectado()) {
			return ResponseEntity.status(412).build(); // 412: pre-condition failed
		}        
       	return ResponseEntity.ok(Collections.singletonMap("success", fingerService.comparar(tipcol, numcad)));	


	}
	
	@PostMapping(path = "/salvar",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> salvar(@RequestBody Funcionario funcionario) {
		
		if (fingerService.isAparelhoDesconectado()) {
			return ResponseEntity.status(412).build(); // 412: pre-condition failed
		}
		try {
			fingerService.salvarNoLeitor(funcionario);		
		}catch(Exception e) {
			return ResponseEntity.status(400).build();
		}
	
		return ResponseEntity.ok(Collections.singletonMap("sucesso", true));
	}
	
	@PostMapping(path = "/popularLeitor",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> popularLeitor(@RequestBody List<Funcionario> funcionarios) {
		
		if (fingerService.isAparelhoDesconectado()) {
			return ResponseEntity.status(412).build(); // 412: pre-condition failed
		}
		
		fingerService.popularLeitor(funcionarios);
		return ResponseEntity.ok(Collections.singletonMap("sucesso", true));
	}
	
	@GetMapping(value = "/isAtivado")
	public ResponseEntity<Map<String, Object>> isAtivado() {
		return fingerService.isAtivado();
	}
	
	@GetMapping(value = "/shutdown")
	public void shutdown() {
		SpringApplication.exit(context, ()->0);
	}
	
	@GetMapping(value = "/deletarTodos", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Biometria> deletarTodos() {
		return fingerService.deletarTodos();
	}
	
}
