package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class StagioneDAO {

    public boolean inserisciStagione(String codStagione, LocalDate dataInizio, LocalDate dataFine) {
        String query = "INSERT INTO STAGIONE (CodStagione, DataInizio, DataFine) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, codStagione);
            pstmt.setDate(2, java.sql.Date.valueOf(dataInizio));
            pstmt.setDate(3, java.sql.Date.valueOf(dataFine));

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}