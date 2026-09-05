package model;

public class Hotel {
    private int codHotel;
    private String nomeHotel;
    private int scontoHotel;

    public Hotel() {

    }

    public Hotel(int codHotel, String nomeHotel, int scontoHotel) {
        this.codHotel = codHotel;
        this.nomeHotel = nomeHotel;
        this.scontoHotel = scontoHotel;
    }

    public int getCodHotel() {
        return codHotel;
    }

    public String getNomeHotel() {
        return nomeHotel;
    }

    public float getScontoHotel() {
        return scontoHotel;
    }

    public void setCodHotel(int codHotel) {
        this.codHotel = codHotel;
    }

    public void setNomeHotel(String nomeHotel) {
        this.nomeHotel = nomeHotel;
    }

    public void setScontoHotel(int scontoHotel) {
        this.scontoHotel = scontoHotel;
    }
}
