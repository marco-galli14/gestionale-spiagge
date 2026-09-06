package model;

public class PacchettoSconto {

    private String codPacchetto;
    private String nomePacchetto;
    private int giorniMinimi;
    private int giorniMassimi;
    private int percentualeSconto;

    public PacchettoSconto() {
    }

    public PacchettoSconto(String codPacchetto, String nomePacchetto, int giorniMinimi, int giorniMassimi, int percentualeSconto) {
        this.codPacchetto = codPacchetto;
        this.nomePacchetto = nomePacchetto;
        this.giorniMinimi = giorniMinimi;
        this.giorniMassimi = giorniMassimi;
        this.percentualeSconto = percentualeSconto;
    }

    public String getCodPacchetto() {
        return codPacchetto;
    }

    public void setCodPacchetto(String codPacchetto) {
        this.codPacchetto = codPacchetto;
    }

    public String getNomePacchetto() {
        return nomePacchetto;
    }

    public void setNomePacchetto(String nomePacchetto) {
        this.nomePacchetto = nomePacchetto;
    }

    public int getGiorniMinimi() {
        return giorniMinimi;
    }

    public void setGiorniMinimi(int giorniMinimi) {
        this.giorniMinimi = giorniMinimi;
    }

    public int getGiorniMassimi() {
        return giorniMassimi;
    }

    public void setGiorniMassimi(int giorniMassimi) {
        this.giorniMassimi = giorniMassimi;
    }

    public int getPercentualeSconto() {
        return percentualeSconto;
    }

    public void setPercentualeSconto(int percentualeSconto) {
        this.percentualeSconto = percentualeSconto;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((codPacchetto == null) ? 0 : codPacchetto.hashCode());
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
        PacchettoSconto other = (PacchettoSconto) obj;
        if (codPacchetto == null) {
            if (other.codPacchetto != null)
                return false;
        } else if (!codPacchetto.equals(other.codPacchetto))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PacchettoSconto [codPacchetto=" + codPacchetto + ", nomePacchetto=" + nomePacchetto + ", giorniMinimi="
                + giorniMinimi + ", giorniMassimi=" + giorniMassimi + ", percentualeSconto=" + percentualeSconto + "]";
    }

}
