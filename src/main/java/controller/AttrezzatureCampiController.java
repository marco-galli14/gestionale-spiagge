package controller;

import dao.NoleggioAttrezzaturaDAO;
import dao.PrenotazioneCampoDAO;
import view.AttrezzatureCampiView;

public class AttrezzatureCampiController {

    private final AttrezzatureCampiView view;
    private final NoleggioAttrezzaturaDAO noleggioDao;
    private final PrenotazioneCampoDAO campoDao;
    private final Runnable onAggiornaMappa;

    public AttrezzatureCampiController(AttrezzatureCampiView view, NoleggioAttrezzaturaDAO noleggioDao, Runnable onAggiornaMappa) {
        this.view = view;
        this.noleggioDao = noleggioDao;
        this.campoDao = new PrenotazioneCampoDAO();
        this.onAggiornaMappa = onAggiornaMappa;

        inizializzaListener();
    }

    private void inizializzaListener() {
        view.setOnCreaNoleggioAction((data, oraInizio, durata, cf, codDip, codAttrezzatura) -> {
            int codNol = noleggioDao.inserisciNoleggioAttrezzatura(data, oraInizio, durata, cf, codDip, codAttrezzatura);
            if (codNol != -1) {
                noleggioDao.aggiornaCostoTotaleNoleggio(codNol);
                view.mostraMessaggioEsito(true, "Noleggio salvato con successo!", "");
                onAggiornaMappa.run();
            } else {
                view.mostraMessaggioEsito(false, "", "Errore durante il salvataggio del noleggio.");
            }
        });

        view.setOnEliminaNoleggioAction(codNoleggio -> {
            boolean ok = noleggioDao.eliminaNoleggioAttrezzatura(codNoleggio);
            view.mostraMessaggioEsito(ok, "Noleggio eliminato con successo.", "Errore durante l'eliminazione del noleggio.");
            onAggiornaMappa.run();
        });

        view.setOnCreaPrenotazioneCampoAction((data, oraInizio, oraFine, codCampo, cf, codDip) -> {
            boolean ok = campoDao.inserisciPrenotazioneCampo(data, oraInizio, oraFine, codCampo, cf, codDip);
            view.mostraMessaggioEsito(ok, "Prenotazione campo salvata con successo!", "Errore durante il salvataggio della prenotazione campo.");
            onAggiornaMappa.run();
        });

        view.setOnEliminaPrenotazioneCampoAction(codCampi -> {
            boolean ok = campoDao.eliminaPrenotazioneCampo(codCampi);
            view.mostraMessaggioEsito(ok, "Prenotazione campo eliminata.", "Errore durante l'eliminazione.");
            onAggiornaMappa.run();
        });
    }
}