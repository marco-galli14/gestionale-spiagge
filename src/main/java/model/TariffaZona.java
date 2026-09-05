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
}
