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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((codSeduta == null) ? 0 : codSeduta.hashCode());
        result = prime * result + ((codPrenotazione == null) ? 0 : codPrenotazione.hashCode());
        result = prime * result + numero;
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
        Allestimento other = (Allestimento) obj;
        if (codSeduta == null) {
            if (other.codSeduta != null)
                return false;
        } else if (!codSeduta.equals(other.codSeduta))
            return false;
        if (codPrenotazione == null) {
            if (other.codPrenotazione != null)
                return false;
        } else if (!codPrenotazione.equals(other.codPrenotazione))
            return false;
        if (numero != other.numero)
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
        return "Allestimento [codSeduta=" + codSeduta + ", codPrenotazione=" + codPrenotazione + ", numero=" + numero
                + ", dataRiferimento=" + dataRiferimento + ", quantita=" + quantita + "]";
    }

}
