package controller;

import java.time.LocalDate;
import java.util.List;
import common.Pair;
import common.StoricoNoleggio;
import dao.*;
import model.Prenotazione;
import view.ContabilitaStatisticheView;

public class StatisticheController {

    private final ContabilitaStatisticheView view;
    private final PagamentoDAO pagamentoDao;
    private final PrenotazioneDAO prenotazioneDao;
    private final NoleggioAttrezzaturaDAO noleggioDao;
    private final DipendenteDAO dipendenteDao;
    private final ZonaDAO zonaDao;

    public StatisticheController(ContabilitaStatisticheView view, NoleggioAttrezzaturaDAO noleggioDao) {
        this.view = view;
        this.noleggioDao = noleggioDao;
        this.pagamentoDao = new PagamentoDAO();
        this.prenotazioneDao = new PrenotazioneDAO();
        this.dipendenteDao = new DipendenteDAO();
        this.zonaDao = new ZonaDAO();

        inizializzaListener();
    }

    private void inizializzaListener() {
        view.setOnRegistraPagamentoPrenotazione((importo, dataPag, metodo, codPren) -> {
            boolean ok = pagamentoDao.insertPagamentoPrenotazione(importo, dataPag, metodo, codPren);
            if (ok) {
                prenotazioneDao.updateStatoPagamento(codPren);
                view.mostraMessaggioEsito(true, "Pagamento registrato con successo!", "");
                apriCentroContabilita();
            } else {
                view.mostraMessaggioEsito(false, "", "Errore durante la registrazione del pagamento.");
            }
        });

        view.setOnRegistraPagamentoNoleggio((importo, dataPag, metodo, codNoleggio) -> {
            boolean ok = pagamentoDao.insertPagamentoNoleggio(importo, dataPag, metodo, codNoleggio);
            if (ok) {
                view.mostraMessaggioEsito(true, "Pagamento noleggio registrato con successo!", "");
                apriCentroContabilita();
            } else {
                view.mostraMessaggioEsito(false, "", "Errore durante la registrazione del pagamento.");
            }
        });

        view.setOnReportPerDataAction(data -> {
            Pair<Integer, Integer> report = pagamentoDao.getDailyReport(data);
            view.mostraReportIncassi(report.key(), report.value());
        });

        view.setOnRichiediTopDipendentePrenotazioni(() -> 
            view.mostraTopDipendentePrenotazioni(dipendenteDao.getDipendenteWithMostPrenotazioni())
        );

        view.setOnRichiediTopDipendenteNoleggi(() -> 
            view.mostraTopDipendenteNoleggi(dipendenteDao.getDipendenteWithMostNoleggi())
        );

        view.setOnRichiediOccupazioneZone((dataInizio, dataFine) -> 
            view.mostraStatisticheZone(zonaDao.getPercentualiOccupazioneZone(dataInizio, dataFine))
        );
    }

    public void apriCentroContabilita() {
        List<Pair<Prenotazione, Integer>> nonSaldatePren = prenotazioneDao.getPrenotazioniNonSaldate();
        List<StoricoNoleggio> nonSaldateNol = noleggioDao.getNoleggiNonSaldatiEsterni();
        Pair<Integer, Integer> reportOggi = pagamentoDao.getDailyReport(LocalDate.now());

        view.mostraCentroContabilita(nonSaldatePren, nonSaldateNol, reportOggi.key(), reportOggi.value());
    }
}
