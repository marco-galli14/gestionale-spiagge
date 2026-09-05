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
    
}
