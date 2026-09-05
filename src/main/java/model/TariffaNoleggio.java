package model;

public class TariffaNoleggio {
    private String codStagione;
    private String codAttrezzatura;
    private Integer tariffaOraria;

    public TariffaNoleggio() {
    }

    public TariffaNoleggio(String codStagione, String codAttrezzatura, Integer tariffaOraria) {
        this.codStagione = codStagione;
        this.codAttrezzatura = codAttrezzatura;
        this.tariffaOraria = tariffaOraria;
    }

    public String getCodStagione() {
        return codStagione;
    }

    public void setCodStagione(String codStagione) {
        this.codStagione = codStagione;
    }

    public String getCodAttrezzatura() {
        return codAttrezzatura;
    }

    public void setCodAttrezzatura(String codAttrezzatura) {
        this.codAttrezzatura = codAttrezzatura;
    }

    public Integer getTariffaOraria() {
        return tariffaOraria;
    }

    public void setTariffaOraria(Integer tariffaOraria) {
        this.tariffaOraria = tariffaOraria;
    }
}
