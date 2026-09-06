package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class PrenotazioneCampo {

    private String codPrenotazioneCampo;
    private LocalDate dataPrenotazione;
    private LocalTime oraInizio;
    private LocalTime oraFine;
    private String cf;
    private String codCampo;
    private int CodDipendente;

    public PrenotazioneCampo() {
    }

    public PrenotazioneCampo(String codPrenotazioneCampo, LocalDate dataPrenotazione, LocalTime oraInizio, LocalTime oraFine, String cf, String codCampo, int codDipendente) {
        this.codPrenotazioneCampo = codPrenotazioneCampo;
        this.dataPrenotazione = dataPrenotazione;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.cf = cf;
        this.codCampo = codCampo;
        CodDipendente = codDipendente;
    }

    public String getCodPrenotazioneCampo() {
        return codPrenotazioneCampo;
    }

    public void setCodPrenotazioneCampo(String codPrenotazioneCampo) {
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
        return CodDipendente;
    }

    public void setCodDipendente(int codDipendente) {
        CodDipendente = codDipendente;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((codPrenotazioneCampo == null) ? 0 : codPrenotazioneCampo.hashCode());
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
        PrenotazioneCampo other = (PrenotazioneCampo) obj;
        if (codPrenotazioneCampo == null) {
            if (other.codPrenotazioneCampo != null)
                return false;
        } else if (!codPrenotazioneCampo.equals(other.codPrenotazioneCampo))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PrenotazioneCampo [codPrenotazioneCampo=" + codPrenotazioneCampo + ", dataPrenotazione="
                + dataPrenotazione + ", oraInizio=" + oraInizio + ", oraFine=" + oraFine + ", cf=" + cf + ", codCampo="
                + codCampo + ", CodDipendente=" + CodDipendente + "]";
    }
    
}
