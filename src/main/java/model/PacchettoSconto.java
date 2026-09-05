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

}
