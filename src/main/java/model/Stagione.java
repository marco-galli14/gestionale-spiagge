package model;

import java.util.Date;
public class Stagione {
    private String cosStagione;
    private Date dataInizio;
    private Date dataFine;

    public Stagione() {
    }

    public Stagione(String cosStagione, Date dataInizio, Date dataFine) {
        this.cosStagione = cosStagione;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
    }

    public String getCosStagione() {
        return cosStagione;
    }

    public void setCosStagione(String cosStagione) {
        this.cosStagione = cosStagione;
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
