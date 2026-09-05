
import model.Ombrellone;
import dao.OmbrelloneDao;
import dao.PrenotazioneDAO;

import java.time.LocalDate;
import java.util.List;

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
        PrenotazioneDAO dao = new PrenotazioneDAO();

        String codPrenotazione = "P4";
        LocalDate dataInizio = LocalDate.of(2026, 7, 1);
        LocalDate dataFine = LocalDate.of(2026, 7, 7);
        int codDipendente = 1;
        String cf = "ACEVNT50M20E300D";

        System.out.println(dao.addPrenotazione(codPrenotazione, dataInizio, dataFine, codDipendente, cf));
        */
    }
} 
