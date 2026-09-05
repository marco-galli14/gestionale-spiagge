package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalTime;

public class PrenotazioneCampoDao {

    public boolean inserisciPrenotazioneCampo(String codPrenotazione, LocalTime oraInizio, LocalTime oraFine, String codCampo, String cf, String codDipendente) {
        String query = "INSERT INTO prenotazione_campo (CodPrenotazione, DataPrenotazione, OraInizio, OraFine, CodCampo, CF, CodDipendente) " +
                       "VALUES (?, CURRENT_DATE, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, codPrenotazione);
            pstmt.setTime(2, java.sql.Time.valueOf(oraInizio));
            pstmt.setTime(3, java.sql.Time.valueOf(oraFine));
            pstmt.setString(4, codCampo);
            pstmt.setString(5, cf);
            pstmt.setString(6, codDipendente);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminaPrenotazioneCampo(String codPrenotazione) {
        String query = "DELETE FROM prenotazione_campo WHERE CodPrenotazione = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, codPrenotazione);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}