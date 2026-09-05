package model;

public class Seduta {
    
    private String codSeduta;
    private String tipo;
    private Integer costo;

    public Seduta() {
    }

    public Seduta(String codSeduta, String tipo, Integer costo) {
        this.codSeduta = codSeduta;
        this.tipo = tipo;
        this.costo = costo;
    }

    public String getCodSeduta() {
        return codSeduta;
    }

    public void setCodSeduta(String codSeduta) {
        this.codSeduta = codSeduta;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getCosto() {
        return costo;
    }

    public void setCosto(Integer costo) {
        this.costo = costo;
    }
}
