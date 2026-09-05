package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class NoleggioAttrezzaturaDao {

    public boolean inserisciNoleggioAttrezzatura(String codNoleggio, LocalDate dataNoleggio, LocalTime oraInizio, 
                                                   int durataOre, String cf, String codDipendente, String codAttrezzatura) {
        String query = "INSERT INTO noleggio_attrezzatura (CodNoleggio, DataNoleggio, OraInizio, DurataOre, CostoTotale, CF, CodPrenotazione, CodDipendente, CodAttrezzatura) " +
                       "VALUES (?, ?, ?, ?, NULL, ?, (SELECT CodPrenotazione FROM Prenotazione WHERE CF = ? AND ? BETWEEN DataInizio AND DataFine LIMIT 1), ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, codNoleggio);
            pstmt.setDate(2, java.sql.Date.valueOf(dataNoleggio));
            pstmt.setTime(3, java.sql.Time.valueOf(oraInizio));
            pstmt.setInt(4, durataOre);
            pstmt.setString(5, cf);
            pstmt.setString(6, cf); // per la subquery CF
            pstmt.setDate(7, java.sql.Date.valueOf(dataNoleggio)); // per la subquery date range
            pstmt.setString(8, codDipendente);
            pstmt.setString(9, codAttrezzatura);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean aggiornaCostoTotaleNoleggio(String codNoleggio) {
        String query = "UPDATE noleggio_attrezzatura n, attrezzatura a, tariffa_noleggio t, stagione s " +
                       "SET n.CostoTotale = n.DurataOre * t.TariffaOraria " +
                       "WHERE n.CodAttrezzatura = a.CodAttrezzatura " +
                       "AND a.CodAttrezzatura = t.CodAttrezzatura " +
                       "AND t.CodStagione = s.CodStagione " +
                       "AND n.CodNoleggio = ? " +
                       "AND n.DataNoleggio BETWEEN s.DataInizio AND s.DataFine";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, codNoleggio);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminaNoleggioAttrezzatura(String codNoleggio) {
        String query = "DELETE FROM noleggio_attrezzatura WHERE CodNoleggio = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, codNoleggio);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}