package model;

import java.time.LocalDate;

public class Pagamento {

    private String codPagamento;
    private int importo;
    private LocalDate dataPagamento;
    private String metodoPagamento;
    private String codNoleggio;
    private Integer codPrenotazione;

    public Pagamento() {
    }

    public Pagamento(String codPagamento, int importo, LocalDate dataPagamento, String metodoPagamento, String codNoleggio, Integer codPrenotazione) {
        this.codPagamento = codPagamento;
        this.importo = importo;
        this.dataPagamento = dataPagamento;
        this.metodoPagamento = metodoPagamento;
        this.codNoleggio = codNoleggio;
        this.codPrenotazione = codPrenotazione;
    }

    public String getCodPagamento() {
        return codPagamento;
    }

    public void setCodPagamento(String codPagamento) {
        this.codPagamento = codPagamento;
    }

    public int getImporto() {
        return importo;
    }

    public void setImporto(int importo) {
        this.importo = importo;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public String getCodNoleggio() {
        return codNoleggio;
    }

    public void setCodNoleggio(String codNoleggio) {
        this.codNoleggio = codNoleggio;
    }

    public Integer getCodPrenotazione() {
        return codPrenotazione;
    }

    public void setCodPrenotazione(Integer codPrenotazione) {
        this.codPrenotazione = codPrenotazione;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((codPagamento == null) ? 0 : codPagamento.hashCode());
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
        Pagamento other = (Pagamento) obj;
        if (codPagamento == null) {
            if (other.codPagamento != null)
                return false;
        } else if (!codPagamento.equals(other.codPagamento))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Pagamento [codPagamento=" + codPagamento + ", importo=" + importo + ", dataPagamento=" + dataPagamento
                + ", metodoPagamento=" + metodoPagamento + ", codNoleggio=" + codNoleggio + ", codPrenotazione="
                + codPrenotazione + "]";
    }
}
