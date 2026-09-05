package model;

import java.util.Date;
public class Stagione {
    private String codStagione;
    private Date dataInizio;
    private Date dataFine;

    public Stagione() {
    }

    public Stagione(String codStagione, Date dataInizio, Date dataFine) {
        this.codStagione = codStagione;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
    }

    public String getCodStagione() {
        return codStagione;
    }

    public void setCodStagione(String codStagione) {
        this.codStagione = codStagione;
    }

    public Date getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(Date dataInizio) {
        this.dataInizio = dataInizio;
    }

    public Date getDataFine() {
        return dataFine;
    }

    public void setDataFine(Date dataFine) {
        this.dataFine = dataFine;
    }
}
