package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TariffaZonaDao {

    public boolean inserisciTariffaZona(String codStagione, String codZona, int tariffaGiornaliera) {
        String query = "INSERT INTO TARIFFA_ZONA (CodStagione, CodZona, TariffaGiornaliera) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, codStagione);
            pstmt.setString(2, codZona);
            pstmt.setInt(3, tariffaGiornaliera);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
