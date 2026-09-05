package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class PrenotazioneDAO {

    public boolean addPrenotazione(String codPrenotazione, LocalDate dataInizio, LocalDate dataFine,
                                    int codDipendente, String cf) {
        String sql = "INSERT INTO prenotazione (CodPrenotazione, DataInizio, DataFine, PrezzoTotale," +
                                        "StatoPagamento, CodDipendente, CF, CodPacchetto, ID_gruppo)" +
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
}
