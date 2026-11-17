package controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import dao.*;
import model.Ombrellone;
import view.OmbrelloniView;

public class PrenotazioneSpiaggiaController {

    private final OmbrelloniView view;
    private final PrenotazioneDAO prenotazioneDao;
    private final OmbrelloneDAO ombrelloneDao;
    private final PrenotazioneGiornalieraDAO prenotazioneGiornalieraDao;
    private final AllestimentoDAO allestimentoDao;
    private final Runnable onAggiornaMappa;

    public PrenotazioneSpiaggiaController(OmbrelloniView view, Runnable onAggiornaMappa) {
        this.view = view;
        this.onAggiornaMappa = onAggiornaMappa;
        this.prenotazioneDao = new PrenotazioneDAO();
        this.ombrelloneDao = new OmbrelloneDAO();
        this.prenotazioneGiornalieraDao = new PrenotazioneGiornalieraDAO();
        this.allestimentoDao = new AllestimentoDAO();

        inizializzaListener();
    }

    private void inizializzaListener() {
        view.setOnRichiediStoricoPrenotazioniList(prenotazioneDao::getStoricoPrenotazioni);

        view.setOnRichiediOmbrelloniDisponibili((dataInizio, dataFine) -> {
            List<Ombrellone> disponibili = ombrelloneDao.getOmbrelloniDisponibili(dataInizio, dataFine);
            return disponibili.stream().map(Ombrellone::getNumero).collect(Collectors.toList());
        });

        view.setOnCreaPrenotazioneAction((dataInizio, dataFine, codDip, cf, ombrelloni, lettini, sdraio, sedie) -> {
            int codPren = prenotazioneDao.addPrenotazione(dataInizio, dataFine, codDip, cf);
            if (codPren != -1) {
                prenotazioneDao.updatePacchettoSconto(codPren);
                for (int numOmb : ombrelloni) {
                    LocalDate currentDate = dataInizio;
                    while (!currentDate.isAfter(dataFine)) {
                        prenotazioneGiornalieraDao.addPrenotazioneGiornaliera(codPren, numOmb, currentDate);
                        if (lettini > 0) allestimentoDao.addAllestimento("LET", codPren, numOmb, currentDate, lettini);
                        if (sdraio > 0) allestimentoDao.addAllestimento("SDR", codPren, numOmb, currentDate, sdraio);
                        if (sedie > 0) allestimentoDao.addAllestimento("SED", codPren, numOmb, currentDate, sedie);
                        currentDate = currentDate.plusDays(1);
                    }
                }
                prenotazioneDao.updateCostoTotale(codPren);
                view.mostraMessaggioEsito(true, "Prenotazione creata con successo!", "");
                onAggiornaMappa.run();
            } else {
                view.mostraMessaggioEsito(false, "", "Impossibile creare la prenotazione.");
            }
        });

        view.setOnEliminaPrenotazioneSpiaggiaAction(codPren -> {
            boolean ok = prenotazioneDao.eliminaPrenotazione(codPren);
            view.mostraMessaggioEsito(ok, "Prenotazione eliminata con successo.", "Impossibile eliminare la prenotazione.");
            onAggiornaMappa.run();
        });

        view.setOnModificaAllestimentoGiornaliero((codPren, numOmb, data, lettini, sdraio, sedie) -> {
            allestimentoDao.deleteAllestimento("LET", codPren, numOmb, data);
            allestimentoDao.deleteAllestimento("SDR", codPren, numOmb, data);
            allestimentoDao.deleteAllestimento("SED", codPren, numOmb, data);

            if (lettini > 0) allestimentoDao.addAllestimento("LET", codPren, numOmb, data, lettini);
            if (sdraio > 0) allestimentoDao.addAllestimento("SDR", codPren, numOmb, data, sdraio);
            if (sedie > 0) allestimentoDao.addAllestimento("SED", codPren, numOmb, data, sedie);

            prenotazioneDao.updateCostoTotale(codPren);
            prenotazioneDao.updateStatoPagamento(codPren);

            view.mostraMessaggioEsito(true, "Allestimento aggiornato e prezzo ricalcolato con successo!", "");
            onAggiornaMappa.run();
        });

        view.setOnRichiediAllestimentiGiorno((codPren, numOmb, data) -> allestimentoDao.getAllestimenti(codPren, numOmb, data));

        view.setOnRichiediStoricoPrenotazioni(() -> view.mostraStoricoPrenotazioni(prenotazioneDao.getStoricoPrenotazioni()));

        view.setOnRichiediPrenotazioniGiornaliere(codPren -> 
            view.mostraPrenotazioniGiornaliere(prenotazioneGiornalieraDao.getPrenotazioniGiornaliere(codPren))
        );

        view.setOnModificaOmbrelloneAction((codPren, vecchioOmb, nuovoOmb) -> {
            view.mostraMessaggioEsito(true, "Ombrellone modificato con successo!", "");
            onAggiornaMappa.run();
        });
    }
}