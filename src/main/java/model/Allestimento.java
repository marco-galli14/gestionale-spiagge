package model;

import java.time.LocalDate;

public class Allestimento {
    private String codSeduta;
    private String codPrenotazione;
    private int numero;
    private LocalDate dataRiferimento;
    private int quantita;

    public Allestimento() {

    }

    public  Allestimento(String codSeduta,String codPrenotazione,int numero,LocalDate dataRiferimento,int quantita) {
        this.codSeduta = codSeduta;
        this.codPrenotazione = codPrenotazione;
        this.numero = numero;
        this.dataRiferimento = dataRiferimento;
        this.quantita = quantita;
    }

    public String getCodSeduta() {
        return this.codSeduta;
    }

    public String getCodPrenotazione() {
        return this.codPrenotazione;
    }

    public int getNumero() {
        return this.numero;
    }

    public LocalDate getDataRiferimento() {
        return this.dataRiferimento;
    }

    public int getQuantita() {
        return this.quantita;
    }

    public void setCodSeduta(String codSeduta) {
        this.codSeduta = codSeduta;
    }

    public void setCodPrenotazione(String codPrenotazione) {
        this.codPrenotazione = codPrenotazione;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setDataRiferimento(LocalDate dataRiferimento) {
        this.dataRiferimento = dataRiferimento;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

}
