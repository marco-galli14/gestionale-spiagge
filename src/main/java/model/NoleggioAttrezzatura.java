package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class NoleggioAttrezzatura {

    private int codNoleggio;
    private LocalDate dataNoleggio;
    private LocalTime oraInizio;
    private int durataOre;
    private int costoTotale;
    private int codDipendente;
    private String cf;
    private String codAttrezzatura;
    private String codPrenotazione;

    public NoleggioAttrezzatura() {
    }

    public NoleggioAttrezzatura(int codNoleggio, LocalDate dataNoleggio, LocalTime oraInizio, int durataOre, int costoTotale, int codDipendente, String cf, String codAttrezzatura, String codPrenotazione) {
        this.codNoleggio = codNoleggio;
        this.dataNoleggio = dataNoleggio;
        this.oraInizio = oraInizio;
        this.durataOre = durataOre;
        this.costoTotale = costoTotale;
        this.codDipendente = codDipendente;
        this.cf = cf;
        this.codAttrezzatura = codAttrezzatura;
        this.codPrenotazione = codPrenotazione;
    }

    public int getCodNoleggio() {
        return codNoleggio;
    }

    public void setCodNoleggio(int codNoleggio) {
        this.codNoleggio = codNoleggio;
    }

    public LocalDate getDataNoleggio() {
        return dataNoleggio;
    }

    public void setDataNoleggio(LocalDate dataNoleggio) {
        this.dataNoleggio = dataNoleggio;
    }

    public LocalTime getOraInizio() {
        return oraInizio;
    }

    public void setOraInizio(LocalTime oraInizio) {
        this.oraInizio = oraInizio;
    }

    public int getDurataOre() {
        return durataOre;
    }

    public void setDurataOre(int durataOre) {
        this.durataOre = durataOre;
    }

    public int getCostoTotale() {
        return costoTotale;
    }

    public void setCostoTotale(int costoTotale) {
        this.costoTotale = costoTotale;
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

    public String getCodAttrezzatura() {
        return codAttrezzatura;
    }

    public void setCodAttrezzatura(String codAttrezzatura) {
        this.codAttrezzatura = codAttrezzatura;
    }

    public String getCodPrenotazione() {
        return codPrenotazione;
    }

    public void setCodPrenotazione(String codPrenotazione) {
        this.codPrenotazione = codPrenotazione;
    }

}