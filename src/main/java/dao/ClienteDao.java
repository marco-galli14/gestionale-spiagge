package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ClienteDAO {

    public boolean insertCliente(String cf, String nomeCliente, String cognomeCliente, String email, int telefono, Integer codHotel) {
        
        String query = "INSERT INTO CLIENTE (CF, Nome, Cognome, Email, Telefono, CodHotel, ID_gruppo)" +
                        "VALUES (?, ?, ?,?, ?, ?, NULL)";

        try (Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(query)) {

                ps.setString(1, cf);
                ps.setString(2, nomeCliente);
                ps.setString(3, cognomeCliente);
                ps.setString(4, email);
                ps.setInt(5, telefono);
                ps.setObject(6, codHotel);

                int rowsAffected = ps.executeUpdate();
                return rowsAffected > 0; // Return true if the insert was successful

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if there was an error
        }
    }

    public boolean setGroup(String cf, Integer idGruppo) {

        String query = "UPDATE Cliente " + 
                        "SET ID_Gruppo = ? " +
                        "WHERE CF = ?";
        
        try (Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(query)) {

                ps.setObject(1, idGruppo);
                ps.setString(2, cf);

                int rowsAffected = ps.executeUpdate();
                return rowsAffected > 0; // Return true if the update was successful

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if there was an error
        }
    }
}
