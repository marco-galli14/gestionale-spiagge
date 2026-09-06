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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((codSeduta == null) ? 0 : codSeduta.hashCode());
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
        Seduta other = (Seduta) obj;
        if (codSeduta == null) {
            if (other.codSeduta != null)
                return false;
        } else if (!codSeduta.equals(other.codSeduta))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Seduta [codSeduta=" + codSeduta + ", tipo=" + tipo + ", costo=" + costo + "]";
    }
}
