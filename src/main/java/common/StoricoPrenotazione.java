package common;

import java.time.LocalDate;

public record StoricoPrenotazione(String cf, String nome, String cognome, int codPrenotazione, LocalDate dataInizio, LocalDate dataFine, int PrezzoTotale) {

}
