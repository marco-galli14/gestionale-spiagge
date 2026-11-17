package view;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Cliente;
import view.MainView.OnSalvaClienteListener;

public abstract class BaseView {

    protected final Stage stage;
    protected final Runnable onTornaAllaMappa;

    public BaseView(Stage stage, Runnable onTornaAllaMappa) {
        this.stage = stage;
        this.onTornaAllaMappa = onTornaAllaMappa;
    }

    protected Button creaPulsanteSezione(String testo, double altezza) {
        Button btn = new Button(testo);
        btn.setPrefSize(160, altezza);
        btn.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6;");
        return btn;
    }

    protected Button creaPulsanteIndietro(String testo, Runnable azione) {
        Button btn = new Button(testo);
        btn.setPrefSize(180, 38);
        btn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        btn.setOnAction(e -> azione.run());
        return btn;
    }

    public void mostraMessaggioEsito(boolean successo, String msgSuccesso, String msgErrore) {
        Alert alert = new Alert(successo ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(successo ? "Operazione completata" : "Errore");
        alert.setHeaderText(null);
        alert.setContentText(successo ? msgSuccesso : msgErrore);
        alert.showAndWait();
    }

    @SuppressWarnings("unchecked")
    public void mostraSelezionatoreCliente(TextField targetField, Supplier<List<Cliente>> supplierClienti) {
        if (supplierClienti == null) {
            mostraMessaggioEsito(false, "", "Impossibile recuperare la lista dei clienti.");
            return;
        }
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Seleziona Cliente");
        TextField txtFiltro = new TextField();
        txtFiltro.setPromptText("Filtra per Nome, Cognome o CF...");

        TableView<Cliente> tabella = new TableView<>();
        TableColumn<Cliente, String> colCf = new TableColumn<>("CF");
        colCf.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCf()));
        TableColumn<Cliente, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNome()));
        TableColumn<Cliente, String> colCognome = new TableColumn<>("Cognome");
        colCognome.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCognome()));
        tabella.getColumns().addAll(colCf, colNome, colCognome);
        tabella.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        List<Cliente> clienti = supplierClienti.get();
        ObservableList<Cliente> masterData = FXCollections.observableArrayList(clienti != null ? clienti : new ArrayList<>());
        FilteredList<Cliente> filteredData = new FilteredList<>(masterData, p -> true);

        txtFiltro.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredData.setPredicate(cliente -> {
                if (newVal == null || newVal.isEmpty()) return true;
                String filter = newVal.toLowerCase();
                return (cliente.getCf() != null && cliente.getCf().toLowerCase().contains(filter))
                        || (cliente.getNome() != null && cliente.getNome().toLowerCase().contains(filter))
                        || (cliente.getCognome() != null && cliente.getCognome().toLowerCase().contains(filter));
            });
        });

        tabella.setItems(filteredData);
        tabella.setRowFactory(tv -> {
            TableRow<Cliente> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    targetField.setText(row.getItem().getCf());
                    popup.close();
                }
            });
            return row;
        });

        VBox layout = new VBox(10, new Label("Cerca e fai doppio clic sul cliente per selezionarlo:"), txtFiltro, tabella);
        layout.setPadding(new Insets(15));
        popup.setScene(new Scene(layout, 480, 400));
        popup.showAndWait();
    }

    public void mostraFormNuovoClienteRapido(TextField targetField, OnSalvaClienteListener onSalvaClienteAction) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Registra Nuovo Cliente");
        GridPane grid = new GridPane();
        grid.setVgap(10); grid.setHgap(10); grid.setPadding(new Insets(20));

        TextField txtCf = new TextField(), txtNome = new TextField(), txtCognome = new TextField();
        TextField txtEmail = new TextField(), txtTelefono = new TextField(), txtCodHotel = new TextField(), txtIdGruppo = new TextField();

        grid.add(new Label("Codice Fiscale:"), 0, 0); grid.add(txtCf, 1, 0);
        grid.add(new Label("Nome:"), 0, 1); grid.add(txtNome, 1, 1);
        grid.add(new Label("Cognome:"), 0, 2); grid.add(txtCognome, 1, 2);
        grid.add(new Label("Email:"), 0, 3); grid.add(txtEmail, 1, 3);
        grid.add(new Label("Telefono:"), 0, 4); grid.add(txtTelefono, 1, 4);
        grid.add(new Label("Cod. Hotel (opzionale):"), 0, 5); grid.add(txtCodHotel, 1, 5);
        grid.add(new Label("ID Gruppo (opzionale):"), 0, 6); grid.add(txtIdGruppo, 1, 6);

        Button btnSalva = new Button("Salva Cliente");
        btnSalva.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");
        btnSalva.setOnAction(e -> {
            if (txtCf.getText().trim().isEmpty() || txtNome.getText().trim().isEmpty() || txtCognome.getText().trim().isEmpty()) {
                mostraMessaggioEsito(false, "", "CF, Nome e Cognome sono obbligatori.");
                return;
            }
            try {
                Integer codHotel = txtCodHotel.getText().trim().isEmpty() ? null : Integer.parseInt(txtCodHotel.getText().trim());
                Integer idGruppo = txtIdGruppo.getText().trim().isEmpty() ? null : Integer.parseInt(txtIdGruppo.getText().trim());
                if (onSalvaClienteAction != null) {
                    onSalvaClienteAction.onSalva(txtCf.getText().trim(), txtNome.getText().trim(), txtCognome.getText().trim(),
                            txtEmail.getText().trim(), txtTelefono.getText().trim(), codHotel, idGruppo);
                    if (targetField != null) targetField.setText(txtCf.getText().trim());
                    popup.close();
                }
            } catch (NumberFormatException ex) {
                mostraMessaggioEsito(false, "", "Codice Hotel e ID Gruppo devono essere numeri interi.");
            }
        });

        Button btnAnnulla = new Button("Annulla");
        btnAnnulla.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        btnAnnulla.setOnAction(e -> popup.close());

        HBox boxBottoni = new HBox(10, btnSalva, btnAnnulla);
        boxBottoni.setAlignment(Pos.CENTER_RIGHT);
        VBox layout = new VBox(15, new Label("REGISTRAZIONE NUOVO CLIENTE"), grid, boxBottoni);
        layout.setPadding(new Insets(15));
        popup.setScene(new Scene(layout, 380, 400));
        popup.showAndWait();
    }
}