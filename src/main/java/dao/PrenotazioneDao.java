package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import common.StoricoPrenotazione;
import model.Prenotazione;

public class PrenotazioneDAO {

    public int addPrenotazione(LocalDate dataInizio, LocalDate dataFine,
                                int codDipendente, String cf) {
        String sql = "INSERT INTO prenotazione (DataInizio, DataFine, PrezzoTotale," +
                                            "StatoPagamento, CodDipendente, CF, CodPacchetto, ID_gruppo) " +
                        "VALUES (?, ?, '00.00', 'Non pagato', ?, ?, NULL," + 
                                "(SELECT c.ID_gruppo FROM cliente c WHERE c.CF = ?));";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setDate(1, Date.valueOf(dataInizio));
            stmt.setDate(2, Date.valueOf(dataFine));
            stmt.setInt(3, codDipendente);
            stmt.setString(4, cf);
            stmt.setString(5, cf);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // Restituisce il CodPrenotazione generato (AUTO_INCREMENT)
                    }
                }
            }
            return -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean updatePacchettoSconto(int codPrenotazione) {
        String sql = "UPDATE prenotazione p " +
                    "SET p.CodPacchetto = (" +
                        "CASE " +
                            "WHEN (DATEDIFF(p.DataFine, p.DataInizio) + 1) BETWEEN 3 AND 6 THEN 'PACK1' " +
                            "WHEN (DATEDIFF(p.DataFine, p.DataInizio) + 1) BETWEEN 7 AND 29  THEN 'PACK2' " +
                            "WHEN (DATEDIFF(p.DataFine, p.DataInizio) + 1) BETWEEN 30 AND 89 THEN 'PACK3' " +
                            "WHEN (DATEDIFF(p.DataFine, p.DataInizio) + 1) BETWEEN 90 AND 120 THEN 'PACK4' " +
                            "ELSE NULL " +
                        "END) " +
                    "WHERE p.CodPrenotazione = ?;";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, codPrenotazione);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateCostoTotale(int codPrenotazione) {
        String sql = "UPDATE prenotazione p \n" +
                    "LEFT JOIN pacchetto_sconto ps ON p.CodPacchetto = ps.CodPacchetto \n" +
                    "SET p.PrezzoTotale = ROUND(( \n" +
                        "( \n" +
                            "-- 1. Costo totale degli ombrelloni \n" +
                            "COALESCE(( \n" +
                                "SELECT SUM(tz.TariffaGiornaliera) \n" +
                                "FROM prenotazione_giornaliera pg \n" +
                                "JOIN ombrellone o ON pg.Numero = o.Numero \n" +
                                "JOIN tariffa_zona tz ON o.CodZona = tz.CodZona \n" +
                                "JOIN stagione st ON tz.CodStagione = st.CodStagione \n" +
                                "WHERE pg.CodPrenotazione = p.CodPrenotazione \n" +
                                "AND pg.DataRiferimento BETWEEN st.DataInizio AND st.DataFine \n" +
                            "), 0) + \n" +
                            "-- 2. Costo totale delle sedute extra \n" +
                            "COALESCE(( \n" +
                                "SELECT SUM(a.Quantita * s.Costo) \n" +
                                "FROM allestimento a \n" +
                                "JOIN seduta s ON a.CodSeduta = s.CodSeduta \n" +
                                "WHERE a.CodPrenotazione = p.CodPrenotazione \n" +
                            "), 0) \n" +
                        ") -- 3. Sconto Pacchetto \n" +
                        "* (1 - COALESCE(ps.PercentualeSconto, 0) / 100.0) \n" +
                        "-- 4. Sconto Hotel \n" +
                        "* (1 - COALESCE(( \n" +
                            "SELECT h.ScontoHotel \n" +
                            "FROM cliente c \n" +
                            "JOIN hotel h ON c.CodHotel = h.CodHotel \n" +
                            "WHERE c.CF = p.CF \n" +
                        "), 0) / 100.0) \n" +
                        "-- 5. Sconto Gruppo \n" +
                        "* (1 - CASE \n" +
                            "WHEN p.ID_gruppo IS NULL THEN 0 \n" +
                            "WHEN ( \n" +
                                "SELECT COUNT(DISTINCT pg_g.Numero) \n" +
                                "FROM prenotazione_giornaliera pg_g \n" +
                                "WHERE pg_g.CodPrenotazione = p.CodPrenotazione \n" +
                            ") >= ( \n" +
                                "SELECT COUNT(*) / 2.0 \n" +
                                "FROM cliente c_grp \n" +
                                "WHERE c_grp.ID_gruppo = p.ID_gruppo \n" +
                            ") THEN ( \n" +
                                "SELECT g.ScontoGruppo \n" +
                                "FROM gruppo g \n" +
                                "WHERE g.ID_gruppo = p.ID_gruppo \n" +
                            ") \n" +
                            "ELSE 0 \n" +
                        "END / 100.0) \n" +
                        "-- 6. Costo Noleggio Attrezzature \n" +
                        "+ COALESCE(( \n" +
                            "SELECT SUM(n.DurataOre * tn.TariffaOraria) \n" +
                            "FROM noleggio_attrezzatura n \n" +
                            "JOIN tariffa_noleggio tn ON n.CodAttrezzatura = tn.CodAttrezzatura \n" +
                            "JOIN stagione st ON tn.CodStagione = st.CodStagione \n" +
                            "WHERE n.CodPrenotazione = p.CodPrenotazione \n" +
                            "AND n.DataNoleggio BETWEEN st.DataInizio AND st.DataFine \n" +
                        "), 0) \n" +
                    "), 2) \n" +
                    "WHERE p.CodPrenotazione = ? ;";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, codPrenotazione);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateStatoPagamento(int codPrenotazione) {
        String sql = "UPDATE PRENOTAZIONE p \n" + 
                    "SET p.StatoPagamento = CASE \n" + 
                    "WHEN (\n" + 
                    "SELECT SUM(pg.Importo) \n" + 
                    "FROM pagamento pg \n" + 
                    "WHERE pg.CodPrenotazione = p.CodPrenotazione\n" + 
                    ") >= p.PrezzoTotale \n" + 
                    "THEN 'Pagato' \n" + 
                    "ELSE 'Non Saldato' \n" + 
                    "END \n" + 
                    "WHERE p.CodPrenotazione = ?;";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, codPrenotazione);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminaPrenotazione(int codPrenotazione) {
        String sql = "DELETE FROM prenotazione WHERE CodPrenotazione = ?;";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, codPrenotazione);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Prenotazione> getPrenotazioniNonSaldate() {
        List<Prenotazione> nonSaldate = new ArrayList<>();
        String sql = "SELECT * FROM prenotazione p WHERE p.StatoPagamento = 'Non pagato'";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int codP = rs.getInt("CodPrenotazione");
                LocalDate datI = rs.getDate("DataInizio").toLocalDate();
                LocalDate datF = rs.getDate("DataFine").toLocalDate();
                int prezzo = rs.getInt("PrezzoTotale");
                int cd = rs.getInt("CodDipendente");
                String cf = rs.getString("CF");
                String cp = rs.getString("CodPacchetto");
                Integer idGruppo = rs.getInt("ID_gruppo");

                nonSaldate.add(new Prenotazione(codP, datI, datF, prezzo, false, cd, cf, cp, idGruppo));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nonSaldate;
    }

    public List<StoricoPrenotazione> getStoricoPrenotazioni() {
        List<StoricoPrenotazione> storicoPrenotazioni = new ArrayList<>();
        String query = "SELECT c.CF, c.Nome, c.Cognome, p.CodPrenotazione, p.DataInizio, p.DataFine, p.PrezzoTotale " +
                        "FROM cliente c, prenotazione p " +
                        "WHERE c.CF = p.CF";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String cf = rs.getString("CF");
                String nome = rs.getString("Nome");
                String cognome = rs.getString("Cognome");
                int codPrenotazione = rs.getInt("CodPrenotazione");
                LocalDate dataInizio = rs.getDate("DataInizio").toLocalDate();
                LocalDate dataFine = rs.getDate("DataFine").toLocalDate();
                int prezzoTotale = rs.getInt("PrezzoTotale");

                storicoPrenotazioni.add(new StoricoPrenotazione(cf, nome, cognome, codPrenotazione, dataInizio, dataFine, prezzoTotale));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return storicoPrenotazioni;
    }
}