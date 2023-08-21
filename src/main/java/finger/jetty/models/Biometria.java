package finger.jetty.models;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonPOJOBuilder
public class Biometria implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("digital")
	private String digital;
	
	@JsonProperty("numcad")
	private int numcad;
	
	public Biometria() {
		
	}
	
	public Biometria(String digital, int numcad) {
		this.digital = digital;
		this.numcad = numcad;
	}


	public String getDigital() {
		return digital;
	}
	public void setDigital(String digital) {
		this.digital = digital;
	}
	public int getNumcad() {
		return numcad;
	}
	public void setNumcad(int numcad) {
		this.numcad = numcad;
	}

}
