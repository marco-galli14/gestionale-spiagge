package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.PrenotazioneGiornaliera;

public class PrenotazioneGiornalieraDAO {

    public boolean addPrenotazioneGiornaliera(int codPrenotazione, int numero, LocalDate dataRiferimento) {
        String sql = "INSERT INTO prenotazione_giornaliera (CodPrenotazione, Numero, DataRiferimento) " +
                        "VALUES (?, ?, ?);";


        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, codPrenotazione);
            stmt.setInt(2, numero);
            stmt.setDate(3, Date.valueOf(dataRiferimento));
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateOmbrellone(int numero, int codPrenotazione, LocalDate dataRiferimento) {
        String sql = "UPDATE prenotazione_giornaliera " +
                        "SET Numero = ? " +
                        "WHERE CodPrenotazione = ? " +
                        "AND DataRiferimento = ?;";


        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, numero);
            stmt.setInt(2, codPrenotazione);
            stmt.setDate(3, Date.valueOf(dataRiferimento));
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<PrenotazioneGiornaliera> getPrenotazioniGiornaliere(int codPrenotazione) {
        List<PrenotazioneGiornaliera> prenGiorn = new ArrayList<>();

        String sql = "SELECT * " +
                        "FROM prenotazione_giornaliera " +
                        "WHERE CodPrenotazione = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codPrenotazione);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int codP = rs.getInt("CodPrenotazione");
                    int numero = rs.getInt("Numero");
                    LocalDate dataRiferimento = rs.getDate("DataRiferimento").toLocalDate();

                    PrenotazioneGiornaliera preno = new PrenotazioneGiornaliera(codP, numero, dataRiferimento);
                    prenGiorn.add(preno);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return prenGiorn;
    }

    public boolean deletePrenotazioneGiornaliera(int codPrenotazione, int numero, LocalDate dataRiferimento) {
        String sql = "DELETE FROM prenotazione_giornaliera \n" +
                        "WHERE CodPrenotazione = ? \n" +
                        "AND Numero = ? \n" +
                        "AND DataRiferimento = ?;";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, codPrenotazione);
            stmt.setInt(2, numero);
            stmt.setDate(3, Date.valueOf(dataRiferimento));
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
