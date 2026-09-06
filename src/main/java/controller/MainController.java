package controller;

import java.time.LocalDate;
import java.util.List;

import common.Pair;
import dao.ClienteDAO;
import dao.DipendenteDAO;
import dao.MappaDAO;
import dao.NoleggioAttrezzaturaDAO;
import dao.PagamentoDAO;
import dao.PrenotazioneCampoDAO;
import dao.PrenotazioneDAO;
import dao.ZonaDAO;
import model.Dipendente;
import model.PrenotazioneCampo;
import view.MainView;

public final class MainController {

    private final MainView view;
    
    private final ClienteDAO clienteDao;
    private final PrenotazioneDAO prenotazioneDao;
    private final MappaDAO mappaDao;
    private final NoleggioAttrezzaturaDAO noleggioAttrezzaturaDao;
    private final PagamentoDAO pagamentoDao;
    private final DipendenteDAO dipendenteDao;
    private final ZonaDAO zonaDao;
    private final PrenotazioneCampoDAO prenotazioneCampoDao;

    public MainController(MainView view) {
        this.view = view;
        
        this.clienteDao = new ClienteDAO();
        this.prenotazioneDao = new PrenotazioneDAO();
        this.mappaDao = new MappaDAO();
        this.noleggioAttrezzaturaDao = new NoleggioAttrezzaturaDAO();
        this.pagamentoDao = new PagamentoDAO();
        this.dipendenteDao = new DipendenteDAO();
        this.zonaDao = new ZonaDAO();
        this.prenotazioneCampoDao = new PrenotazioneCampoDAO();

        inizializzaEventiUI();
        ricaricaDatiGlobali();
    }

    private void inizializzaEventiUI() {
        
        view.setOnCellaOmbrelloneClicked(numeroCella -> {
            gestisciInterazioneCellaSpiaggia(numeroCella);
        });

        view.setOnCambioDataMappa(dataRiferimento -> {
            aggiornaMappaSpiaggia(dataRiferimento);
        });

        view.setOnSalvaClienteAction((cf, nome, cognome, email, telefono, codHotel) -> {
            boolean ok = clienteDao.insertCliente(cf, nome, cognome, email, telefono, codHotel);
            view.mostraMessaggioEsito(ok, "Cliente registrato con successo!", "Errore durante la registrazione del cliente.");
        });

        view.setOnAssegnaGruppoCliente((cf, idGruppo) -> {
            boolean ok = clienteDao.setGroup(cf, idGruppo);
            view.mostraMessaggioEsito(ok, "Gruppo assegnato con successo!", "Errore nell'assegnazione del gruppo.");
        });

        view.setOnCreaPrenotazioneAction((codPrenotazione, dataInizio, dataFine, codDipendente, cf) -> {
            boolean creata = prenotazioneDao.addPrenotazione(codPrenotazione, dataInizio, dataFine, codDipendente, cf);
            if (creata) {
                prenotazioneDao.updatePacchettoSconto(codPrenotazione);
                prenotazioneDao.updateCostoTotale(codPrenotazione);
                view.aggiornaTabellaPrenotazioni(prenotazioneDao.getStoricoPrenotazioni());
            }
        });

        view.setOnCreaNoleggioAction((data, oraInizio, durata, cf, codDipendente, codAttrezzatura) -> {
            String codGenerato = noleggioAttrezzaturaDao.inserisciNoleggioAttrezzatura(data, oraInizio, durata, cf, codDipendente, codAttrezzatura);
            
            if (codGenerato != null) {
                noleggioAttrezzaturaDao.aggiornaCostoTotaleNoleggio(codGenerato);
                view.aggiornaTabellaNoleggi(noleggioAttrezzaturaDao.getStoricoNoleggi());
                view.mostraMessaggioEsito(true, "Noleggio inserito! Codice assegnato: " + codGenerato, "");
            } else {
                view.mostraMessaggioEsito(false, "", "Errore: Controlla che i dati siano corretti e che il cliente abbia una prenotazione attiva.");
            }
        });

        view.setOnEliminaNoleggioAction(codNoleggio -> {
            boolean eliminato = noleggioAttrezzaturaDao.eliminaNoleggioAttrezzatura(codNoleggio);
            if (eliminato) {
                view.aggiornaTabellaNoleggi(noleggioAttrezzaturaDao.getStoricoNoleggi());
                view.mostraMessaggioEsito(true, "Noleggio " + codNoleggio + " eliminato con successo!", "");
            } else {
                view.mostraMessaggioEsito(false, "", "Errore: Noleggio non trovato o impossibile da eliminare.");
            }
        });

        view.setOnVerificaCampiAction((data, ora) -> {
            List<PrenotazioneCampo> campiOccupati = mappaDao.getOccupazioneCampi(data, ora);
            view.mostraCampiOccupati(campiOccupati);
        });

        view.setOnCreaPrenotazioneCampoAction((codPren, oraInizio, oraFine, codCampo, cf, codDip) -> {
            boolean ok = prenotazioneCampoDao.inserisciPrenotazioneCampo(codPren, oraInizio, oraFine, codCampo, cf, codDip);
            view.mostraMessaggioEsito(ok, "Campo prenotato con successo!", "Errore nella prenotazione del campo.");
        });

        view.setOnRegistraPagamentoPrenotazione((codPag, importo, data, metodo, codPren) -> {
            boolean ok = pagamentoDao.insertPagamentoPrenotazione(codPag, importo, data, metodo, codPren);
            if (ok) {
                prenotazioneDao.updateStatoPagamento(codPren);
                view.aggiornaListaPrenotazioniNonSaldate(prenotazioneDao.getPrenotazioniNonSaldate());
            }
        });

        view.setOnGeneraReportGiornaliero(data -> {
            Pair<Integer, Integer> report = pagamentoDao.getDailyReport(data);
            view.mostraReportIncassi(report.key(), report.value());
        });

        view.setOnRichiediStatisticheDipendenti(() -> {
            Pair<Dipendente, Integer> topPrenotazioni = dipendenteDao.getDipendenteWithMostPrenotazioni();
            Pair<Dipendente, Integer> topNoleggi = dipendenteDao.getDipendenteWithMostNoleggi();
            view.mostraStatisticheDipendenti(topPrenotazioni, topNoleggi);
        });
        
        view.setOnRichiediOccupazioneZone((dataInizio, dataFine) -> {
            List<ZonaDAO.ZonaOccupazioneInfo> percentuali = zonaDao.getPercentualiOccupazioneZone(dataInizio, dataFine);
            view.mostraStatisticheZone(percentuali);
        });
    }

    private void aggiornaMappaSpiaggia(LocalDate dataRiferimento) {
        List<MappaDAO.MappaOmbrelloneInfo> infoCelle = mappaDao.getMappaSpiaggia(dataRiferimento);
        view.aggiornaGrigliaSpiaggia(infoCelle);
    }

    private void gestisciInterazioneCellaSpiaggia(int numeroCella) {
        LocalDate oggi = LocalDate.now();
        List<MappaDAO.MappaOmbrelloneInfo> mappaOggi = mappaDao.getMappaSpiaggia(oggi);
        
        MappaDAO.MappaOmbrelloneInfo cellaSelezionata = mappaOggi.stream()
                .filter(c -> c.getNumero() == numeroCella)
                .findFirst()
                .orElse(null);

        if (cellaSelezionata != null && cellaSelezionata.isOccupato()) {
            view.mostraDettagliPrenotazioneCella(
                cellaSelezionata.getCodZona(), 
                cellaSelezionata.getNomeZona(), 
                cellaSelezionata.getCodPrenotazione(), 
                cellaSelezionata.getClientePrenotato()
            );
        } else {
            view.apriFormNuovaPrenotazionePerCella(numeroCella);
        }
    }

    private void ricaricaDatiGlobali() {
        LocalDate oggi = LocalDate.now();
        aggiornaMappaSpiaggia(oggi);
        view.impostaTabellaNoleggi(noleggioAttrezzaturaDao.getStoricoNoleggi());
        view.impostaTabellaPrenotazioni(prenotazioneDao.getStoricoPrenotazioni());
        view.aggiornaListaPrenotazioniNonSaldate(prenotazioneDao.getPrenotazioniNonSaldate());
    }
}