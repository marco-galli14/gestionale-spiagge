package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.Prenotazione;

public class PrenotazioneDAO {

    public boolean addPrenotazione(String codPrenotazione, LocalDate dataInizio, LocalDate dataFine,
                                    int codDipendente, String cf) {
        String sql = "INSERT INTO prenotazione (CodPrenotazione, DataInizio, DataFine, PrezzoTotale," +
                                        "StatoPagamento, CodDipendente, CF, CodPacchetto, ID_gruppo) " +
                        "VALUES (?, ?, ?, '00.00', 'Non pagato', ?, ?, NULL," + 
                                "(SELECT c.ID_gruppo FROM cliente c WHERE c.CF = ?));";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, codPrenotazione);
            stmt.setDate(2, Date.valueOf(dataInizio));
            stmt.setDate(3, Date.valueOf(dataFine));
            stmt.setInt(4, codDipendente);
            stmt.setString(5, cf);
            stmt.setString(6, cf);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePacchettoSconto(String codPrenotazione) {
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
            
            stmt.setString(1, codPrenotazione);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateCostoTotale(String codPrenotazione) {
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
                        ") -- 3. Sconto Pacchetto (se non presente, assegna 0%) \n" +
                        "* (1 - COALESCE(ps.PercentualeSconto, 0) / 100.0) \n" +
                        "-- 4. Sconto Hotel (se il cliente non alloggia in hotel, assegna 0%) \n" +
                        "* (1 - COALESCE(( \n" +
                            "SELECT h.ScontoHotel \n" +
                            "FROM cliente c \n" +
                            "JOIN hotel h ON c.CodHotel = h.CodHotel \n" +
                            "WHERE c.CF = p.CF \n" +
                        "), 0) / 100.0) \n" +
                        "-- 5. Sconto Gruppo (se non applicabile, assegna 0%) \n" +
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
                        "-- 6. Costo Noleggio Attrezzature (Blocco NON scontato) \n" +
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
            
            stmt.setString(1, codPrenotazione);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }
    
    public boolean updateStatoPagamento(String codPrenotazione) {
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
            
            stmt.setString(1, codPrenotazione);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePrenotazione(String codPrenotazione) {
        String sql = "DELETE FROM prenotazione WHERE CodPrenotazione = ?; ";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, codPrenotazione);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Prenotazione> getPrenotazioniNonSaldate() {
        List<Prenotazione> nonSaldate = new ArrayList<>();

        String sql = "SELECT *\n" + 
                        "FROM prenotazione p\n" + 
                        "WHERE p.StatoPagamento = 'Non pagato'";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String codP = rs.getString("CodPrenotazione");
                    LocalDate datI = rs.getDate("DataInizio").toLocalDate();
                    LocalDate datF = rs.getDate("DataFine").toLocalDate();
                    int prezzo = rs.getInt("PrezzoTotale");
                    int cd = rs.getInt("CodDipendente");
                    String cf = rs.getString("CF");
                    String cp = rs.getString("CodPacchetto");
                    Integer idGruppo = rs.getInt("ID_gruppo");

                    Prenotazione pren = new Prenotazione(codP, datI, datF, prezzo, false, cd, cf, cp, idGruppo);
                    nonSaldate.add(pren);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nonSaldate;
    }

}
