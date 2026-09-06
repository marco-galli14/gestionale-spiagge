package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class PrenotazioneCampo {

    private int codPrenotazioneCampo;
    private LocalDate dataPrenotazione;
    private LocalTime oraInizio;
    private LocalTime oraFine;
    private String cf;
    private String codCampo;
    private int codDipendente; // Corretto da CodDipendente a codDipendente

    public PrenotazioneCampo() {
    }

    public PrenotazioneCampo(int codPrenotazioneCampo, LocalDate dataPrenotazione, LocalTime oraInizio, LocalTime oraFine, String cf, String codCampo, int codDipendente) {
        this.codPrenotazioneCampo = codPrenotazioneCampo;
        this.dataPrenotazione = dataPrenotazione;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.cf = cf;
        this.codCampo = codCampo;
        this.codDipendente = codDipendente;
    }

    public int getCodPrenotazioneCampo() {
        return codPrenotazioneCampo;
    }

    public void setCodPrenotazioneCampo(int codPrenotazioneCampo) {
        this.codPrenotazioneCampo = codPrenotazioneCampo;
    }

    public LocalDate getDataPrenotazione() {
        return dataPrenotazione;
    }

    public void setDataPrenotazione(LocalDate dataPrenotazione) {
        this.dataPrenotazione = dataPrenotazione;
    }

    public LocalTime getOraInizio() {
        return oraInizio;
    }

    public void setOraInizio(LocalTime oraInizio) {
        this.oraInizio = oraInizio;
    }

    public LocalTime getOraFine() {
        return oraFine;
    }

    public void setOraFine(LocalTime oraFine) {
        this.oraFine = oraFine;
    }

    public String getCf() {
        return cf;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public String getCodCampo() {
        return codCampo;
    }

    public void setCodCampo(String codCampo) {
        this.codCampo = codCampo;
    }

    public int getCodDipendente() {
        return codDipendente;
    }

    public void setCodDipendente(int codDipendente) {
        this.codDipendente = codDipendente;
    }
}