package finger.jetty.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import finger.jetty.models.Biometria;
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
	public ResponseEntity<Biometria> identificar() {
		return fingerService.identificar();
	}
	
	@GetMapping(value = "/comparar/{numcad}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> comparar(@PathVariable("numcad") int numcad) {
		return fingerService.comparar(numcad);
	}
	
	@PostMapping(path = "/salvar",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Biometria> salvar(@RequestBody Biometria biometria) {
		return fingerService.salvarNoLeitor(biometria);
	}
	
	@PostMapping(path = "/popularLeitor",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Biometria> popularLeitor(@RequestBody List<Biometria> biometrias) {
		return fingerService.popularLeitor(biometrias);
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
