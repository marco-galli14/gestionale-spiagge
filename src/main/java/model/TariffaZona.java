package model;

public class TariffaZona {
    private String codStagione;
    private String codZona;
    private Integer TariffaGiornaliera;

    public TariffaZona() {
    }

    public TariffaZona(String codStagione, String codZona, Integer tariffaGiornaliera) {
        this.codStagione = codStagione;
        this.codZona = codZona;
        TariffaGiornaliera = tariffaGiornaliera;
    }

    public String getCodStagione() {
        return codStagione;
    }

    public String getCodZona() {
        return codZona;
    }

    public Integer getTariffaGiornaliera() {
        return TariffaGiornaliera;
    }

    public void setCodStagione(String codStagione) {
        this.codStagione = codStagione;
    }

    public void setCodZona(String codZona) {
        this.codZona = codZona;
    }

    public void setTariffaGiornaliera(Integer tariffaGiornaliera) {
        TariffaGiornaliera = tariffaGiornaliera;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((codStagione == null) ? 0 : codStagione.hashCode());
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
        TariffaZona other = (TariffaZona) obj;
        if (codStagione == null) {
            if (other.codStagione != null)
                return false;
        } else if (!codStagione.equals(other.codStagione))
            return false;
        if (codZona == null) {
            if (other.codZona != null)
                return false;
        } else if (!codZona.equals(other.codZona))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "TariffaZona [codStagione=" + codStagione + ", codZona=" + codZona + ", TariffaGiornaliera="
                + TariffaGiornaliera + "]";
    }
}
