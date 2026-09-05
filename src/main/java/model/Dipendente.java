package model;

public class Dipendente {
    private int codDipendente;
    private String nome;
    private String cognome;
    private String username;
    private String pwd;
    private String ruolo;

    public Dipendente() {

    }

    public Dipendente(int codDipendente, String nome, String cognome, String username, String pwd, String ruolo) {
        this.codDipendente = codDipendente;
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.pwd = pwd;
        this.ruolo = ruolo;
    }

    public int getCodDipendente() {
        return codDipendente;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getUsername() {
        return username;
    }

    public String getPwd() {
        return pwd;
    }

    public String getRuolo() {
        return ruolo;
    }

    public void setCodDipendente(int codDipendente) {
        this.codDipendente = codDipendente;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }
    
}
