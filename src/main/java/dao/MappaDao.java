package dao;

import model.PrenotazioneCampo;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MappaDao {

    public static class MappaOmbrelloneInfo {
        private final int numero;
        private final String codZona;
        private final String nomeZona;
        private final String codPrenotazione;
        private final String clientePrenotato;

        public MappaOmbrelloneInfo(int numero, String codZona, String nomeZona, String codPrenotazione, String clientePrenotato) {
            this.numero = numero;
            this.codZona = codZona;
            this.nomeZona = nomeZona;
            this.codPrenotazione = codPrenotazione;
            this.clientePrenotato = clientePrenotato;
        }

        public int getNumero() { return numero; }
        public String getCodZona() { return codZona; }
        public String getNomeZona() { return nomeZona; }
        public String getCodPrenotazione() { return codPrenotazione; }
        public String getClientePrenotato() { return clientePrenotato; }
        public boolean isOccupato() { return codPrenotazione != null; }
    }

    public List<MappaOmbrelloneInfo> getMappaSpiaggia(LocalDate dataRiferimento) {
        List<MappaOmbrelloneInfo> mappa = new ArrayList<>();
        String query = "SELECT o.Numero, o.CodZona, z.NomeZona, " +
                       "(SELECT pg.CodPrenotazione FROM prenotazione_giornaliera pg WHERE pg.Numero = o.Numero AND pg.DataRiferimento = ?) AS CodPrenotazione, " +
                       "(SELECT p.CF FROM prenotazione_giornaliera pg, prenotazione p WHERE pg.CodPrenotazione = p.CodPrenotazione AND pg.Numero = o.Numero AND pg.DataRiferimento = ?) AS ClientePrenotato " +
                       "FROM ombrellone o, zona z WHERE z.CodZona = o.CodZona ORDER BY o.Numero";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDate(1, Date.valueOf(dataRiferimento));
            pstmt.setDate(2, Date.valueOf(dataRiferimento));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    mappa.add(new MappaOmbrelloneInfo(
                        rs.getInt("Numero"),
                        rs.getString("CodZona"),
                        rs.getString("NomeZona"),
                        rs.getString("CodPrenotazione"),
                        rs.getString("ClientePrenotato")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mappa;
    }

    public List<PrenotazioneCampo> getOccupazioneCampi(LocalDate dataPrenotazione, LocalTime oraControllo) {
        List<PrenotazioneCampo> campiOccupati = new ArrayList<>();
        String query = "SELECT * FROM prenotazione_campo pc WHERE pc.DataPrenotazione = ? AND pc.OraInizio <= ? AND pc.OraFine > ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDate(1, Date.valueOf(dataPrenotazione));
            pstmt.setTime(2, Time.valueOf(oraControllo));
            pstmt.setTime(3, Time.valueOf(oraControllo));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    PrenotazioneCampo pc = new PrenotazioneCampo(
                        rs.getString("CodPrenotazione"),
                        rs.getDate("DataPrenotazione").toLocalDate(),
                        rs.getTime("OraInizio").toLocalTime(),
                        rs.getTime("OraFine").toLocalTime(),
                        rs.getString("CF"),
                        rs.getString("CodCampo"),
                        rs.getInt("CodDipendente")
                    );
                    campiOccupati.add(pc);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return campiOccupati;
    }
}