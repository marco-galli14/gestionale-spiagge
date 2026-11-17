package controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import dao.ClienteDAO;
import dao.MappaDAO;
import dao.NoleggioAttrezzaturaDAO;
import javafx.stage.Stage;
import model.PrenotazioneCampo;
import view.MainView;

public class MainController {

    private final MainView view;
    private final MappaDAO mappaDao;
    private final ClienteDAO clienteDao;
    private final NoleggioAttrezzaturaDAO noleggioAttrezzaturaDao;

    private final StatisticheController contabilitaController;

    public MainController(Stage stage) {
        this.view = new MainView(stage);
        this.mappaDao = new MappaDAO();
        this.clienteDao = new ClienteDAO();
        this.noleggioAttrezzaturaDao = new NoleggioAttrezzaturaDAO();

        Runnable aggiornaMappaCorrente = () -> aggiornaDatiMappa(view.getDataSelezionata(), LocalTime.now());

        // Inizializza i sotto-controller collegandoli alle rispettive sotto-viste
        new PrenotazioneSpiaggiaController(view.getOmbrelloniView(), aggiornaMappaCorrente);
        new AttrezzatureCampiController(view.getAttrezzatureCampiView(), noleggioAttrezzaturaDao, aggiornaMappaCorrente);
        this.contabilitaController = new StatisticheController(view.getContabilitaStatisticheView(), noleggioAttrezzaturaDao);
        new AmministrazioneController(view.getAmministrazioneView(), clienteDao);

        inizializzaListenerPrincipali();
    }

    public void avvia() {
        view.mostraFinestra();
        aggiornaDatiMappa(LocalDate.now(), LocalTime.now());
    }

    private void inizializzaListenerPrincipali() {
        view.setOnCambioDataOraMappa(this::aggiornaDatiMappa);

        view.setOnCellaOmbrelloneClicked(numeroCella -> {
            LocalDate data = view.getDataSelezionata();
            MappaDAO.MappaOmbrelloneInfo info = mappaDao.getMappaSpiaggia(data).stream()
                    .filter(c -> c.getNumero() == numeroCella)
                    .findFirst()
                    .orElse(null);

            if (info != null && info.isOccupato()) {
                view.mostraDettagliPrenotazioneCella(info.getCodZona(), info.getNomeZona(), info.getCodPrenotazione(), info.getClientePrenotato());
            } else {
                view.getOmbrelloniView().apriFormNuovaPrenotazionePerCella(numeroCella);
            }
        });

        // Gestione condivisa dei clienti (usata da più sotto-viste)
        view.setOnRichiediListaClienti(clienteDao::getTuttiIClienti);
        view.setOnSalvaClienteAction((cf, nome, cognome, email, telefono, codHotel, idGruppo) -> {
            boolean successo = clienteDao.insertCliente(cf, nome, cognome, email, telefono, codHotel, idGruppo);
            view.mostraMessaggioEsito(successo, "Cliente registrato con successo!", "Errore durante il salvataggio del cliente.");
        });

        // Apertura Centro Contabilità dal pulsante laterale
        view.setOnVisualizzaContabilitaAction(() -> {
            view.getAttrezzatureCampiView().impostaTabellaNoleggi(noleggioAttrezzaturaDao.getStoricoNoleggi());
            contabilitaController.apriCentroContabilita();
        });
    }

    private void aggiornaDatiMappa(LocalDate data, LocalTime ora) {
        view.aggiornaGrigliaSpiaggia(mappaDao.getMappaSpiaggia(data));

        List<PrenotazioneCampo> campiOccupati = mappaDao.getOccupazioneCampi(data, ora);
        view.mostraCampiOccupati(campiOccupati);

        view.getAttrezzatureCampiView().impostaTabellaNoleggi(noleggioAttrezzaturaDao.getStoricoNoleggi());
    }
}