package finger.jetty.models;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonPOJOBuilder
public class Biometria implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("digital")
	private String digital;
	
	@JsonProperty("tiptem")
	private int tiptem;
	
	public Biometria() {
		
	}
	
	public Biometria(String digital, int tiptem) {
		this.digital = digital;
		this.tiptem = tiptem;
	}


	public String getDigital() {
		return digital;
	}
	public void setDigital(String digital) {
		this.digital = digital;
	}
	public int getTiptem() {
		return tiptem;
	}
	public void setTiptem(int tiptem) {
		this.tiptem = tiptem;
	}

}
