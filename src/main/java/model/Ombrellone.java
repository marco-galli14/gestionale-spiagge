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

}
