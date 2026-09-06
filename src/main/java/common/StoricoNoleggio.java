package common;

import java.time.LocalDate;
import java.time.LocalTime;

public record StoricoNoleggio(String cf, String nome, String cognome, int codNoleggio, LocalDate dataNoleggio, String codAttrezzatura, LocalTime oraInizio, int durataOre, int costoTotale) {

}
