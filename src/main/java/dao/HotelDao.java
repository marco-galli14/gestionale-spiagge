package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HotelDao {

    public boolean insertHotel(int codHotel, String nomeHotel, int scontoHotel) {
        
        String query = "INSERT INTO HOTEL (CodHotel, NomeHotel, ScontoHotel)" + 
                        "VALUES (?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, codHotel);
            ps.setString(2, nomeHotel);
            ps.setFloat(3, scontoHotel);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Return true if the insert was successful

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if there was an error
        }
    }
}
