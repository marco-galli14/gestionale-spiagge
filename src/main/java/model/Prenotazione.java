package model;

import java.time.LocalDate;

public class Prenotazione {

    private String codPrenotazione;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    private int PrezzoTotale;
    private boolean statoPagmento;
    private int codDipendente;
    private String cf;
    private String codPacchetto;
    private Integer idGruppo;

    public Prenotazione() {
    }

    public Prenotazione(String codPrenotazione, LocalDate dataInizio, LocalDate dataFine,int prezzoTotale,
            boolean statoPagmento, int codDipendente, String cf, String codPacchetto, Integer idGruppo) {
        this.codPrenotazione = codPrenotazione;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.PrezzoTotale = prezzoTotale;
        this.statoPagmento = statoPagmento;
        this.codDipendente = codDipendente;
        this.cf = cf;
        this.codPacchetto = codPacchetto;
        this.idGruppo = idGruppo;
    }

    public String getCodPrenotazione() {
        return codPrenotazione;
    }

    public void setCodPrenotazione(String codPrenotazione) {
        this.codPrenotazione = codPrenotazione;
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

    public int getPrezzoTotale() {
        return PrezzoTotale;
    }

    public void setPrezzoTotale(int prezzoTotale) {
        PrezzoTotale = prezzoTotale;
    }

    public boolean getStatoPagmento() {
        return statoPagmento;
    }

    public void setStatoPagmento(boolean statoPrenotazione) {
        statoPagmento = statoPrenotazione;
    }

    public int getCodDipendente() {
        return codDipendente;
    }

    public void setCodDipendente(int codDipendente) {
        this.codDipendente = codDipendente;
    }

    public String getCf() {
        return cf;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public String getCodPacchetto() {
        return codPacchetto;
    }

    public void setCodPacchetto(String codPacchetto) {
        this.codPacchetto = codPacchetto;
    }

    public Integer getIdGruppo() {
        return idGruppo;
    }

    public void setIdGruppo(Integer idGruppo) {
        this.idGruppo = idGruppo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((codPrenotazione == null) ? 0 : codPrenotazione.hashCode());
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
        Prenotazione other = (Prenotazione) obj;
        if (codPrenotazione == null) {
            if (other.codPrenotazione != null)
                return false;
        } else if (!codPrenotazione.equals(other.codPrenotazione))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Prenotazione [codPrenotazione=" + codPrenotazione + ", dataInizio=" + dataInizio + ", dataFine="
                + dataFine + ", PrezzoTotale=" + PrezzoTotale + ", statoPagmento=" + statoPagmento + ", codDipendente="
                + codDipendente + ", cf=" + cf + ", codPacchetto=" + codPacchetto + ", idGruppo=" + idGruppo + "]";
    }

}
