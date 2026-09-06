package dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ZonaDAO {

    public static class ZonaOccupazioneInfo {
        private final int codZona; // Aggiornato a int
        private final int ombrelloniOccupati;
        private final int capienzaMassima;
        private final double percentualeOccupazione;

        public ZonaOccupazioneInfo(int codZona, int ombrelloniOccupati, int capienzaMassima, double percentualeOccupazione) {
            this.codZona = codZona;
            this.ombrelloniOccupati = ombrelloniOccupati;
            this.capienzaMassima = capienzaMassima;
            this.percentualeOccupazione = percentualeOccupazione;
        }

        public int getCodZona() { return codZona; }
        public int getOmbrelloniOccupati() { return ombrelloniOccupati; }
        public int getCapienzaMassima() { return capienzaMassima; }
        public double getPercentualeOccupazione() { return percentualeOccupazione; }
    }

    public List<ZonaOccupazioneInfo> getPercentualiOccupazioneZone(LocalDate dataInizio, LocalDate dataFine) {
        List<ZonaOccupazioneInfo> risultati = new ArrayList<>();
        String query = "SELECT " +
                       "    z.CodZona, " +
                       "    (SELECT COUNT(pg.Numero) FROM prenotazione_giornaliera pg, ombrellone o WHERE pg.Numero = o.Numero AND o.CodZona = z.CodZona AND pg.DataRiferimento BETWEEN ? AND ?) AS OmbrelloniOccupati, " +
                       "    (SELECT COUNT(*) FROM ombrellone o_tot WHERE o_tot.CodZona = z.CodZona) * (DATEDIFF(?, ?) + 1) AS CapienzaMassimaMese, " +
                       "    ROUND(((SELECT COUNT(pg.Numero) FROM prenotazione_giornaliera pg, ombrellone o WHERE pg.Numero = o.Numero AND o.CodZona = z.CodZona AND pg.DataRiferimento BETWEEN ? AND ?) * 100.0) / " +
                       "    ((SELECT COUNT(*) FROM ombrellone o_tot WHERE o_tot.CodZona = z.CodZona) * (DATEDIFF(?, ?) + 1)), 2) AS PercentualeOccupazione " +
                       "FROM zona z";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            Date sqlInizio = Date.valueOf(dataInizio);
            Date sqlFine = Date.valueOf(dataFine);

            pstmt.setDate(1, sqlInizio);
            pstmt.setDate(2, sqlFine);
            pstmt.setDate(3, sqlFine);
            pstmt.setDate(4, sqlInizio);
            pstmt.setDate(5, sqlInizio);
            pstmt.setDate(6, sqlFine);
            pstmt.setDate(7, sqlFine);
            pstmt.setDate(8, sqlInizio);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    risultati.add(new ZonaOccupazioneInfo(
                        rs.getInt("CodZona"), // Aggiornato a getInt
                        rs.getInt("OmbrelloniOccupati"),
                        rs.getInt("CapienzaMassimaMese"),
                        rs.getDouble("PercentualeOccupazione")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return risultati;
    }
}