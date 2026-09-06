package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;

import javafx.util.Pair;

public class PagamentoDAO {

    public boolean insertPagamentoPrenotazione(String codPagamento, int importo, LocalDate dataPagamento, String metodoPagamento, String codPrenotazione) {
        
        String query = "INSERT INTO PAGAMENTO (CodPagamento, DataPagamento, Importo, MetodoPagamento, CodPrenotazione, CodNoleggio) " +
                        "VALUES (?, ?, ?, ?, ?, NULL)";

        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(query)) {
    
                    ps.setString(1, codPagamento);
                    ps.setDate(2, java.sql.Date.valueOf(dataPagamento));
                    ps.setInt(3, importo);
                    ps.setString(4, metodoPagamento);
                    ps.setString(5, codPrenotazione);
    
                    int rowsAffected = ps.executeUpdate();
                    return rowsAffected > 0;
                    
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
    }

    public boolean insertPagamentoNoleggio(String codPagamento, int importo, LocalDate dataPagamento, String metodoPagamento, String codNoleggio) {
        
        String query = "INSERT INTO PAGAMENTO (CodPagamento, DataPagamento, Importo, MetodoPagamento, CodPrenotazione, CodNoleggio) " +
                        "VALUES (?, ?, ?, ?, NULL, ?)";

        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(query)) {
    
                    ps.setString(1, codPagamento);
                    ps.setDate(2, java.sql.Date.valueOf(dataPagamento));
                    ps.setInt(3, importo);
                    ps.setString(4, metodoPagamento);
                    ps.setString(5, codNoleggio);
    
                    int rowsAffected = ps.executeUpdate();
                    return rowsAffected > 0;
                    
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
    }

    public Pair<Integer, Integer> getDailyReport(LocalDate date) {

        String query = "SELECT " +
                        "(SELECT SUM(p.PrezzoTotale) " +
                        " FROM PRENOTAZIONE p " + 
                        " WHERE p.DataInizio = ?) AS IncassiPrenotazioniSpiaggia," +
                        " (SELECT SUM(na.CostoTotale) " +
                        "  FROM NOLEGGIO_ATTREZZATURA na " +
                        "  WHERE na.DataNoleggio = ?) AS IncassiNoleggiAttrezzature";

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
