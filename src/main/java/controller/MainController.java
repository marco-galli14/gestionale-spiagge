package controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import common.Pair;
import common.StoricoPrenotazione;
import dao.AllestimentoDAO;
import dao.ClienteDAO;
import dao.DipendenteDAO;
import dao.MappaDAO;
import dao.NoleggioAttrezzaturaDAO;
import dao.OmbrelloneDAO;
import dao.PagamentoDAO;
import dao.PrenotazioneCampoDAO;
import dao.PrenotazioneDAO;
import dao.PrenotazioneGiornalieraDAO;
import dao.ZonaDAO;
import model.Cliente;
import model.Dipendente;
import model.Prenotazione;
import model.PrenotazioneCampo;
import view.MainView;

public final class MainController {

    private final MainView view;
    
    private final ClienteDAO clienteDao;
    private final PrenotazioneDAO prenotazioneDao;
    private final PrenotazioneGiornalieraDAO prenotazioneGiornalieraDao;
    private final MappaDAO mappaDao;
    private final NoleggioAttrezzaturaDAO noleggioAttrezzaturaDao;
    private final PagamentoDAO pagamentoDao;
    private final DipendenteDAO dipendenteDao;
    private final ZonaDAO zonaDao;
    private final PrenotazioneCampoDAO prenotazioneCampoDao;
    private final OmbrelloneDAO ombrelloneDao;
    private final AllestimentoDAO allestimentoDao;

    public MainController(MainView view) {
        this.view = view;
        
        this.clienteDao = new ClienteDAO();
        this.prenotazioneDao = new PrenotazioneDAO();
        this.prenotazioneGiornalieraDao = new PrenotazioneGiornalieraDAO();
        this.mappaDao = new MappaDAO();
        this.noleggioAttrezzaturaDao = new NoleggioAttrezzaturaDAO();
        this.pagamentoDao = new PagamentoDAO();
        this.dipendenteDao = new DipendenteDAO();
        this.zonaDao = new ZonaDAO();
        this.prenotazioneCampoDao = new PrenotazioneCampoDAO();
        this.ombrelloneDao = new OmbrelloneDAO();
        this.allestimentoDao = new AllestimentoDAO();

        inizializzaEventiUI();
        ricaricaDatiGlobali();
    }

    private void inizializzaEventiUI() {
        
        view.setOnRichiediListaClienti(() -> clienteDao.getTuttiIClienti());

        view.setOnCellaOmbrelloneClicked(this::gestisciInterazioneCellaSpiaggia);

        view.setOnCambioDataOraMappa(this::aggiornaMappaSpiaggiaCompleta);

        view.setOnSalvaClienteAction((cf, nome, cognome, email, telefono, codHotel) -> {
            boolean ok = clienteDao.insertCliente(cf, nome, cognome, email, telefono, codHotel);
            view.mostraMessaggioEsito(ok, "Cliente registrato con successo!", "Errore durante la registrazione del cliente.");
        });

        view.setOnAssegnaGruppoCliente((cf, idGruppo) -> {
            boolean ok = clienteDao.setGroup(cf, idGruppo);
            view.mostraMessaggioEsito(ok, "Gruppo assegnato con successo!", "Errore nell'assegnazione del gruppo.");
        });

        view.setOnCreaPrenotazioneAction((dataInizio, dataFine, codDipendente, cf, numeriOmbrelloni, qtaLettini, qtaSdraio, qtaSedie) -> {
            int nuovoCodice = prenotazioneDao.addPrenotazione(dataInizio, dataFine, codDipendente, cf);
            if (nuovoCodice > 0) {
                for (int numOmbrellone : numeriOmbrelloni) {
                    LocalDate corrente = dataInizio;
                    while (!corrente.isAfter(dataFine)) {
                        prenotazioneGiornalieraDao.addPrenotazioneGiornaliera(nuovoCodice, numOmbrellone, corrente);
                        
                        aggiornaOInserisciAllestimento("LETTINO", nuovoCodice, numOmbrellone, corrente, qtaLettini);
                        aggiornaOInserisciAllestimento("SDRAIO", nuovoCodice, numOmbrellone, corrente, qtaSdraio);
                        aggiornaOInserisciAllestimento("SEDIA", nuovoCodice, numOmbrellone, corrente, qtaSedie);

                        corrente = corrente.plusDays(1);
                    }
                }

                prenotazioneDao.updatePacchettoSconto(nuovoCodice);
                prenotazioneDao.updateCostoTotale(nuovoCodice);
                
                aggiornaMappaSpiaggiaCompleta(view.getDataSelezionata(), LocalTime.now());
                
                view.aggiornaTabellaPrenotazioni(prenotazioneDao.getStoricoPrenotazioni());
                view.mostraMessaggioEsito(true, "Prenotazione creata con successo! Codice: " + nuovoCodice, "");
            } else {
                view.mostraMessaggioEsito(false, "", "Errore nella creazione della prenotazione.");
            }
        });

        view.setOnModificaAllestimentoGiornaliero((codPrenotazione, numOmbrellone, data, qtaLettini, qtaSdraio, qtaSedie) -> {
            aggiornaOInserisciAllestimento("LETTINO", codPrenotazione, numOmbrellone, data, qtaLettini);
            aggiornaOInserisciAllestimento("SDRAIO", codPrenotazione, numOmbrellone, data, qtaSdraio);
            aggiornaOInserisciAllestimento("SEDIA", codPrenotazione, numOmbrellone, data, qtaSedie);

            boolean ok = prenotazioneDao.updateCostoTotale(codPrenotazione);
            view.mostraMessaggioEsito(ok, "Allestimento del giorno " + data + " aggiornato!", "Errore nell'aggiornamento dell'allestimento.");
        });

        view.setOnRichiediAllestimentiGiorno((codPren, numOmb, data) -> 
            allestimentoDao.getAllestimenti(codPren, numOmb, data)
        );

        view.setOnEliminaPrenotazioneSpiaggiaAction(codPren -> {
            boolean eliminata = prenotazioneDao.eliminaPrenotazione(codPren);
            if (eliminata) {
                aggiornaMappaSpiaggiaCompleta(view.getDataSelezionata(), LocalTime.now());
                view.aggiornaTabellaPrenotazioni(prenotazioneDao.getStoricoPrenotazioni());
                view.mostraMessaggioEsito(true, "Prenotazione " + codPren + " eliminata con successo!", "");
            } else {
                view.mostraMessaggioEsito(false, "", "Errore: Prenotazione non trovata.");
            }
        });

        view.setOnRichiediStoricoPrenotazioni(() -> {
            List<StoricoPrenotazione> storico = prenotazioneDao.getStoricoPrenotazioni();
            view.mostraStoricoPrenotazioni(storico);
        });

        view.setOnCreaNoleggioAction((data, oraInizio, durata, cf, codDipendente, codAttrezzatura) -> {
            int codGenerato = noleggioAttrezzaturaDao.inserisciNoleggioAttrezzatura(data, oraInizio, durata, cf, codDipendente, codAttrezzatura);
            
            if (codGenerato > 0) {
                noleggioAttrezzaturaDao.aggiornaCostoTotaleNoleggio(codGenerato);
                view.aggiornaTabellaNoleggi(noleggioAttrezzaturaDao.getStoricoNoleggi());
                view.mostraMessaggioEsito(true, "Noleggio inserito! Codice assegnato: " + codGenerato, "");
            } else {
                view.mostraMessaggioEsito(false, "", "Errore: Controlla che i dati siano corretti.");
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

        view.setOnCreaPrenotazioneCampoAction((dataPrenotazione, oraInizio, oraFine, codCampo, cf, codDip) -> {
            boolean ok = prenotazioneCampoDao.inserisciPrenotazioneCampo(dataPrenotazione, oraInizio, oraFine, codCampo, cf, codDip);
            view.mostraMessaggioEsito(ok, "Campo prenotato con successo!", "Errore nella prenotazione del campo.");
            ricaricaDatiGlobali();
        });

        view.setOnEliminaPrenotazioneCampoAction(codPren -> {
            boolean ok = prenotazioneCampoDao.eliminaPrenotazioneCampo(codPren);
            view.mostraMessaggioEsito(ok, "Prenotazione campo eliminata con successo!", "Errore nell'eliminazione della prenotazione.");
            ricaricaDatiGlobali();
        });

        view.setOnVisualizzaContabilitaAction(() -> {
            List<Prenotazione> nonSaldate = prenotazioneDao.getPrenotazioniNonSaldate();
            Pair<Integer, Integer> reportOggi = pagamentoDao.getDailyReport(LocalDate.now());
            view.mostraCentroContabilita(nonSaldate, reportOggi.key(), reportOggi.value());
        });

        view.setOnReportPerDataAction(data -> {
            Pair<Integer, Integer> report = pagamentoDao.getDailyReport(data);
            view.mostraReportIncassi(report.key(), report.value());
        });

        view.setOnRegistraPagamentoPrenotazione((codPag, importo, data, metodo, codPren) -> {
            boolean ok = pagamentoDao.insertPagamentoPrenotazione(codPag, importo, data, metodo, codPren);
            if (ok) {
                prenotazioneDao.updateStatoPagamento(codPren);
                view.mostraMessaggioEsito(true, "Pagamento registrato con successo!", "");
                
                List<Prenotazione> nonSaldate = prenotazioneDao.getPrenotazioniNonSaldate();
                Pair<Integer, Integer> reportOggi = pagamentoDao.getDailyReport(LocalDate.now());
                view.mostraCentroContabilita(nonSaldate, reportOggi.key(), reportOggi.value());
                
                ricaricaDatiGlobali();
            } else {
                view.mostraMessaggioEsito(false, "", "Errore durante la registrazione del pagamento.");
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

    private void aggiornaOInserisciAllestimento(String codSeduta, int codPrenotazione, int numOmbrellone, LocalDate data, int quantita) {
        if (quantita > 0) {
            boolean ok = allestimentoDao.updateAllestimento(quantita, codSeduta, codPrenotazione, numOmbrellone, data);
            if (!ok) {
                allestimentoDao.addAllestimento(codSeduta, codPrenotazione, numOmbrellone, data, quantita);
            }
        } else {
            allestimentoDao.deleteAllestimento(codSeduta, codPrenotazione, numOmbrellone, data);
        }
    }

    private void aggiornaMappaSpiaggiaCompleta(LocalDate dataRiferimento, LocalTime oraRiferimento) {
        List<MappaDAO.MappaOmbrelloneInfo> infoCelle = mappaDao.getMappaSpiaggia(dataRiferimento);
        view.aggiornaGrigliaSpiaggia(infoCelle);

        List<PrenotazioneCampo> campiOccupati = mappaDao.getOccupazioneCampi(dataRiferimento, oraRiferimento);
        view.mostraCampiOccupati(campiOccupati);
    }

    private void gestisciInterazioneCellaSpiaggia(int numeroCella) {
        LocalDate dataSelezionata = view.getDataSelezionata();
        if (dataSelezionata == null) {
            dataSelezionata = LocalDate.now();
        }
        
        List<MappaDAO.MappaOmbrelloneInfo> mappaData = mappaDao.getMappaSpiaggia(dataSelezionata);
        
        MappaDAO.MappaOmbrelloneInfo cellaSelezionata = mappaData.stream()
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
        LocalTime oraCorrente = LocalTime.now().withMinute(0);
        
        aggiornaMappaSpiaggiaCompleta(oggi, oraCorrente);

        view.impostaTabellaNoleggi(noleggioAttrezzaturaDao.getStoricoNoleggi());
        view.impostaTabellaPrenotazioni(prenotazioneDao.getStoricoPrenotazioni());
        view.aggiornaListaPrenotazioniNonSaldate(prenotazioneDao.getPrenotazioniNonSaldate());
    }
}