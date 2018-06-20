package com.example.yye5891.nostrocalendario;

import java.util.Date;

public class Presenze {

    String giorno;
    String mattina="";
    String pomeriggio="";

    public Presenze(String giorno, String mattina, String pomeriggio) {
        this.giorno = giorno;
        this.mattina = mattina;
        this.pomeriggio = pomeriggio;
    }

    public Presenze(){

    }

    public String getGiorno() {
        return giorno;
    }

    public void setGiorno(String giorno) {
        this.giorno = giorno;
    }

    public String getMattina() {
        return mattina;
    }

    public void setMattina(String mattina) {
        this.mattina = mattina;
    }

    public String getPomeriggio() {
        return pomeriggio;
    }

    public void setPomeriggio(String pomeriggio) {
        this.pomeriggio = pomeriggio;
    }
}

