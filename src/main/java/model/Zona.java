package model;

public class Zona {
    private int codZona;
    private String nomeZona;
    private String descrizione;
    
    public Zona() {
    }

    public Zona(int codZona, String nomeZona, String descrizione) {
        this.codZona = codZona;
        this.nomeZona = nomeZona;
        this.descrizione = descrizione;
    }

    public int getCodZona() {
        return codZona;
    }

    public String getNomeZona() {
        return nomeZona;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setCodZona(int codZona) {
        this.codZona = codZona;
    }

    public void setNomeZona(String nomeZona) {
        this.nomeZona = nomeZona;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + codZona;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Zona other = (Zona) obj;
        if (codZona != other.codZona)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Zona [codZona=" + codZona + ", nomeZona=" + nomeZona + ", descrizione=" + descrizione + "]";
    }
}