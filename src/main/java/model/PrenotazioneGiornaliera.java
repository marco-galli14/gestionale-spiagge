package model;

import java.time.LocalDate;

public class PrenotazioneGiornaliera {

    private String CodPrenotazione;
    private Integer numero;
    private LocalDate dataRiferimento;

    public PrenotazioneGiornaliera() {
    }
    
    public PrenotazioneGiornaliera(String codPrenotazione, Integer numero, LocalDate dataRiferimento) {
        CodPrenotazione = codPrenotazione;
        this.numero = numero;
        this.dataRiferimento = dataRiferimento;
    }

    public String getCodPrenotazione() {
        return CodPrenotazione;
    }

    public void setCodPrenotazione(String codPrenotazione) {
        CodPrenotazione = codPrenotazione;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public LocalDate getDataRiferimento() {
        return dataRiferimento;
    }

    public void setDataRiferimento(LocalDate dataRiferimento) {
        this.dataRiferimento = dataRiferimento;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((CodPrenotazione == null) ? 0 : CodPrenotazione.hashCode());
        result = prime * result + ((numero == null) ? 0 : numero.hashCode());
        result = prime * result + ((dataRiferimento == null) ? 0 : dataRiferimento.hashCode());
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
        PrenotazioneGiornaliera other = (PrenotazioneGiornaliera) obj;
        if (CodPrenotazione == null) {
            if (other.CodPrenotazione != null)
                return false;
        } else if (!CodPrenotazione.equals(other.CodPrenotazione))
            return false;
        if (numero == null) {
            if (other.numero != null)
                return false;
        } else if (!numero.equals(other.numero))
            return false;
        if (dataRiferimento == null) {
            if (other.dataRiferimento != null)
                return false;
        } else if (!dataRiferimento.equals(other.dataRiferimento))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PrenotazioneGiornaliera [CodPrenotazione=" + CodPrenotazione + ", numero=" + numero
                + ", dataRiferimento=" + dataRiferimento + "]";
    }
    
}