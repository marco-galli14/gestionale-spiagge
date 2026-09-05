package model;

public class Attrezzatura {
    private String codAttrezzatura;
    private String tipo;

    public Attrezzatura() {

    }

    public Attrezzatura(String codAttrezzatura, String tipo) {
        this.codAttrezzatura = codAttrezzatura;
        this.tipo = tipo;
    }

    public String getCodAttrezzatura() {
        return codAttrezzatura;
    }

    public String getTipo() {
        return tipo;
    }

    public void setCodAttrezzatura(String codAttrezzatura) {
        this.codAttrezzatura = codAttrezzatura;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
