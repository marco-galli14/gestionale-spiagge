package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class NoleggioAttrezzatura {

    private String codNoleggio;
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

    public NoleggioAttrezzatura(String codNoleggio, LocalDate dataNoleggio, LocalTime oraInizio, int durataOre, int costoTotale, int codDipendente, String cf, String codAttrezzatura, String codPrenotazione) {
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

    public String getCodNoleggio() {
        return codNoleggio;
    }

    public void setCodNoleggio(String codNoleggio) {
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((codNoleggio == null) ? 0 : codNoleggio.hashCode());
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
        NoleggioAttrezzatura other = (NoleggioAttrezzatura) obj;
        if (codNoleggio == null) {
            if (other.codNoleggio != null)
                return false;
        } else if (!codNoleggio.equals(other.codNoleggio))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "NoleggioAttrezzatura [codNoleggio=" + codNoleggio + ", dataNoleggio=" + dataNoleggio + ", oraInizio="
                + oraInizio + ", durataOre=" + durataOre + ", costoTotale=" + costoTotale + ", codDipendente="
                + codDipendente + ", cf=" + cf + ", codAttrezzatura=" + codAttrezzatura + ", codPrenotazione="
                + codPrenotazione + "]";
    }

}
