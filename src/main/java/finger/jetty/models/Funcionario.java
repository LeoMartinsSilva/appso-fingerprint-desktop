package finger.jetty.models;

import java.io.Serializable;
import java.util.List;

public class Funcionario implements Serializable {
    private int codemp;

    private int tipcol;
    private int numcad;
    private String nome;

    private List<Biometria> digitais;

    public Funcionario(){

    }
    public Funcionario(int codemp, int tipcol, int numcad, String nome){
        this.codemp = codemp;
        this.tipcol = tipcol;
        this.numcad = numcad;
        this.nome = nome;
    }

    public int getCodemp() {
        return codemp;
    }

    public int getTipcol() {
        return tipcol;
    }

    public void setTipcol(int tipcol) {
        this.tipcol = tipcol;
    }

    public void setNumcad(int numcad) {
		this.numcad = numcad;
	}
    public int getNumcad() {
        return numcad;
    }

    public String getNome() {
        return nome;
    }


    public List<Biometria> getDigitais() {
        return digitais;
    }

    public void setDigitais(List<Biometria> digitais) {
        this.digitais = digitais;
    }

}

