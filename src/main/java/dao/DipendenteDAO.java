package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import common.Pair;
import model.Dipendente;

public class DipendenteDAO {

    public boolean insertDipendente(int codDipendente, String nome, String cognome, String username, String pwd, String ruolo) {

        String query = "INSERT INTO DIPENDENTE (CodDipendente, Nome, Cognome, Username, Password, Ruolo) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

                ps.setInt(1, codDipendente);
                ps.setString(2, nome);
                ps.setString(3, cognome);
                ps.setString(4, username);
                ps.setString(5, pwd);
                ps.setString(6, ruolo);

                int rowsAffected = ps.executeUpdate();
                return rowsAffected > 0;
                
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Pair<Dipendente, Integer> getDipendenteWithMostPrenotazioni() {

        String query = "SELECT d.CodDipendente, d.Nome, d.Cognome, COUNT(p.CodPrenotazione) as NumeroPrenotazioni " +
                        "FROM dipendente d, prenotazione p " +
                        "WHERE d.CodDipendente = p.CodDipendente " +
                        "GROUP BY d.CodDipendente, d.Nome, d.Cognome " +
                        "ORDER BY NumeroPrenotazioni DESC " +
                        "LIMIT 1";

        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery()) {
    

                    if (rs.next()) {
                        int codDipendente = rs.getInt("CodDipendente");
                        String nome = rs.getString("Nome");
                        String cognome = rs.getString("Cognome");
                        int numeroPrenotazioni = rs.getInt("NumeroPrenotazioni");
                        // Assuming you have a constructor that takes these parameters
                        return new Pair<>(new Dipendente(codDipendente, nome, cognome, null, null, null), numeroPrenotazioni);
                    } else {
                        return null; // No dipendente found
                    }
    
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
    }

    public Pair<Dipendente, Integer> getDipendenteWithMostNoleggi() {

        String query = "SELECT d.CodDipendente, d.Nome, d.Cognome, COUNT(n.CodNoleggio) as NumeroNoleggi " +
                        "FROM dipendente d, noleggio n " +
                        "WHERE d.CodDipendente = n.CodDipendente " +
                        "GROUP BY d.CodDipendente, d.Nome, d.Cognome " +
                        "ORDER BY NumeroNoleggi DESC " +
                        "LIMIT 1";

        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery()) {
    

                    if (rs.next()) {
                        int codDipendente = rs.getInt("CodDipendente");
                        String nome = rs.getString("Nome");
                        String cognome = rs.getString("Cognome");
                        int numeroNoleggi = rs.getInt("NumeroNoleggi");
                        // Assuming you have a constructor that takes these parameters
                        return new Pair<>(new Dipendente(codDipendente, nome, cognome, null, null, null), numeroNoleggi);
                    } else {
                        return null; // No dipendente found
                    }
    
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
    }

}
