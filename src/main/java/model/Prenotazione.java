package model;

import java.time.LocalDate;

public class Prenotazione {

    private String codPrenotazione;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    private int PrezzoTotale;
    private String StatoPrenotazione;
    private int codDipendente;
    private String cf;
    private String codPacchetto;
    private Integer idGruppo;

    public Prenotazione() {
    }

    public Prenotazione(String codPrenotazione, LocalDate dataInizio, LocalDate dataFine, int prezzoTotale, String statoPrenotazione, int codDipendente, String cf, String codPacchetto, Integer idGruppo) {
        this.codPrenotazione = codPrenotazione;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        PrezzoTotale = prezzoTotale;
        StatoPrenotazione = statoPrenotazione;
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

    public String getStatoPrenotazione() {
        return StatoPrenotazione;
    }

    public void setStatoPrenotazione(String statoPrenotazione) {
        StatoPrenotazione = statoPrenotazione;
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

}
