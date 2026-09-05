package model;

public class Gruppo {
    private int idGruppo;
    private float scontoGruppo;

    public Gruppo() {

    }

    public Gruppo(int idGruppo, float scontoGruppo) {
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

    public void setScontoGruppo(float scontoGruppo) {
        this.scontoGruppo = scontoGruppo;
    }
    
}
