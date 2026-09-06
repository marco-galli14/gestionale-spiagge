package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GruppoDao {

    public boolean insertGruppo(int idGruppo, int scontoGruppo) {

        String query = "INSERT INTO GRUPPO (ID_gruppo, ScontoGruppo)" +
                        "VALUES (?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps =con.prepareStatement(query)) {

                ps.setInt(1, idGruppo);
                ps.setInt(2, scontoGruppo);

                int rowsAffected = ps.executeUpdate();
                return rowsAffected > 0; // Return true if the insert was successful

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if there was an error
        }
    }
}
