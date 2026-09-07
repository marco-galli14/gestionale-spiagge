package model;

import java.time.LocalDate;

public class Pagamento {

    private int codPagamento; // Aggiornato a int
    private int importo;
    private LocalDate dataPagamento;
    private String metodoPagamento;
    private Integer codNoleggio;
    private Integer codPrenotazione;

    public Pagamento() {
    }

    public Pagamento(int codPagamento, int importo, LocalDate dataPagamento, String metodoPagamento, Integer codNoleggio, Integer codPrenotazione) {
        this.codPagamento = codPagamento;
        this.importo = importo;
        this.dataPagamento = dataPagamento;
        this.metodoPagamento = metodoPagamento;
        this.codNoleggio = codNoleggio;
        this.codPrenotazione = codPrenotazione;
    }

    public int getCodPagamento() {
        return codPagamento;
    }

    public void setCodPagamento(int codPagamento) {
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

    public Integer getCodNoleggio() {
        return codNoleggio;
    }

    public void setCodNoleggio(Integer codNoleggio) {
        this.codNoleggio = codNoleggio;
    }

    public Integer getCodPrenotazione() {
        return codPrenotazione;
    }

    public void setCodPrenotazione(Integer codPrenotazione) {
        this.codPrenotazione = codPrenotazione;
    }
}