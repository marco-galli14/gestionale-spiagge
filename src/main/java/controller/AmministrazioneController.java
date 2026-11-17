package controller;

import dao.*;
import view.AmministrazioneView;

public class AmministrazioneController {

    private final AmministrazioneView view;
    private final ClienteDAO clienteDao;
    private final GruppoDAO gruppoDao;
    private final HotelDAO hotelDao;
    private final DipendenteDAO dipendenteDao;
    private final StagioneDAO stagioneDao;
    private final TariffaZonaDAO tariffaZonaDao;
    private final TariffaNoleggioDAO tariffaNoleggioDao;

    public AmministrazioneController(AmministrazioneView view, ClienteDAO clienteDao) {
        this.view = view;
        this.clienteDao = clienteDao;
        this.gruppoDao = new GruppoDAO();
        this.hotelDao = new HotelDAO();
        this.dipendenteDao = new DipendenteDAO();
        this.stagioneDao = new StagioneDAO();
        this.tariffaZonaDao = new TariffaZonaDAO();
        this.tariffaNoleggioDao = new TariffaNoleggioDAO();

        inizializzaListener();
    }

    private void inizializzaListener() {
        view.setOnCreaGruppoCompletoAction((idGruppo, sconto) -> {
            boolean ok = gruppoDao.insertGruppo(idGruppo, sconto);
            view.mostraMessaggioEsito(ok, "Gruppo creato con successo!", "Errore durante la creazione del gruppo.");
        });

        view.setOnAssegnaGruppoClienteAction((cf, idGruppo) -> {
            boolean ok = clienteDao.setGroup(cf, idGruppo);
            view.mostraMessaggioEsito(ok, "Gruppo assegnato al cliente con successo!", "Errore durante l'assegnazione del gruppo.");
        });

        view.setOnSalvaHotelAction((codHotel, nomeHotel, scontoHotel) -> {
            boolean ok = hotelDao.insertHotel(codHotel, nomeHotel, scontoHotel);
            view.mostraMessaggioEsito(ok, "Hotel convenzionato aggiunto con successo!", "Errore durante il salvataggio dell'hotel.");
        });

        view.setOnSalvaDipendenteCompletoAction((codDip, nome, cognome, user, pwd, ruolo) -> {
            boolean ok = dipendenteDao.insertDipendente(codDip, nome, cognome, user, pwd, ruolo);
            view.mostraMessaggioEsito(ok, "Dipendente registrato con successo!", "Errore durante la registrazione del dipendente.");
        });

        view.setOnSalvaStagioneAction((codStagione, inizio, fine) -> {
            boolean ok = stagioneDao.inserisciStagione(codStagione, inizio, fine);
            view.mostraMessaggioEsito(ok, "Stagione registrata con successo!", "Errore durante la registrazione della stagione.");
        });

        view.setOnSalvaTariffaZonaListener((codStagione, codZona, tariffa) -> {
            boolean ok = tariffaZonaDao.inserisciTariffaZona(codStagione, codZona, tariffa);
            view.mostraMessaggioEsito(ok, "Tariffa zona salvata con successo!", "Errore durante il salvataggio della tariffa zona.");
        });

        view.setOnSalvaTariffaNoleggioListener((codStagione, codAttrezzatura, tariffaOraria) -> {
            boolean ok = tariffaNoleggioDao.inserisciTariffaNoleggio(codStagione, codAttrezzatura, tariffaOraria);
            view.mostraMessaggioEsito(ok, "Tariffa noleggio salvata con successo!", "Errore durante il salvataggio della tariffa noleggio.");
        });
    }
}