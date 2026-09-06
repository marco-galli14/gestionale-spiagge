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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((codZona == null) ? 0 : codZona.hashCode());
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
        if (codZona == null) {
            if (other.codZona != null)
                return false;
        } else if (!codZona.equals(other.codZona))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Zona [codZona=" + codZona + ", nomeZona=" + nomeZona + ", descrizione=" + descrizione + "]";
    }

}
