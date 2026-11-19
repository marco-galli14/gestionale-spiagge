package view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import common.StoricoPrenotazione;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Allestimento;
import model.Cliente;
import model.PrenotazioneGiornaliera;
import view.MainView.*;

public class OmbrelloniView extends BaseView {

    private Scene scenaMenuOmbrelloni;
    private final Supplier<LocalDate> dataSelezionataSupplier;

    private OnSalvaClienteListener onSalvaClienteAction;
    private Supplier<List<Cliente>> onRichiediListaClienti;
    private OnCreaPrenotazioneListener onCreaPrenotazioneAction;
    private Consumer<Integer> onEliminaPrenotazioneSpiaggiaAction;
    private OnModificaOmbrelloneListener onModificaOmbrelloneAction;
    private BiFunction<LocalDate, LocalDate, List<Integer>> onRichiediOmbrelloniDisponibili;
    private Supplier<List<StoricoPrenotazione>> onRichiediStoricoPrenotazioniList;
    private Runnable onRichiediStoricoPrenotazioni;
    private Consumer<Integer> onRichiediPrenotazioniGiornaliere;
    private OnModificaAllestimentoGiornalieroListener onModificaAllestimentoGiornaliero;
    private OnRichiediAllestimentiGiornoListener onRichiediAllestimentiGiorno;

    public OmbrelloniView(Stage stage, Runnable onTornaAllaMappa, Supplier<LocalDate> dataSelezionataSupplier) {
        super(stage, onTornaAllaMappa);
        this.dataSelezionataSupplier = dataSelezionataSupplier;
    }

    public void setOnSalvaClienteAction(OnSalvaClienteListener l) { this.onSalvaClienteAction = l; }
    public void setOnRichiediListaClienti(Supplier<List<Cliente>> l) { this.onRichiediListaClienti = l; }
    public void setOnCreaPrenotazioneAction(OnCreaPrenotazioneListener l) { this.onCreaPrenotazioneAction = l; }
    public void setOnEliminaPrenotazioneSpiaggiaAction(Consumer<Integer> l) { this.onEliminaPrenotazioneSpiaggiaAction = l; }
    public void setOnModificaOmbrelloneAction(OnModificaOmbrelloneListener l) { this.onModificaOmbrelloneAction = l; }
    public void setOnRichiediOmbrelloniDisponibili(BiFunction<LocalDate, LocalDate, List<Integer>> l) { this.onRichiediOmbrelloniDisponibili = l; }
    public void setOnRichiediStoricoPrenotazioniList(Supplier<List<StoricoPrenotazione>> l) { this.onRichiediStoricoPrenotazioniList = l; }
    public void setOnRichiediStoricoPrenotazioni(Runnable l) { this.onRichiediStoricoPrenotazioni = l; }
    public void setOnRichiediPrenotazioniGiornaliere(Consumer<Integer> l) { this.onRichiediPrenotazioniGiornaliere = l; }
    public void setOnModificaAllestimentoGiornaliero(OnModificaAllestimentoGiornalieroListener l) { this.onModificaAllestimentoGiornaliero = l; }
    public void setOnRichiediAllestimentiGiorno(OnRichiediAllestimentiGiornoListener l) { this.onRichiediAllestimentiGiorno = l; }

    public void mostraMenu() {
        Label titolo = new Label("GESTIONE OMBRELLONI E PRENOTAZIONI");
        titolo.setFont(new Font("System Bold", 22));

        Button btnInserisciPren = creaPulsanteSezione("Inserisci Prenotazione", 40);
        Button btnEliminaPren = creaPulsanteSezione("Elimina Prenotazione", 40);
        Button btnModificaOmb = creaPulsanteSezione("Modifica Ombrellone", 40);
        Button btnStoricoPren = creaPulsanteSezione("Storico Prenotazioni", 40);
        Button btnPrenGior = creaPulsanteSezione("Visualizza Prenotazioni Giornaliere", 40);
        Button btnModificaAllestimentoGiorno = creaPulsanteSezione("Modifica Allestimento Giornaliero", 40);
        Button btnIndietro = creaPulsanteIndietro("⬅ Torna alla Mappa", onTornaAllaMappa);

        btnInserisciPren.setOnAction(e -> mostraFormInserisciPrenotazione(null));
        btnEliminaPren.setOnAction(e -> mostraFormEliminaPrenotazione());
        btnModificaOmb.setOnAction(e -> mostraFormModificaOmbrellone());
        btnStoricoPren.setOnAction(e -> { if (onRichiediStoricoPrenotazioni != null) onRichiediStoricoPrenotazioni.run(); });
        btnPrenGior.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Ricerca Prenotazione");
            dialog.setHeaderText("Visualizza Prenotazioni Giornaliere");
            dialog.setContentText("Inserisci il codice prenotazione:");
            dialog.showAndWait().ifPresent(input -> {
                try {
                    if (onRichiediPrenotazioniGiornaliere != null) onRichiediPrenotazioniGiornaliere.accept(Integer.parseInt(input.trim()));
                } catch (NumberFormatException ex) {
                    mostraMessaggioEsito(false, "", "Inserisci un numero intero valido.");
                }
            });
        });
        btnModificaAllestimentoGiorno.setOnAction(e -> mostraFormModificaAllestimentoGiornaliero());

        VBox contentBox = new VBox(10, titolo, btnInserisciPren, btnEliminaPren, btnModificaOmb, btnStoricoPren, btnPrenGior, btnModificaAllestimentoGiorno, btnIndietro);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(30));

        ScrollPane scroll = new ScrollPane(contentBox);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        this.scenaMenuOmbrelloni = new Scene(scroll, stage.getScene().getWidth(), stage.getScene().getHeight());
        stage.setScene(scenaMenuOmbrelloni);
    }

    public void apriFormNuovaPrenotazionePerCella(int numeroCella) {
        mostraFormInserisciPrenotazione(numeroCella);
    }

    public void mostraFormInserisciPrenotazione(Integer numeroOmbrellonePreselezionato) {
        GridPane grid = new GridPane();
        grid.setVgap(12); grid.setHgap(15); grid.setAlignment(Pos.CENTER);

        LocalDate dataBase = dataSelezionataSupplier.get();
        DatePicker dpInizio = new DatePicker(dataBase);
        DatePicker dpFine = new DatePicker(dataBase.plusDays(7));
        TextField txtCf = new TextField();

        Button btnCercaCf = new Button("🔍 Cerca");
        btnCercaCf.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-weight: bold;");
        btnCercaCf.setOnAction(e -> mostraSelezionatoreCliente(txtCf, onRichiediListaClienti));

        Button btnNuovoCliente = new Button("➕ Nuovo");
        btnNuovoCliente.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        btnNuovoCliente.setOnAction(e -> mostraFormNuovoClienteRapido(txtCf, onSalvaClienteAction));

        HBox boxCf = new HBox(8, txtCf, btnCercaCf, btnNuovoCliente);
        boxCf.setAlignment(Pos.CENTER_LEFT);

        TextField txtDip = new TextField();
        TextField txtOmbrelloni = new TextField();
        txtOmbrelloni.setPromptText("Seleziona dalla mappa");
        txtOmbrelloni.setEditable(false);
        if (numeroOmbrellonePreselezionato != null) txtOmbrelloni.setText(String.valueOf(numeroOmbrellonePreselezionato));

        Button btnScegliOmb = new Button("🏖️ Scegli Ombrelloni");
        btnScegliOmb.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-weight: bold;");
        btnScegliOmb.setOnAction(e -> mostraMiniMappaDisponibili(dpInizio.getValue(), dpFine.getValue(), txtOmbrelloni));
        HBox boxOmb = new HBox(8, txtOmbrelloni, btnScegliOmb);
        boxOmb.setAlignment(Pos.CENTER_LEFT);

        Spinner<Integer> spnLettini = new Spinner<>(0, 10, 0);
        Spinner<Integer> spnSdraio = new Spinner<>(0, 10, 0);
        Spinner<Integer> spnSedie = new Spinner<>(0, 10, 0);
        spnLettini.setPrefWidth(70); spnSdraio.setPrefWidth(70); spnSedie.setPrefWidth(70);
        HBox boxAllestimenti = new HBox(10, new Label("Lettini:"), spnLettini, new Label("Sdraio:"), spnSdraio, new Label("Sedie:"), spnSedie);
        boxAllestimenti.setAlignment(Pos.CENTER_LEFT);

        grid.add(new Label("Data Inizio:"), 0, 0); grid.add(dpInizio, 1, 0);
        grid.add(new Label("Data Fine:"), 0, 1); grid.add(dpFine, 1, 1);
        grid.add(new Label("CF Cliente:"), 0, 2); grid.add(boxCf, 1, 2);
        grid.add(new Label("Cod. Dipendente:"), 0, 3); grid.add(txtDip, 1, 3);
        grid.add(new Label("Ombrelloni:"), 0, 4); grid.add(boxOmb, 1, 4);
        grid.add(new Label("Allestimento Extra:"), 0, 5); grid.add(boxAllestimenti, 1, 5);

        Button btnSalva = new Button("Salva Prenotazione");
        btnSalva.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");
        btnSalva.setOnAction(e -> {
            if (onCreaPrenotazioneAction != null) {
                try {
                    List<Integer> listaOmbrelloni = Arrays.stream(txtOmbrelloni.getText().split(","))
                            .map(String::trim).filter(s -> !s.isEmpty()).map(Integer::parseInt).collect(Collectors.toList());
                    onCreaPrenotazioneAction.onCrea(dpInizio.getValue(), dpFine.getValue(), Integer.parseInt(txtDip.getText()),
                            txtCf.getText(), listaOmbrelloni, spnLettini.getValue(), spnSdraio.getValue(), spnSedie.getValue());
                    onTornaAllaMappa.run();
                } catch (Exception ex) {
                    mostraMessaggioEsito(false, "", "Controlla i dati inseriti.");
                }
            }
        });

        Button btnIndietro = creaPulsanteIndietro("Indietro", onTornaAllaMappa);
        HBox boxAzioni = new HBox(15, btnSalva, btnIndietro);
        boxAzioni.setAlignment(Pos.CENTER);

        VBox layout = new VBox(25, new Label("INSERISCI NUOVA PRENOTAZIONE"), grid, boxAzioni);
        layout.setAlignment(Pos.CENTER); layout.setPadding(new Insets(30));
        stage.setScene(new Scene(layout, stage.getScene().getWidth(), stage.getScene().getHeight()));
    }

    private void mostraMiniMappaDisponibili(LocalDate dataInizio, LocalDate dataFine, TextField targetField) {
        if (dataInizio == null || dataFine == null || dataInizio.isAfter(dataFine)) {
            mostraMessaggioEsito(false, "", "Seleziona prima un intervallo di date valido.");
            return;
        }

        List<Integer> ombrelloniLiberi = onRichiediOmbrelloniDisponibili != null
                ? onRichiediOmbrelloniDisponibili.apply(dataInizio, dataFine) : new ArrayList<>();

        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Seleziona Ombrelloni Disponibili");

        GridPane gridMiniMappa = new GridPane();
        gridMiniMappa.setAlignment(Pos.CENTER); gridMiniMappa.setHgap(5); gridMiniMappa.setVgap(5); gridMiniMappa.setPadding(new Insets(15));

        List<Integer> ombrelloniSelezionati = new ArrayList<>();
        int r = 0, c = 0;
        for (int i = 1; i <= 100; i++) {
            Button btn = new Button(String.valueOf(i));
            btn.setPrefSize(45, 35);
            final int numOmb = i;

            if (ombrelloniLiberi.contains(numOmb)) {
                btn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");
                btn.setOnAction(e -> {
                    if (ombrelloniSelezionati.contains(numOmb)) {
                        ombrelloniSelezionati.remove(Integer.valueOf(numOmb));
                        btn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");
                    } else {
                        ombrelloniSelezionati.add(numOmb);
                        btn.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-weight: bold;");
                    }
                });
            } else {
                btn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
                btn.setDisable(true);
            }
            gridMiniMappa.add(btn, c, r);
            if (++c >= 10) { c = 0; r++; }
        }

        Button btnConferma = new Button("Conferma Selezione");
        btnConferma.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");
        btnConferma.setOnAction(e -> {
            targetField.setText(ombrelloniSelezionati.stream().map(String::valueOf).collect(Collectors.joining(", ")));
            popup.close();
        });

        VBox layout = new VBox(15, new Label("Verdi = Disponibili | Rossi = Occupati"), new ScrollPane(gridMiniMappa), btnConferma);
        layout.setAlignment(Pos.CENTER); layout.setPadding(new Insets(15));
        popup.setScene(new Scene(layout, 558, 550));
        popup.showAndWait();
    }

    @SuppressWarnings("unchecked")
    public void mostraSelezionatorePrenotazione(TextField targetField) {
        if (onRichiediStoricoPrenotazioniList == null) {
            mostraMessaggioEsito(false, "", "Impossibile recuperare lo storico delle prenotazioni.");
            return;
        }
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Seleziona Prenotazione (dallo Storico)");

        TableView<StoricoPrenotazione> tabella = new TableView<>();
        TableColumn<StoricoPrenotazione, Integer> colCod = new TableColumn<>("Codice");
        colCod.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().codPrenotazione()));
        TableColumn<StoricoPrenotazione, String> colCf = new TableColumn<>("CF Cliente");
        colCf.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().cf()));
        TableColumn<StoricoPrenotazione, String> colNome = new TableColumn<>("Nome e Cognome");
        colNome.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().nome() + " " + data.getValue().cognome()));
        TableColumn<StoricoPrenotazione, LocalDate> colInizio = new TableColumn<>("Inizio");
        colInizio.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().dataInizio()));
        TableColumn<StoricoPrenotazione, LocalDate> colFine = new TableColumn<>("Fine");
        colFine.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().dataFine()));
        TableColumn<StoricoPrenotazione, Integer> colPrezzo = new TableColumn<>("Prezzo (€)");
        colPrezzo.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().prezzoTotale()));

        tabella.getColumns().addAll(colCod, colCf, colNome, colInizio, colFine, colPrezzo);
        tabella.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        List<StoricoPrenotazione> storico = onRichiediStoricoPrenotazioniList.get();
        tabella.setItems(FXCollections.observableArrayList(storico != null ? storico : new ArrayList<>()));

        tabella.setRowFactory(tv -> {
            TableRow<StoricoPrenotazione> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    targetField.setText(String.valueOf(row.getItem().codPrenotazione()));
                    popup.close();
                }
            });
            return row;
        });

        VBox layout = new VBox(10, new Label("Fai doppio clic sulla prenotazione per selezionarla:"), tabella);
        layout.setPadding(new Insets(15));
        popup.setScene(new Scene(layout, 600, 400));
        popup.showAndWait();
    }

    private void mostraFormModificaOmbrellone() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Modifica Ombrellone in Prenotazione");
        GridPane grid = new GridPane(); grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20));

        TextField txtCodPren = new TextField(); txtCodPren.setPromptText("Codice Prenotazione");
        Button btnCercaPren = new Button("🔍 Cerca");
        btnCercaPren.setOnAction(e -> mostraSelezionatorePrenotazione(txtCodPren));
        HBox boxPren = new HBox(8, txtCodPren, btnCercaPren);

        TextField txtVecchioOmb = new TextField(); txtVecchioOmb.setPromptText("Ombrellone Attuale");
        TextField txtNuovoOmb = new TextField(); txtNuovoOmb.setPromptText("Nuovo Ombrellone");

        grid.add(new Label("Cod. Prenotazione:"), 0, 0); grid.add(boxPren, 1, 0);
        grid.add(new Label("Ombrellone Attuale:"), 0, 1); grid.add(txtVecchioOmb, 1, 1);
        grid.add(new Label("Nuovo Ombrellone:"), 0, 2); grid.add(txtNuovoOmb, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(res -> {
            if (res == ButtonType.OK && onModificaOmbrelloneAction != null) {
                try {
                    onModificaOmbrelloneAction.onModifica(Integer.parseInt(txtCodPren.getText().trim()),
                            Integer.parseInt(txtVecchioOmb.getText().trim()), Integer.parseInt(txtNuovoOmb.getText().trim()));
                } catch (Exception ex) {
                    mostraMessaggioEsito(false, "", "Inserisci valori numerici validi.");
                }
            }
        });
    }

    private void mostraFormEliminaPrenotazione() {
        VBox layout = new VBox(15); layout.setAlignment(Pos.CENTER);
        TextField txtCod = new TextField(); txtCod.setMaxWidth(200);
        Button btnCerca = new Button("🔍 Cerca Prenotazione");
        btnCerca.setOnAction(e -> mostraSelezionatorePrenotazione(txtCod));

        Button btnElimina = new Button("Elimina Definitivamente");
        btnElimina.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        btnElimina.setOnAction(e -> {
            try {
                if (onEliminaPrenotazioneSpiaggiaAction != null) {
                    onEliminaPrenotazioneSpiaggiaAction.accept(Integer.parseInt(txtCod.getText()));
                    onTornaAllaMappa.run();
                }
            } catch (NumberFormatException ex) {
                mostraMessaggioEsito(false, "", "Il codice deve essere numerico.");
            }
        });

        Button btnIndietro = new Button("Annulla");
        btnIndietro.setOnAction(e -> stage.setScene(scenaMenuOmbrelloni));

        HBox boxAzioni = new HBox(10, btnElimina, btnIndietro);
        boxAzioni.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(new Label("Codice Numerico Prenotazione da eliminare:"), txtCod, btnCerca, boxAzioni);
        stage.setScene(new Scene(layout, stage.getScene().getWidth(), stage.getScene().getHeight()));
    }

    private void mostraFormModificaAllestimentoGiornaliero() {
        GridPane grid = new GridPane(); grid.setVgap(12); grid.setHgap(15); grid.setAlignment(Pos.CENTER);
        TextField txtCodPren = new TextField(), txtNumOmb = new TextField();
        DatePicker dpData = new DatePicker(LocalDate.now());
        Button btnCercaPren = new Button("🔍 Cerca");
        btnCercaPren.setOnAction(e -> mostraSelezionatorePrenotazione(txtCodPren));
        HBox boxPren = new HBox(8, txtCodPren, btnCercaPren);

        Spinner<Integer> spnLettini = new Spinner<>(0, 10, 0), spnSdraio = new Spinner<>(0, 10, 0), spnSedie = new Spinner<>(0, 10, 0);
        spnLettini.setPrefWidth(70); spnSdraio.setPrefWidth(70); spnSedie.setPrefWidth(70);

        Button btnCarica = new Button("🔄 Cerca Allestimento Attuale");
        btnCarica.setOnAction(e -> {
            try {
                if (onRichiediAllestimentiGiorno != null) {
                    List<Allestimento> lista = onRichiediAllestimentiGiorno.onRichiedi(
                            Integer.parseInt(txtCodPren.getText().trim()), Integer.parseInt(txtNumOmb.getText().trim()), dpData.getValue());
                    int l = 0, s = 0, sd = 0;
                    for (Allestimento a : lista) {
                        if ("LET".equalsIgnoreCase(a.getCodSeduta())) l = a.getQuantita();
                        if ("SDR".equalsIgnoreCase(a.getCodSeduta())) s = a.getQuantita();
                        if ("SED".equalsIgnoreCase(a.getCodSeduta())) sd = a.getQuantita();
                    }
                    spnLettini.getValueFactory().setValue(l);
                    spnSdraio.getValueFactory().setValue(s);
                    spnSedie.getValueFactory().setValue(sd);
                }
            } catch (Exception ex) {
                mostraMessaggioEsito(false, "", "Inserisci codice prenotazione e numero ombrellone validi.");
            }
        });

        HBox boxAllestimenti = new HBox(10, new Label("Lettini:"), spnLettini, new Label("Sdraio:"), spnSdraio, new Label("Sedie:"), spnSedie);
        boxAllestimenti.setAlignment(Pos.CENTER_LEFT);

        grid.add(new Label("Cod. Prenotazione:"), 0, 0); grid.add(boxPren, 1, 0);
        grid.add(new Label("Num. Ombrellone:"), 0, 1); grid.add(txtNumOmb, 1, 1);
        grid.add(new Label("Data Riferimento:"), 0, 2); grid.add(dpData, 1, 2);
        grid.add(btnCarica, 1, 3);
        grid.add(new Label("Nuovo Allestimento:"), 0, 4); grid.add(boxAllestimenti, 1, 4);

        Button btnSalva = new Button("Salva Modifica Giorno");
        btnSalva.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");
        btnSalva.setOnAction(e -> {
            try {
                if (onModificaAllestimentoGiornaliero != null) {
                    onModificaAllestimentoGiornaliero.onModifica(Integer.parseInt(txtCodPren.getText().trim()),
                            Integer.parseInt(txtNumOmb.getText().trim()), dpData.getValue(), spnLettini.getValue(), spnSdraio.getValue(), spnSedie.getValue());
                    onTornaAllaMappa.run();
                }
            } catch (Exception ex) {
                mostraMessaggioEsito(false, "", "Verifica i parametri inseriti.");
            }
        });

        Button btnIndietro = new Button("Indietro");
        btnIndietro.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        btnIndietro.setOnAction(e -> stage.setScene(scenaMenuOmbrelloni));

        HBox boxAzioni = new HBox(15, btnSalva, btnIndietro);
        boxAzioni.setAlignment(Pos.CENTER);

        VBox layout = new VBox(20, new Label("MODIFICA ALLESTIMENTO SINGOLO GIORNO"), grid, boxAzioni);
        layout.setAlignment(Pos.CENTER); layout.setPadding(new Insets(25));
        stage.setScene(new Scene(layout, stage.getScene().getWidth(), stage.getScene().getHeight()));
    }

    @SuppressWarnings("unchecked")
    public void mostraStoricoPrenotazioni(List<StoricoPrenotazione> storico) {
        TableView<StoricoPrenotazione> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<StoricoPrenotazione, Integer> colCod = new TableColumn<>("Cod Prenotazione");
        colCod.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().codPrenotazione()));
        TableColumn<StoricoPrenotazione, LocalDate> colInizio = new TableColumn<>("Data Inizio");
        colInizio.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().dataInizio()));
        TableColumn<StoricoPrenotazione, LocalDate> colFine = new TableColumn<>("Data Fine");
        colFine.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().dataFine()));
        TableColumn<StoricoPrenotazione, String> colCf = new TableColumn<>("CF Cliente");
        colCf.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().cf()));
        TableColumn<StoricoPrenotazione, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().nome()));
        TableColumn<StoricoPrenotazione, String> colCognome = new TableColumn<>("Cognome");
        colCognome.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().cognome()));
        TableColumn<StoricoPrenotazione, Integer> colPrezzo = new TableColumn<>("Prezzo Tot (€)");
        colPrezzo.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().prezzoTotale()));

        table.getColumns().addAll(colCod, colInizio, colFine, colCf, colNome, colCognome, colPrezzo);
        table.setItems(FXCollections.observableArrayList(storico));

        Button btnIndietro = new Button("Indietro");
        btnIndietro.setOnAction(e -> stage.setScene(scenaMenuOmbrelloni));

        VBox layout = new VBox(15, new Label("STORICO PRENOTAZIONI SPIAGGIA"), table, btnIndietro);
        layout.setAlignment(Pos.CENTER); layout.setPadding(new Insets(20));
        stage.setScene(new Scene(layout, stage.getScene().getWidth(), stage.getScene().getHeight()));
    }

    @SuppressWarnings("unchecked")
    public void mostraPrenotazioniGiornaliere(List<PrenotazioneGiornaliera> storico) {
        TableView<PrenotazioneGiornaliera> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<PrenotazioneGiornaliera, Integer> colCod = new TableColumn<>("Cod Prenotazione");
        colCod.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCodPrenotazione()));
        TableColumn<PrenotazioneGiornaliera, Integer> colNum = new TableColumn<>("Numero");
        colNum.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getNumero()));
        TableColumn<PrenotazioneGiornaliera, LocalDate> colInizio = new TableColumn<>("Data Inizio");
        colInizio.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDataRiferimento()));

        table.getColumns().addAll(colCod, colNum, colInizio);
        table.setItems(FXCollections.observableArrayList(storico));

        Button btnIndietro = new Button("Indietro");
        btnIndietro.setOnAction(e -> stage.setScene(scenaMenuOmbrelloni));

        VBox layout = new VBox(15, new Label("PRENOTAZIONI GIORNALIERE"), table, btnIndietro);
        layout.setAlignment(Pos.CENTER); layout.setPadding(new Insets(20));
        stage.setScene(new Scene(layout, stage.getScene().getWidth(), stage.getScene().getHeight()));
    }
}