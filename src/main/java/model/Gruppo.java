package model;

public class Gruppo {
    private int idGruppo;
    private int scontoGruppo;

    public Gruppo() {

    }

    public Gruppo(int idGruppo, int scontoGruppo) {
        this.idGruppo = idGruppo;
        this.scontoGruppo = scontoGruppo;
    }

    public int getIdGruppo() {
        return idGruppo;
    }

    public float getScontoGruppo() {
        return scontoGruppo;
    }

    public void setIdGruppo(int idGruppo) {
        this.idGruppo = idGruppo;
    }

    public void setScontoGruppo(int scontoGruppo) {
        this.scontoGruppo = scontoGruppo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + idGruppo;
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
        Gruppo other = (Gruppo) obj;
        if (idGruppo != other.idGruppo)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Gruppo [idGruppo=" + idGruppo + ", scontoGruppo=" + scontoGruppo + "]";
    }
    
}
