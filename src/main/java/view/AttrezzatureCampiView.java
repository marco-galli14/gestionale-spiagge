package view;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import common.StoricoNoleggio;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Cliente;
import view.MainView.*;

public class AttrezzatureCampiView extends BaseView {

    private Scene scenaMenuAttrezzature;
    private Scene scenaMenuCampi;
    private List<StoricoNoleggio> listaStoricoNoleggiAttuali = new ArrayList<>();

    private OnSalvaClienteListener onSalvaClienteAction;
    private Supplier<List<Cliente>> onRichiediListaClienti;
    private OnCreaNoleggioListener onCreaNoleggioAction;
    private Consumer<Integer> onEliminaNoleggioAction;
    private OnCreaPrenotazioneCampoListener onCreaPrenotazioneCampoAction;
    private Consumer<Integer> onEliminaPrenotazioneCampoAction;

    public AttrezzatureCampiView(Stage stage, Runnable onTornaAllaMappa) {
        super(stage, onTornaAllaMappa);
    }

    public void setOnSalvaClienteAction(OnSalvaClienteListener l) { this.onSalvaClienteAction = l; }
    public void setOnRichiediListaClienti(Supplier<List<Cliente>> l) { this.onRichiediListaClienti = l; }
    public void setOnCreaNoleggioAction(OnCreaNoleggioListener l) { this.onCreaNoleggioAction = l; }
    public void setOnEliminaNoleggioAction(Consumer<Integer> l) { this.onEliminaNoleggioAction = l; }
    public void setOnCreaPrenotazioneCampoAction(OnCreaPrenotazioneCampoListener l) { this.onCreaPrenotazioneCampoAction = l; }
    public void setOnEliminaPrenotazioneCampoAction(Consumer<Integer> l) { this.onEliminaPrenotazioneCampoAction = l; }

    public void impostaTabellaNoleggi(List<StoricoNoleggio> storico) {
        this.listaStoricoNoleggiAttuali = storico;
    }

    public void mostraMenuAttrezzature() {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(40)); layout.setAlignment(Pos.CENTER); layout.setStyle("-fx-background-color: #f8f9fa;");
        Label titolo = new Label("GESTIONE ATTREZZATURE");
        titolo.setFont(new Font("System Bold", 28));

        Button btnAggiungi = creaPulsanteSezione("Aggiungi Noleggio", 50);
        Button btnStorico = creaPulsanteSezione("Storico Noleggi", 50);
        Button btnElimina = creaPulsanteSezione("Elimina Noleggio", 50);
        Button btnIndietro = creaPulsanteIndietro("⬅ Torna alla Mappa", onTornaAllaMappa);

        btnAggiungi.setOnAction(e -> mostraFormAggiungiNoleggio());
        btnStorico.setOnAction(e -> mostraStoricoNoleggi());
        btnElimina.setOnAction(e -> mostraFormEliminaNoleggio());

        layout.getChildren().addAll(titolo, btnAggiungi, btnStorico, btnElimina, btnIndietro);
        this.scenaMenuAttrezzature = new Scene(layout, stage.getScene().getWidth(), stage.getScene().getHeight());
        stage.setScene(scenaMenuAttrezzature);
    }

    public void mostraMenuCampi() {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(40)); layout.setAlignment(Pos.CENTER); layout.setStyle("-fx-background-color: #f8f9fa;");
        Label titolo = new Label("GESTIONE CAMPI SPORTIVI");
        titolo.setFont(new Font("System Bold", 28));

        Button btnInserisci = creaPulsanteSezione("Inserisci Prenotazione Campo", 50);
        Button btnElimina = creaPulsanteSezione("Elimina Prenotazione Campo", 50);
        Button btnIndietro = creaPulsanteIndietro("⬅ Torna alla Mappa", onTornaAllaMappa);

        btnInserisci.setOnAction(e -> mostraFormAggiungiPrenotazioneCampo());
        btnElimina.setOnAction(e -> mostraFormEliminaPrenotazioneCampo());

        layout.getChildren().addAll(titolo, btnInserisci, btnElimina, btnIndietro);
        this.scenaMenuCampi = new Scene(layout, stage.getScene().getWidth(), stage.getScene().getHeight());
        stage.setScene(scenaMenuCampi);
    }

    private void mostraFormAggiungiNoleggio() {
        GridPane grid = new GridPane(); grid.setVgap(12); grid.setHgap(15); grid.setAlignment(Pos.CENTER);
        DatePicker datePicker = new DatePicker(LocalDate.now());
        TextField txtOra = new TextField("10:00");
        TextField txtDurata = new TextField();
        TextField txtCf = new TextField();

        Button btnCercaCf = new Button("🔍 Cerca");
        btnCercaCf.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-weight: bold;");
        btnCercaCf.setOnAction(e -> mostraSelezionatoreCliente(txtCf, onRichiediListaClienti));

        Button btnNuovoCliente = new Button("➕ Nuovo");
        btnNuovoCliente.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        btnNuovoCliente.setOnAction(e -> mostraFormNuovoClienteRapido(txtCf, onSalvaClienteAction));

        HBox boxCf = new HBox(8, txtCf, btnCercaCf, btnNuovoCliente);
        boxCf.setAlignment(Pos.CENTER_LEFT);

        TextField txtDipendente = new TextField();
        ComboBox<String> cmbAttrezzatura = new ComboBox<>();
        cmbAttrezzatura.getItems().addAll("PED", "CAN", "SUP");
        cmbAttrezzatura.setValue("PED");

        grid.add(new Label("Data Noleggio:"), 0, 0); grid.add(datePicker, 1, 0);
        grid.add(new Label("Ora Inizio (HH:mm):"), 0, 1); grid.add(txtOra, 1, 1);
        grid.add(new Label("Durata (Ore):"), 0, 2); grid.add(txtDurata, 1, 2);
        grid.add(new Label("CF Cliente:"), 0, 3); grid.add(boxCf, 1, 3);
        grid.add(new Label("Cod. Dipendente:"), 0, 4); grid.add(txtDipendente, 1, 4);
        grid.add(new Label("Attrezzatura:"), 0, 5); grid.add(cmbAttrezzatura, 1, 5);

        Button btnSalva = new Button("Salva Noleggio");
        btnSalva.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");
        btnSalva.setOnAction(e -> {
            try {
                if (onCreaNoleggioAction != null) {
                    onCreaNoleggioAction.onCrea(datePicker.getValue(), LocalTime.parse(txtOra.getText()), Integer.parseInt(txtDurata.getText()),
                            txtCf.getText(), Integer.parseInt(txtDipendente.getText()), cmbAttrezzatura.getValue());
                    onTornaAllaMappa.run();
                }
            } catch (Exception ex) {
                mostraMessaggioEsito(false, "", "Formato dati non valido.");
            }
        });

        Button btnAnnulla = creaPulsanteIndietro("Indietro", () -> stage.setScene(scenaMenuAttrezzature));
        HBox boxAzioni = new HBox(15, btnSalva, btnAnnulla);
        boxAzioni.setAlignment(Pos.CENTER);

        VBox layout = new VBox(25, new Label("INSERISCI NUOVO NOLEGGIO"), grid, boxAzioni);
        layout.setAlignment(Pos.CENTER); layout.setPadding(new Insets(30));
        stage.setScene(new Scene(layout, stage.getScene().getWidth(), stage.getScene().getHeight()));
    }

    private void mostraFormEliminaNoleggio() {
        VBox layout = new VBox(15); layout.setAlignment(Pos.CENTER);
        TextField txtCod = new TextField(); txtCod.setMaxWidth(200);

        Button btnElimina = new Button("Elimina Definitivamente");
        btnElimina.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        btnElimina.setOnAction(e -> {
            try {
                if (onEliminaNoleggioAction != null) {
                    onEliminaNoleggioAction.accept(Integer.parseInt(txtCod.getText()));
                    onTornaAllaMappa.run();
                }
            } catch (NumberFormatException ex) {
                mostraMessaggioEsito(false, "", "Il codice deve essere numerico.");
            }
        });

        Button btnIndietro = new Button("Annulla");
        btnIndietro.setOnAction(e -> stage.setScene(scenaMenuAttrezzature));

        HBox boxAzioni = new HBox(10, btnElimina, btnIndietro);
        boxAzioni.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(new Label("Codice Numerico Noleggio da eliminare:"), txtCod, boxAzioni);
        stage.setScene(new Scene(layout, stage.getScene().getWidth(), stage.getScene().getHeight()));
    }

    @SuppressWarnings("unchecked")
    private void mostraStoricoNoleggi() {
        TableView<StoricoNoleggio> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<StoricoNoleggio, String> colCf = new TableColumn<>("CF");
        colCf.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().cf()));
        TableColumn<StoricoNoleggio, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().nome()));
        TableColumn<StoricoNoleggio, String> colCognome = new TableColumn<>("Cognome");
        colCognome.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().cognome()));
        TableColumn<StoricoNoleggio, String> colCodAttrezzatura = new TableColumn<>("Codice Attrezzatura");
        colCodAttrezzatura.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().codAttrezzatura()));
        TableColumn<StoricoNoleggio, Integer> colCod = new TableColumn<>("Cod Noleggio");
        colCod.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().codNoleggio()));
        TableColumn<StoricoNoleggio, LocalDate> colData = new TableColumn<>("Data");
        colData.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().dataNoleggio()));
        TableColumn<StoricoNoleggio, LocalTime> colOra = new TableColumn<>("Ora Inizio");
        colOra.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().oraInizio()));
        TableColumn<StoricoNoleggio, Integer> colDurata = new TableColumn<>("Durata (h)");
        colDurata.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().durataOre()));
        TableColumn<StoricoNoleggio, Integer> colCosto = new TableColumn<>("Costo Tot (€)");
        colCosto.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().costoTotale()));

        table.getColumns().addAll(colCf, colNome, colCognome, colCod, colData, colCodAttrezzatura, colOra, colDurata, colCosto);
        List<StoricoNoleggio> listaPulita = listaStoricoNoleggiAttuali.stream().filter(n -> n != null).toList();
        table.setItems(FXCollections.observableArrayList(listaPulita));

        Button btnIndietro = new Button("Indietro");
        btnIndietro.setOnAction(e -> stage.setScene(scenaMenuAttrezzature));

        VBox layout = new VBox(15, new Label("STORICO COMPLETO NOLEGGI"), table, btnIndietro);
        layout.setAlignment(Pos.CENTER); layout.setPadding(new Insets(20));
        stage.setScene(new Scene(layout, stage.getScene().getWidth(), stage.getScene().getHeight()));
    }

    private void mostraFormAggiungiPrenotazioneCampo() {
        GridPane grid = new GridPane(); grid.setVgap(12); grid.setHgap(15); grid.setAlignment(Pos.CENTER);
        DatePicker datePicker = new DatePicker(LocalDate.now());
        ComboBox<String> cmbCampo = new ComboBox<>();
        cmbCampo.getItems().addAll("Beach Volley", "Beach Tennis", "Calcetto", "Bocce Sabbia", "Bocce Cemento");
        cmbCampo.setValue("Beach Volley");

        TextField txtOraInizio = new TextField("10:00");
        TextField txtOraFine = new TextField("11:00");
        TextField txtCf = new TextField();

        Button btnCercaCf = new Button("🔍 Cerca");
        btnCercaCf.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-weight: bold;");
        btnCercaCf.setOnAction(e -> mostraSelezionatoreCliente(txtCf, onRichiediListaClienti));

        Button btnNuovoCliente = new Button("➕ Nuovo");
        btnNuovoCliente.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        btnNuovoCliente.setOnAction(e -> mostraFormNuovoClienteRapido(txtCf, onSalvaClienteAction));

        HBox boxCf = new HBox(8, txtCf, btnCercaCf, btnNuovoCliente);
        boxCf.setAlignment(Pos.CENTER_LEFT);
        TextField txtCodDip = new TextField();

        grid.add(new Label("Data Prenotazione:"), 0, 0); grid.add(datePicker, 1, 0);
        grid.add(new Label("Seleziona Campo:"), 0, 1); grid.add(cmbCampo, 1, 1);
        grid.add(new Label("Ora Inizio (HH:mm):"), 0, 2); grid.add(txtOraInizio, 1, 2);
        grid.add(new Label("Ora Fine (HH:mm):"), 0, 3); grid.add(txtOraFine, 1, 3);
        grid.add(new Label("CF Cliente:"), 0, 4); grid.add(boxCf, 1, 4);
        grid.add(new Label("Cod. Dipendente:"), 0, 5); grid.add(txtCodDip, 1, 5);

        Button btnSalva = new Button("Salva Prenotazione Campo");
        btnSalva.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");
        btnSalva.setOnAction(e -> {
            try {
                String codCampoDb = switch (cmbCampo.getValue()) {
                    case "Beach Volley" -> "C1";
                    case "Beach Tennis" -> "C2";
                    case "Calcetto" -> "C3";
                    case "Bocce Sabbia" -> "C4";
                    case "Bocce Cemento" -> "C5";
                    default -> "C1";
                };
                if (onCreaPrenotazioneCampoAction != null) {
                    onCreaPrenotazioneCampoAction.onCrea(datePicker.getValue(), LocalTime.parse(txtOraInizio.getText()),
                            LocalTime.parse(txtOraFine.getText()), codCampoDb, txtCf.getText(), Integer.getInteger(txtCodDip.getText()));
                    onTornaAllaMappa.run();
                }
            } catch (Exception ex) {
                mostraMessaggioEsito(false, "", "Formato dati non valido.");
            }
        });

        Button btnAnnulla = creaPulsanteIndietro("Indietro", () -> stage.setScene(scenaMenuCampi));
        HBox boxAzioni = new HBox(15, btnSalva, btnAnnulla);
        boxAzioni.setAlignment(Pos.CENTER);

        VBox layout = new VBox(25, new Label("INSERISCI PRENOTAZIONE CAMPO"), grid, boxAzioni);
        layout.setAlignment(Pos.CENTER); layout.setPadding(new Insets(30));
        stage.setScene(new Scene(layout, stage.getScene().getWidth(), stage.getScene().getHeight()));
    }

    private void mostraFormEliminaPrenotazioneCampo() {
        VBox layout = new VBox(15); layout.setAlignment(Pos.CENTER);
        TextField txtCod = new TextField(); txtCod.setMaxWidth(200);

        Button btnElimina = new Button("Elimina Definitivamente");
        btnElimina.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        btnElimina.setOnAction(e -> {
            try {
                if (onEliminaPrenotazioneCampoAction != null) {
                    onEliminaPrenotazioneCampoAction.accept(Integer.parseInt(txtCod.getText()));
                    onTornaAllaMappa.run();
                }
            } catch (NumberFormatException ex) {
                mostraMessaggioEsito(false, "", "Il codice deve essere numerico.");
            }
        });

        Button btnIndietro = new Button("Annulla");
        btnIndietro.setOnAction(e -> stage.setScene(scenaMenuCampi));

        HBox boxAzioni = new HBox(10, btnElimina, btnIndietro);
        boxAzioni.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(new Label("Codice Numerico Prenotazione Campo:"), txtCod, boxAzioni);
        stage.setScene(new Scene(layout, stage.getScene().getWidth(), stage.getScene().getHeight()));
    }
}