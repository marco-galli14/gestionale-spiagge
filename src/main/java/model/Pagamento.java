package model;

import java.time.LocalDate;

public class Pagamento {

    private String codPagamento;
    private int importo;
    private LocalDate dataPagamento;
    private String metodoPagamento;
    private String codNoleggio;
    private String codPrenotazione;

    public Pagamento() {
    }

    public Pagamento(String codPagamento, int importo, LocalDate dataPagamento, String metodoPagamento, String codNoleggio, String codPrenotazione) {
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

    public String getCodPrenotazione() {
        return codPrenotazione;
    }

    public void setCodPrenotazione(String codPrenotazione) {
        this.codPrenotazione = codPrenotazione;
    }
}
