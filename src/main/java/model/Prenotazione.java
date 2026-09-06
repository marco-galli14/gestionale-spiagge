package model;

import java.time.LocalDate;

public class Prenotazione {

    private int codPrenotazione;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    private int prezzoTotale;
    private boolean statoPagamento; // Corretto il typo da statoPagmento
    private int codDipendente;
    private String cf;
    private String codPacchetto;
    private Integer idGruppo;

    public Prenotazione() {
    }

    public Prenotazione(int codPrenotazione, LocalDate dataInizio, LocalDate dataFine, int prezzoTotale,
            boolean statoPagamento, int codDipendente, String cf, String codPacchetto, Integer idGruppo) {
        this.codPrenotazione = codPrenotazione;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.prezzoTotale = prezzoTotale;
        this.statoPagamento = statoPagamento;
        this.codDipendente = codDipendente;
        this.cf = cf;
        this.codPacchetto = codPacchetto;
        this.idGruppo = idGruppo;
    }

    public int getCodPrenotazione() {
        return codPrenotazione;
    }

    public void setCodPrenotazione(int codPrenotazione) {
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
        return prezzoTotale;
    }

    public void setPrezzoTotale(int prezzoTotale) {
        this.prezzoTotale = prezzoTotale;
    }

    public boolean getStatoPagamento() {
        return statoPagamento;
    }

    public void setStatoPagamento(boolean statoPagamento) {
        this.statoPagamento = statoPagamento;
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