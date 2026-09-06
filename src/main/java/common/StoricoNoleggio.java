package common;

import java.time.LocalDate;
import java.time.LocalTime;

public record StoricoNoleggio(String cf, String nome, String cognome, int codNoleggio, LocalDate dataNoleggio, LocalTime oraInizio, int durataOre, int costoTotale) {

}
