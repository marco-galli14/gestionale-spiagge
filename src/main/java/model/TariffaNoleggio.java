package model;

public class TariffaNoleggio {
    private String codStagione;
    private String codAttrezzatura;
    private int tariffaOraria; // Convertito a int primitivo

    public TariffaNoleggio() {
    }

    public TariffaNoleggio(String codStagione, String codAttrezzatura, int tariffaOraria) {
        this.codStagione = codStagione;
        this.codAttrezzatura = codAttrezzatura;
        this.tariffaOraria = tariffaOraria;
    }

    public String getCodStagione() {
        return codStagione;
    }

    public void setCodStagione(String codStagione) {
        this.codStagione = codStagione;
    }

    public String getCodAttrezzatura() {
        return codAttrezzatura;
    }

    public void setCodAttrezzatura(String codAttrezzatura) {
        this.codAttrezzatura = codAttrezzatura;
    }

    public int getTariffaOraria() {
        return tariffaOraria;
    }

    public void setTariffaOraria(int tariffaOraria) {
        this.tariffaOraria = tariffaOraria;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((codStagione == null) ? 0 : codStagione.hashCode());
        result = prime * result + ((codAttrezzatura == null) ? 0 : codAttrezzatura.hashCode());
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
        TariffaNoleggio other = (TariffaNoleggio) obj;
        if (codStagione == null) {
            if (other.codStagione != null)
                return false;
        } else if (!codStagione.equals(other.codStagione))
            return false;
        if (codAttrezzatura == null) {
            if (other.codAttrezzatura != null)
                return false;
        } else if (!codAttrezzatura.equals(other.codAttrezzatura))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "TariffaNoleggio [codStagione=" + codStagione + ", codAttrezzatura=" + codAttrezzatura
                + ", tariffaOraria=" + tariffaOraria + "]";
    }
}