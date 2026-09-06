
import model.Ombrellone;
import dao.AllestimentoDAO;
import dao.OmbrelloneDao;
import dao.PrenotazioneDAO;
import dao.PrenotazioneGiornalieraDAO;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Stream;

public class MainTest {
    public static void main(String[] args) {
        /* TEST VISUALIZZAZIONE OMBRELLONI DISPONIBILI IN UN CERTO INTERVALLO:
        OmbrelloneDao dao = new OmbrelloneDao();

        // Inserisci un intervallo di date di prova (assicurati che nel DB ci siano prenotazioni o dati per queste date)
        LocalDate dataInizio = LocalDate.of(2025, 7, 1);
        LocalDate dataFine = LocalDate.of(2027, 7, 7);

        List<Ombrellone> liberi = dao.getOmbrelloniDisponibili(dataInizio, dataFine);

        System.out.println("=== OMBRELLONI DISPONIBILI ===");
        if (liberi.isEmpty()) {
            System.out.println("Nessun ombrellone libero trovato in questo periodo.");
        } else {
            for (Ombrellone o : liberi) {
                System.out.println("Ombrellone numero: " + o.getNumero() + " | Codice Zona: " + o.getCodZona());
            }
        }
        */

        /*TEST AGGIUNTA NUOVA PRENOTAZIONE: 
        PrenotazioneDAO dao1 = new PrenotazioneDAO();

        String codPrenotazione = "P4";
        LocalDate dataInizio = LocalDate.of(2026, 7, 1);
        LocalDate dataFine = LocalDate.of(2026, 7, 7);
        int codDipendente = 1;
        String cf = "ACEVNT50M20E300D";

        System.out.println(dao1.addPrenotazione(codPrenotazione, dataInizio, dataFine, codDipendente, cf));
        */

        /* TEST AGGIUNTA NUOVA PRENOTAZIONE GIORNALIERA :
        PrenotazioneGiornalieraDAO dao = new PrenotazioneGiornalieraDAO();
        int numero = 3;
        
        dataInizio.datesUntil(dataFine.plusDays(1))
                    .forEach(data -> dao.addPrenotazioneGiornaliera(codPrenotazione, numero, data));
        */

        /*TEST AGGIUNTA ALLESTIMENTO: 
        PrenotazioneGiornalieraDAO dao = new PrenotazioneGiornalieraDAO();
        AllestimentoDAO dao2 = new AllestimentoDAO();
        String codPrenotazione = "P4";
        String codSeduta = "LET";
        int quantita = 2;

        for (var p : dao.getPrenotazioniGiornaliere(codPrenotazione)) {
            dao2.addAllestimento(codSeduta, codPrenotazione, p.getNumero(), p.getDataRiferimento(), quantita);
        }
        */

        /*TEST AGGIORNAMENTO PACCHETTO: 
        PrenotazioneDAO dao1 = new PrenotazioneDAO();

        String codPrenotazione = "P4";
        System.out.println(dao1.updatePacchettoSconto(codPrenotazione));
        */

        /*TEST MODIFICARE OMBRELLONE DA PG: 
        PrenotazioneGiornalieraDAO dao = new PrenotazioneGiornalieraDAO();
        OmbrelloneDao ombDAO = new OmbrelloneDao();
        String codPrenotazione = "P4";
        LocalDate dataRiferimento = LocalDate.of(2026, 7, 7);
        int numero = 5;
        int zona = 1; //sarebbe da fare un altra query per trovare uno specifico ombrellone

        if (ombDAO.getOmbrelloniDisponibili(dataRiferimento, dataRiferimento).contains(new Ombrellone(numero, zona))) {
            System.out.println(dao.updateOmbrellone(numero,codPrenotazione,dataRiferimento));
        }
        */

        /*TEST MODIFICA QUANTITA ALLESTIMENTO: 
        AllestimentoDAO dao2 = new AllestimentoDAO();
        String codPrenotazione = "P4";
        int numero = 5;
        LocalDate dataRiferimento = LocalDate.of(2026, 7, 7);
        String codSeduta = "LET";
        int quantita = 6;

        dao2.getAllestimenti(codPrenotazione, numero, dataRiferimento).stream()
            .forEach(all -> System.out.println(dao2.updateAllestimento(quantita, codSeduta, all.getCodPrenotazione(),
                                                                    all.getNumero(), all.getDataRiferimento())));
        */

        /*TEST ANNULLARE UNA PRENOTAZIONE: 
        PrenotazioneDAO dao1 = new PrenotazioneDAO();

        String codPrenotazione = "P4";
        System.out.println(dao1.deletePrenotazione(codPrenotazione));
        */

        /*TEST ELIMINARE UNA PG: 
        PrenotazioneGiornalieraDAO dao = new PrenotazioneGiornalieraDAO();
        String codPrenotazione = "P4";
        int numero = 3;
        LocalDate dataRiferimento = LocalDate.of(2026, 7, 7);

        System.out.println(dao.deletePrenotazioneGiornaliera(codPrenotazione, numero, dataRiferimento));
        */

        /*TEST ELIMINARE UN ALLESTIMENTO: 
        AllestimentoDAO dao2 = new AllestimentoDAO();
        String codPrenotazione = "P4";
        int numero = 3;
        LocalDate dataRiferimento = LocalDate.of(2026, 7, 7);
        String codSeduta = "LET";

        System.out.println(dao2.deleteAllestimento(codSeduta, codPrenotazione, numero, dataRiferimento));
        */

        /*TEST CALCOLO PREZZO TOTALE DI UNA PRENOTAZIONE: 

        PrenotazioneDAO dao1 = new PrenotazioneDAO();

        String codPrenotazione = "P5";
        System.out.println(dao1.updateCostoTotale(codPrenotazione));*/

        /*TEST AGGIORNAMENTO STATO PAGAMENTO PRENOTAZIONE: 
        PrenotazioneDAO dao1 = new PrenotazioneDAO();

        String codPrenotazione = "P5";
        System.out.println(dao1.updateStatoPagamento(codPrenotazione));*/

        /*TEST ELENCO PRENOTAZIONI ANCORA NON SALDATE: 

        PrenotazioneDAO dao1 = new PrenotazioneDAO();

        for (var p : dao1.getPrenotazioniNonSaldate()) {
            System.out.print(p + "\n");
        }*/

    }
} 
