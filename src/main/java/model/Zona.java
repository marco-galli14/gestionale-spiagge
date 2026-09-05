package model;

public class Zona {
    private String codZona;
    private String nomeZona;
    private String descrizione;
    
    public Zona() {
    }

    public Zona(String codZona, String nomeZona, String descrizione) {
        this.codZona = codZona;
        this.nomeZona = nomeZona;
        this.descrizione = descrizione;
    }

    public String getCodZona() {
        return codZona;
    }

    public String getNomeZona() {
        return nomeZona;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setCodZona(String codZona) {
        this.codZona = codZona;
    }

    public void setNomeZona(String nomeZona) {
        this.nomeZona = nomeZona;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

}
