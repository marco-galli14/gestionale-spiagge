package model;

public class Ombrellone {

    private int Numero;
    private int codZona;

    public Ombrellone() {
    }

    public Ombrellone(int numero, int codZona) {
        this.Numero = numero;
        this.codZona = codZona;
    }

    public int getNumero() {
        return Numero;
    }

    public void setNumero(int numero) {
        Numero = numero;
    }

    public int getCodZona() {
        return codZona;
    }

    public void setCodZona(int codZona) {
        this.codZona = codZona;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Numero;
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
        Ombrellone other = (Ombrellone) obj;
        if (Numero != other.Numero)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Ombrellone [Numero=" + Numero + ", codZona=" + codZona + "]";
    }

}
