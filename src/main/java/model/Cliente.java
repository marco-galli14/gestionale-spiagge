package model;

public class Cliente {
    private String cf;
    private String nome;
    private String cognome;
    private String email;
    private int telefono;
    private Integer codHotel;
    private Integer idGruppo;

    public Cliente() {

    }

    public Cliente(String cf, String nome, String cognome, String email, int telefono, Integer codHotel,
            Integer idGruppo) {
        this.cf = cf;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.telefono = telefono;
        this.codHotel = codHotel;
        this.idGruppo = idGruppo;
    }

    public String getCf() {
        return cf;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getEmail() {
        return email;
    }

    public int getTelefono() {
        return telefono;
    }

    public Integer getCodHotel() {
        return codHotel;
    }

    public Integer getIdGruppo() {
        return idGruppo;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public void setCodHotel(Integer codHotel) {
        this.codHotel = codHotel;
    }

    public void setIdGruppo(Integer idGruppo) {
        this.idGruppo = idGruppo;
    }
}
