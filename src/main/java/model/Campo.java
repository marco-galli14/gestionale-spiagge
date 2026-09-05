package model;

public class Campo {
    private String codCampo;
    private String nomeCampo;
    private boolean stato;
    private String tipoCampo;
    private String tipoSport;
    private Boolean richiedeRete;
    private String tipoFondo;

    public Campo() {

    }

    public Campo(String codCampo, String nomeCampo, boolean stato, String tipoCampo, String tipoSport,
            Boolean richiedeRete, String tipoFondo) {
        this.codCampo = codCampo;
        this.nomeCampo = nomeCampo;
        this.stato = stato;
        this.tipoCampo = tipoCampo;
        this.tipoSport = tipoSport;
        this.richiedeRete = richiedeRete;
        this.tipoFondo = tipoFondo;
    }

    public String getCodCampo() {
        return codCampo;
    }

    public String getNomeCampo() {
        return nomeCampo;
    }

    public boolean getStato() {
        return stato;
    }

    public String getTipoCampo() {
        return tipoCampo;
    }

    public String getTipoSport() {
        return tipoSport;
    }

    public Boolean getRichiedeRete() {
        return richiedeRete;
    }

    public String getTipoFondo() {
        return tipoFondo;
    }

    public void setCodCampo(String codCampo) {
        this.codCampo = codCampo;
    }

    public void setNomeCampo(String nomeCampo) {
        this.nomeCampo = nomeCampo;
    }

    public void setStato(boolean stato) {
        this.stato = stato;
    }

    public void setTipoCampo(String tipoCampo) {
        this.tipoCampo = tipoCampo;
    }

    public void setTipoSport(String tipoSport) {
        this.tipoSport = tipoSport;
    }

    public void setRichiedeRete(Boolean richiedeRete) {
        this.richiedeRete = richiedeRete;
    }

    public void setTipoFondo(String tipoFondo) {
        this.tipoFondo = tipoFondo;
    }
    
}
