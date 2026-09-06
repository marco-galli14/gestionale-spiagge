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

    public int getScontoHotel() { // Corretto da float a int
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + codHotel;
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
        Hotel other = (Hotel) obj;
        if (codHotel != other.codHotel)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Hotel [codHotel=" + codHotel + ", nomeHotel=" + nomeHotel + ", scontoHotel=" + scontoHotel + "]";
    }
}