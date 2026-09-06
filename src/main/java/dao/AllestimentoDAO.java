package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.Allestimento;

public class AllestimentoDAO {

    public boolean addAllestimento(String codSeduta, String codPrenotazione, int numero,
                                    LocalDate dataRiferimento, int quantita) {
        String sql = "INSERT INTO allestimento (CodSeduta, CodPrenotazione, Numero, DataRiferimento, Quantita) " +
                        "VALUES (?, ?, ?, ?, ?);";


        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, codSeduta);
            stmt.setString(2, codPrenotazione);
            stmt.setInt(3, numero);
            stmt.setDate(4, Date.valueOf(dataRiferimento));
            stmt.setInt(5, quantita);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateAllestimento(int quantita, String codSeduta, String codPrenotazione, int numero,
                                    LocalDate dataRiferimento) {
        String sql = "UPDATE allestimento " +
                        "SET Quantita = ? " +
                        "WHERE CodSeduta = ? " +
                        "AND CodPrenotazione = ? " +
                        "AND Numero = ? " +
                        "AND DataRiferimento = ?;";


        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, quantita);
            stmt.setString(2, codSeduta);
            stmt.setString(3, codPrenotazione);
            stmt.setInt(4, numero);
            stmt.setDate(5, Date.valueOf(dataRiferimento));
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAllestimento(String codSeduta, String codPrenotazione, int numero, LocalDate dataRiferimento) {
        String sql = "DELETE FROM allestimento \n" +
                        "WHERE CodSeduta = ? \n" +
                        "AND CodPrenotazione = ? \n" +
                        "AND Numero = ? \n" +
                        "AND DataRiferimento = ?;";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, codSeduta);
            stmt.setString(2, codPrenotazione);
            stmt.setInt(3, numero);
            stmt.setDate(4, Date.valueOf(dataRiferimento));
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Ritorna una lista con tutti gli allestimenti di una certa Prenotazione Giornaliera.
     * 
     * @param codPrenotazione
     * @param numero
     * @param dataRiferimento
     * @return
     */
    public List<Allestimento> getAllestimenti(String codPrenotazione, int numero, LocalDate dataRiferimento) {
        List<Allestimento> allestimenti = new ArrayList<>();

        String sql = "SELECT * " +
                        "FROM allestimento " +
                        "WHERE CodPrenotazione = ? " +
                        "AND Numero = ? " +
                        "AND DataRiferimento = ?;";
        
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, codPrenotazione);
            pstmt.setInt(2, numero);
            pstmt.setDate(3, Date.valueOf(dataRiferimento));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String codSeduta = rs.getString("CodSeduta");
                    String codP = rs.getString("CodPrenotazione");
                    int n = rs.getInt("Numero");
                    LocalDate dRif = rs.getDate("DataRiferimento").toLocalDate();
                    int q = rs.getInt("Quantita");

                    Allestimento al = new Allestimento(codSeduta, codP, n, dRif, q);
                    allestimenti.add(al);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allestimenti;
    }
}
