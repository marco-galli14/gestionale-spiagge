package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class PrenotazioneCampoDAO {

    public boolean inserisciPrenotazioneCampo(LocalDate dataPrenotazione, LocalTime oraInizio, LocalTime oraFine, String codCampo, String cf, String codDipendente) {
        String query = "INSERT INTO prenotazione_campo (DataPrenotazione, OraInizio, OraFine, CodCampo, CF, CodDipendente) " +
                       "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDate(1, java.sql.Date.valueOf(dataPrenotazione));
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

    public boolean eliminaPrenotazioneCampo(int codPrenotazione) {
        String query = "DELETE FROM prenotazione_campo WHERE CodPrenotazioneCampo = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, codPrenotazione);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}