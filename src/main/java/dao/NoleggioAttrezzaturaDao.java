package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import common.StoricoNoleggio;

public class NoleggioAttrezzaturaDAO {

    public int inserisciNoleggioAttrezzatura(LocalDate dataNoleggio, LocalTime oraInizio, 
                                            int durataOre, String cf, String codDipendente, String codAttrezzatura) {
        
        String query = "INSERT INTO noleggio_attrezzatura (DataNoleggio, OraInizio, DurataOre, CostoTotale, CF, CodPrenotazione, CodDipendente, CodAttrezzatura) " +
                       "VALUES (?, ?, ?, '00.00', ?, (SELECT CodPrenotazione FROM Prenotazione WHERE CF = ? AND ? BETWEEN DataInizio AND DataFine LIMIT 1), ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setDate(1, java.sql.Date.valueOf(dataNoleggio));
            pstmt.setTime(2, java.sql.Time.valueOf(oraInizio));
            pstmt.setInt(3, durataOre);
            pstmt.setString(4, cf);
            pstmt.setString(5, cf); 
            pstmt.setDate(6, java.sql.Date.valueOf(dataNoleggio)); 
            pstmt.setString(7, codDipendente);
            pstmt.setString(8, codAttrezzatura);

            if (pstmt.executeUpdate() > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean aggiornaCostoTotaleNoleggio(int codNoleggio) {
        String query = "UPDATE noleggio_attrezzatura n, attrezzatura a, tariffa_noleggio t, stagione s " +
                       "SET n.CostoTotale = n.DurataOre * t.TariffaOraria " +
                       "WHERE n.CodAttrezzatura = a.CodAttrezzatura " +
                       "AND a.CodAttrezzatura = t.CodAttrezzatura " +
                       "AND t.CodStagione = s.CodStagione " +
                       "AND n.CodNoleggio = ? " +
                       "AND n.DataNoleggio BETWEEN s.DataInizio AND s.DataFine";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, codNoleggio);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminaNoleggioAttrezzatura(int codNoleggio) {
        String query = "DELETE FROM noleggio_attrezzatura WHERE CodNoleggio = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, codNoleggio);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<StoricoNoleggio> getStoricoNoleggi() {
        List<StoricoNoleggio> storicoNoleggi = new ArrayList<>();

        String query = "SELECT c.CF, c.Nome, c.Cognome, n.CodNoleggio, n.DataNoleggio, n.codAttrezzatura, n.OraInizio, n.DurataOre, n.CostoTotale " +
                        "FROM cliente c, noleggio_attrezzatura n " +
                        "WHERE c.CF = n.CF";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String cf = rs.getString("CF");
                String nome = rs.getString("Nome");
                String cognome = rs.getString("Cognome");
                int codNoleggio = rs.getInt("CodNoleggio");
                LocalDate dataNoleggio = rs.getDate("DataNoleggio").toLocalDate();
                String codAttrezzatura = rs.getString("codAttrezzatura");
                LocalTime oraInizio = rs.getTime("OraInizio").toLocalTime();
                int durataOre = rs.getInt("DurataOre");
                int costoTotale = rs.getInt("CostoTotale");

                storicoNoleggi.add(new StoricoNoleggio(cf, nome, cognome, codNoleggio, dataNoleggio, codAttrezzatura, oraInizio, durataOre, costoTotale));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return storicoNoleggi;
    }
}
