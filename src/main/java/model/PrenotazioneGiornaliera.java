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
    
}