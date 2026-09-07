package controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import common.Pair;
import common.StoricoNoleggio;
import common.StoricoPrenotazione;
import dao.AllestimentoDAO;
import dao.ClienteDAO;
import dao.DipendenteDAO;
import dao.MappaDAO;
import dao.NoleggioAttrezzaturaDAO;
import dao.PagamentoDAO;
import dao.PrenotazioneCampoDAO;
import dao.PrenotazioneDAO;
import dao.PrenotazioneGiornalieraDAO;
import dao.ZonaDAO;
import javafx.stage.Stage;
import model.Allestimento;
import model.Cliente;
import model.Dipendente;
import model.Prenotazione;
import model.PrenotazioneCampo;
import model.PrenotazioneGiornaliera;
import view.MainView;

public class MainController {

    private final MainView view;
    private final MappaDAO mappaDao;
    private final ClienteDAO clienteDao;
    private final PrenotazioneDAO prenotazioneDao;
    private final NoleggioAttrezzaturaDAO noleggioAttrezzaturaDao;
    private final PrenotazioneCampoDAO prenotazioneCampoDao;
    private final PagamentoDAO pagamentoDao;
    private final DipendenteDAO dipendenteDao;
    private final ZonaDAO zonaDao;
    private final PrenotazioneGiornalieraDAO prenotazioneGiornalieraDao;
    private final AllestimentoDAO allestimentoDao;

    public MainController(Stage stage) {
        this.view = new MainView(stage);
        this.mappaDao = new MappaDAO();
        this.clienteDao = new ClienteDAO();
        this.prenotazioneDao = new PrenotazioneDAO();
        this.noleggioAttrezzaturaDao = new NoleggioAttrezzaturaDAO();
        this.prenotazioneCampoDao = new PrenotazioneCampoDAO();
        this.pagamentoDao = new PagamentoDAO();
        this.dipendenteDao = new DipendenteDAO();
        this.zonaDao = new ZonaDAO();
        this.prenotazioneGiornalieraDao = new PrenotazioneGiornalieraDAO();
        this.allestimentoDao = new AllestimentoDAO();

        inizializzaListener();
    }

    public void avvia() {
        // Prima costruiamo la grafica (creando la gridOmbrelloni in memoria)
        view.mostraFinestra();
        // ORA carichiamo i dati nella griglia
        aggiornaDatiMappa(LocalDate.now(), LocalTime.now());
    }

    private void inizializzaListener() {
        view.setOnCambioDataOraMappa(this::aggiornaDatiMappa);

        view.setOnCellaOmbrelloneClicked(numeroCella -> {
            LocalDate data = view.getDataSelezionata();
            List<MappaDAO.MappaOmbrelloneInfo> infoCelle = mappaDao.getMappaSpiaggia(data);
            MappaDAO.MappaOmbrelloneInfo info = infoCelle.stream()
                    .filter(c -> c.getNumero() == numeroCella)
                    .findFirst()
                    .orElse(null);

            if (info != null && info.isOccupato()) {
                view.mostraDettagliPrenotazioneCella(info.getCodZona(), info.getNomeZona(), info.getCodPrenotazione(), info.getClientePrenotato());
            } else {
                view.apriFormNuovaPrenotazionePerCella(numeroCella);
            }
        });

        view.setOnRichiediListaClienti(clienteDao::getTuttiIClienti);

        view.setOnSalvaClienteAction((cf, nome, cognome, email, telefono, codHotel) -> {
            boolean successo = clienteDao.insertCliente(cf, nome, cognome, email, telefono, codHotel);
            if (!successo) {
                view.mostraMessaggioEsito(false, "", "Errore durante il salvataggio del cliente.");
            }
        });

        view.setOnCreaPrenotazioneAction((dataInizio, dataFine, codDip, cf, ombrelloni, lettini, sdraio, sedie) -> {
            int codPren = prenotazioneDao.addPrenotazione(dataInizio, dataFine, codDip, cf);
            if (codPren != -1) {
                prenotazioneDao.updatePacchettoSconto(codPren);
                
                for (int numOmb : ombrelloni) {
                    LocalDate currentDate = dataInizio;
                    while (!currentDate.isAfter(dataFine)) {
                        prenotazioneGiornalieraDao.addPrenotazioneGiornaliera(codPren, numOmb, currentDate);
                        
                        if (lettini > 0) allestimentoDao.addAllestimento("LETTINO", codPren, numOmb, currentDate, lettini);
                        if (sdraio > 0) allestimentoDao.addAllestimento("SDRAIO", codPren, numOmb, currentDate, sdraio);
                        if (sedie > 0) allestimentoDao.addAllestimento("SEDIA", codPren, numOmb, currentDate, sedie);
                        
                        currentDate = currentDate.plusDays(1);
                    }
                }
                
                prenotazioneDao.updateCostoTotale(codPren);
                view.mostraMessaggioEsito(true, "Prenotazione creata con successo!", "");
                aggiornaDatiMappa(view.getDataSelezionata(), LocalTime.now());
            } else {
                view.mostraMessaggioEsito(false, "", "Impossibile creare la prenotazione.");
            }
        });

        view.setOnCreaNoleggioAction((data, oraInizio, durata, cf, codDip, codAttrezzatura) -> {
            int codNol = noleggioAttrezzaturaDao.inserisciNoleggioAttrezzatura(data, oraInizio, durata, cf, codDip, codAttrezzatura);
            if (codNol != -1) {
                noleggioAttrezzaturaDao.aggiornaCostoTotaleNoleggio(codNol);
                view.mostraMessaggioEsito(true, "Noleggio salvato con successo!", "");
                aggiornaDatiMappa(view.getDataSelezionata(), LocalTime.now());
            } else {
                view.mostraMessaggioEsito(false, "", "Errore durante il salvataggio del noleggio.");
            }
        });

        view.setOnEliminaNoleggioAction(codNoleggio -> {
            boolean ok = noleggioAttrezzaturaDao.eliminaNoleggioAttrezzatura(codNoleggio);
            view.mostraMessaggioEsito(ok, "Noleggio eliminato con successo.", "Errore durante l'eliminazione del noleggio.");
            aggiornaDatiMappa(view.getDataSelezionata(), LocalTime.now());
        });

        view.setOnCreaPrenotazioneCampoAction((data, oraInizio, oraFine, codCampo, cf, codDip) -> {
            boolean ok = prenotazioneCampoDao.inserisciPrenotazioneCampo(data, oraInizio, oraFine, codCampo, cf, codDip);
            view.mostraMessaggioEsito(ok, "Prenotazione campo salvata con successo!", "Errore durante il salvataggio della prenotazione campo.");
            aggiornaDatiMappa(view.getDataSelezionata(), LocalTime.now());
        });

        view.setOnEliminaPrenotazioneCampoAction(codCampi -> {
            boolean ok = prenotazioneCampoDao.eliminaPrenotazioneCampo(codCampi);
            view.mostraMessaggioEsito(ok, "Prenotazione campo eliminata.", "Errore durante l'eliminazione.");
            aggiornaDatiMappa(view.getDataSelezionata(), LocalTime.now());
        });

        // REGISTRAZIONE PAGAMENTI
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

        view.setOnRichiediTopDipendentePrenotazioni(() -> {
            Pair<Dipendente, Integer> top = dipendenteDao.getDipendenteWithMostPrenotazioni();
            view.mostraTopDipendentePrenotazioni(top);
        });

        view.setOnRichiediTopDipendenteNoleggi(() -> {
            Pair<Dipendente, Integer> top = dipendenteDao.getDipendenteWithMostNoleggi();
            view.mostraTopDipendenteNoleggi(top);
        });

        view.setOnRichiediOccupazioneZone((dataInizio, dataFine) -> {
            List<ZonaDAO.ZonaOccupazioneInfo> info = zonaDao.getPercentualiOccupazioneZone(dataInizio, dataFine);
            view.mostraStatisticheZone(info);
        });

        view.setOnRichiediStoricoPrenotazioni(() -> {
            List<StoricoPrenotazione> storico = prenotazioneDao.getStoricoPrenotazioni();
            view.mostraStoricoPrenotazioni(storico);
        });

        view.setOnEliminaPrenotazioneSpiaggiaAction(codPren -> {
            boolean ok = prenotazioneDao.eliminaPrenotazione(codPren);
            view.mostraMessaggioEsito(ok, "Prenotazione eliminata con successo.", "Impossibile eliminare la prenotazione.");
            aggiornaDatiMappa(view.getDataSelezionata(), LocalTime.now());
        });

        view.setOnModificaAllestimentoGiornaliero((codPren, numOmb, data, lettini, sdraio, sedie) -> {
            allestimentoDao.deleteAllestimento("LETTINO", codPren, numOmb, data);
            allestimentoDao.deleteAllestimento("SDRAIO", codPren, numOmb, data);
            allestimentoDao.deleteAllestimento("SEDIA", codPren, numOmb, data);
            
            if (lettini > 0) allestimentoDao.addAllestimento("LETTINO", codPren, numOmb, data, lettini);
            if (sdraio > 0) allestimentoDao.addAllestimento("SDRAIO", codPren, numOmb, data, sdraio);
            if (sedie > 0) allestimentoDao.addAllestimento("SEDIA", codPren, numOmb, data, sedie);

            prenotazioneDao.updateCostoTotale(codPren);
            view.mostraMessaggioEsito(true, "Allestimento aggiornato con successo!", "");
            aggiornaDatiMappa(view.getDataSelezionata(), LocalTime.now());
        });

        view.setOnRichiediAllestimentiGiorno((codPren, numOmb, data) -> allestimentoDao.getAllestimenti(codPren, numOmb, data));

        view.setOnRichiediPrenotazioniGiornaliere(codPrenotazione -> {
            List<PrenotazioneGiornaliera> lista = prenotazioneGiornalieraDao.getPrenotazioniGiornaliere(codPrenotazione);
            view.mostraPrenotazioniGiornaliere(lista);
        });

        view.setOnVisualizzaContabilitaAction(this::apriCentroContabilita);

        view.setOnReportPerDataAction(data -> {
            Pair<Integer, Integer> report = pagamentoDao.getDailyReport(data);
            view.mostraReportIncassi(report.key(), report.value());
        });
    }

    private void apriCentroContabilita() {
        // Recuperiamo il Pair aggiornato
        List<Pair<Prenotazione, Integer>> nonSaldatePren = prenotazioneDao.getPrenotazioniNonSaldate();
        List<StoricoNoleggio> nonSaldateNol = noleggioAttrezzaturaDao.getNoleggiNonSaldatiEsterni();
        Pair<Integer, Integer> reportOggi = pagamentoDao.getDailyReport(LocalDate.now());
        
        view.impostaTabellaNoleggi(noleggioAttrezzaturaDao.getStoricoNoleggi());
        view.mostraCentroContabilita(nonSaldatePren, nonSaldateNol, reportOggi.key(), reportOggi.value());
    }

    private void aggiornaDatiMappa(LocalDate data, LocalTime ora) {
        List<MappaDAO.MappaOmbrelloneInfo> infoCelle = mappaDao.getMappaSpiaggia(data);
        view.aggiornaGrigliaSpiaggia(infoCelle);

        List<PrenotazioneCampo> campiOccupati = mappaDao.getOccupazioneCampi(data, ora);
        view.mostraCampiOccupati(campiOccupati);

        List<StoricoNoleggio> storicoNoleggi = noleggioAttrezzaturaDao.getStoricoNoleggi();
        view.impostaTabellaNoleggi(storicoNoleggi);
    }
}