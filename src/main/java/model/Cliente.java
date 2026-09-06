package model;

public class Cliente {
    private String cf;
    private String nome;
    private String cognome;
    private String email;
    private String telefono;
    private Integer codHotel;
    private Integer idGruppo;

    public Cliente() {

    }

    public Cliente(String cf, String nome, String cognome, String email, String telefono, Integer codHotel,
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

    public String getTelefono() {
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

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setCodHotel(Integer codHotel) {
        this.codHotel = codHotel;
    }

    public void setIdGruppo(Integer idGruppo) {
        this.idGruppo = idGruppo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cf == null) ? 0 : cf.hashCode());
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
        Cliente other = (Cliente) obj;
        if (cf == null) {
            if (other.cf != null)
                return false;
        } else if (!cf.equals(other.cf))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Cliente [cf=" + cf + ", nome=" + nome + ", cognome=" + cognome + ", email=" + email + ", telefono="
                + telefono + ", codHotel=" + codHotel + ", idGruppo=" + idGruppo + "]";
    }
}
