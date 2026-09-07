package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;

import common.Pair;

public class PagamentoDAO {

    public boolean insertPagamentoPrenotazione(int importo, LocalDate dataPagamento, String metodoPagamento, int codPrenotazione) {
        
        String query = "INSERT INTO PAGAMENTO (DataPagamento, Importo, MetodoPagamento, CodPrenotazione, CodNoleggio) " +
                        "VALUES (?, ?, ?, ?, NULL)";

        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(query)) {
    
                    ps.setDate(1, java.sql.Date.valueOf(dataPagamento));
                    ps.setInt(2, importo);
                    ps.setString(3, metodoPagamento);
                    ps.setInt(4, codPrenotazione);
    
                    int rowsAffected = ps.executeUpdate();
                    return rowsAffected > 0;
                    
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
    }

    public boolean insertPagamentoNoleggio(int importo, LocalDate dataPagamento, String metodoPagamento, int codNoleggio) {
        
        String query = "INSERT INTO PAGAMENTO (DataPagamento, Importo, MetodoPagamento, CodPrenotazione, CodNoleggio) " +
                        "VALUES (?, ?, ?, NULL, ?)";

        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(query)) {
    
                    ps.setDate(1, java.sql.Date.valueOf(dataPagamento));
                    ps.setInt(2, importo);
                    ps.setString(3, metodoPagamento);
                    ps.setInt(4, codNoleggio);
    
                    int rowsAffected = ps.executeUpdate();
                    return rowsAffected > 0;
                    
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
    }

    public Pair<Integer, Integer> getDailyReport(LocalDate date) {
        String query = "SELECT " +
                        "(SELECT SUM(p.Importo) " +
                        " FROM pagamento p " +
                        " WHERE p.DataPagamento = ? " +
                        " AND p.CodPrenotazione IS NOT NULL) AS IncassiPrenotazioniSpiaggia, " +
                        "(SELECT SUM(p.Importo) " +
                        " FROM pagamento p " +
                        " WHERE p.DataPagamento = ? " +
                        " AND p.CodNoleggio IS NOT NULL) AS IncassiNoleggiAttrezzature";

        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(query)) {
    
                    Date sqlDATE = Date.valueOf(date);
                    ps.setDate(1, sqlDATE);
                    ps.setDate(2, sqlDATE);

                    var rs = ps.executeQuery();
                    if (rs.next()) {
                        int incassiPrenotazioni = rs.getInt("IncassiPrenotazioniSpiaggia");
                        int incassiNoleggi = rs.getInt("IncassiNoleggiAttrezzature");
                        return new Pair<>(incassiPrenotazioni, incassiNoleggi);
                    } else {
                        return new Pair<>(0, 0);
                    }

            } catch (Exception e) {
                e.printStackTrace();
                return new Pair<>(0, 0);
            }
    }
}