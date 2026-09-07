package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Cliente; // Verifica che il package della tua classe Cliente sia corretto

public class ClienteDAO {

    public boolean insertCliente(String cf, String nomeCliente, String cognomeCliente, String email, String telefono, Integer codHotel) {
        
        String query = "INSERT INTO CLIENTE (CF, Nome, Cognome, Email, Telefono, CodHotel, ID_gruppo)" +
                        "VALUES (?, ?, ?,?, ?, ?, NULL)";

        try (Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(query)) {

                ps.setString(1, cf);
                ps.setString(2, nomeCliente);
                ps.setString(3, cognomeCliente);
                ps.setString(4, email);
                ps.setString(5, telefono);
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

    /**
     * Recupera la lista di tutti i clienti presente nel database.
     */
    public List<Cliente> getTuttiIClienti() {
        List<Cliente> clienti = new ArrayList<>();
        String query = "SELECT CF, Nome, Cognome, Email, Telefono, CodHotel FROM CLIENTE ORDER BY Cognome, Nome";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String cf = rs.getString("CF");
                String nome = rs.getString("Nome");
                String cognome = rs.getString("Cognome");
                String email = rs.getString("Email");
                String telefono = rs.getString("Telefono");
                
                // Gestione dei valori NULL per tipi oggetto come Integer
                Integer codHotel = rs.getObject("CodHotel") != null ? rs.getInt("CodHotel") : null;

                // Crea l'oggetto Cliente (assicurati di avere un costruttore corrispondente nel Model)
                Cliente c = new Cliente(cf, nome, cognome, email, telefono, codHotel, null); // Passa null per ID_gruppo se non è presente nel ResultSet
                clienti.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clienti;
    }
}