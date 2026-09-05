package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TariffaNoleggioDao {

    public boolean inserisciTariffaNoleggio(String codStagione, String codAttrezzatura, double tariffaOraria) {
        String query = "INSERT INTO tariffa_noleggio (CodStagione, CodAttrezzatura, TariffaOraria) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, codStagione);
            pstmt.setString(2, codAttrezzatura);
            pstmt.setDouble(3, tariffaOraria);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}