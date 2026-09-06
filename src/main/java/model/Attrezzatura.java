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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((codAttrezzatura == null) ? 0 : codAttrezzatura.hashCode());
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
        Attrezzatura other = (Attrezzatura) obj;
        if (codAttrezzatura == null) {
            if (other.codAttrezzatura != null)
                return false;
        } else if (!codAttrezzatura.equals(other.codAttrezzatura))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Attrezzatura [codAttrezzatura=" + codAttrezzatura + ", tipo=" + tipo + "]";
    }
}
