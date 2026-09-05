package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.Ombrellone;

public class OmbrelloneDao {

    public List<Ombrellone> getOmbrelloniDisponibili(LocalDate dataInizio, LocalDate dataFine) {
        List<Ombrellone> ombrelloniDisponibili = new ArrayList<>();

        String query = "SELECT o.Numero, o.CodZona, z.NomeZona " +
                       "FROM ombrellone o, zona z " +
                       "WHERE z.Codzona = o.Codzona " +
                       "AND o.Numero NOT IN (" +
                       "    SELECT pg.Numero " +
                       "    FROM prenotazione_giornaliera pg " +
                       "    WHERE pg.DataRiferimento BETWEEN ? AND ?" +
                       ") " +
                       "ORDER BY o.Numero";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDate(1, java.sql.Date.valueOf(dataInizio));
            pstmt.setDate(2, java.sql.Date.valueOf(dataFine));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int numero = rs.getInt("Numero");
                    int codZona = rs.getInt("CodZona"); // Corrisponde al tipo int della tua classe Ombrellone

                    // Creiamo l'oggetto Ombrellone usando il costruttore con parametri
                    Ombrellone ombrellone = new Ombrellone(numero, codZona);
                    ombrelloniDisponibili.add(ombrellone);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ombrelloniDisponibili;
    }
}