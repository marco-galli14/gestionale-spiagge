package model;

import java.time.LocalDate;

public class Stagione {
    private String codStagione;
    private LocalDate dataInizio;
    private LocalDate dataFine;

    public Stagione() {
    }

    public Stagione(String codStagione, LocalDate dataInizio, LocalDate dataFine) {
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

    public LocalDate getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
    }

    public LocalDate getDataFine() {
        return dataFine;
    }

    public void setDataFine(LocalDate dataFine) {
        this.dataFine = dataFine;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((codStagione == null) ? 0 : codStagione.hashCode());
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
        Stagione other = (Stagione) obj;
        if (codStagione == null) {
            if (other.codStagione != null)
                return false;
        } else if (!codStagione.equals(other.codStagione))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Stagione [codStagione=" + codStagione + ", dataInizio=" + dataInizio + ", dataFine=" + dataFine + "]";
    }
}