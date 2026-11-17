package view;

import java.time.LocalDate;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Cliente;
import view.MainView.*;

public class AmministrazioneView extends BaseView {

    private Scene scenaMenuAltro;

    private OnSalvaClienteListener onSalvaClienteAction;
    private Supplier<List<Cliente>> onRichiediListaClienti;
    private BiConsumer<Integer, Integer> onCreaGruppoCompletoAction;
    private BiConsumer<String, Integer> onAssegnaGruppoClienteAction;
    private OnSalvaHotelListener onSalvaHotelAction;
    private OnSalvaDipendenteCompletoListener onSalvaDipendenteCompletoAction;
    private OnSalvaStagioneListener onSalvaStagioneAction;
    private OnSalvaTariffaZonaListener onSalvaTariffaZonaAction;
    private OnSalvaTariffaNoleggioListener onSalvaTariffaNoleggioAction;

    public AmministrazioneView(Stage stage, Runnable onTornaAllaMappa) {
        super(stage, onTornaAllaMappa);
    }

    public void setOnSalvaClienteAction(OnSalvaClienteListener l) { this.onSalvaClienteAction = l; }
    public void setOnRichiediListaClienti(Supplier<List<Cliente>> l) { this.onRichiediListaClienti = l; }
    public void setOnCreaGruppoCompletoAction(BiConsumer<Integer, Integer> l) { this.onCreaGruppoCompletoAction = l; }
    public void setOnAssegnaGruppoClienteAction(BiConsumer<String, Integer> l) { this.onAssegnaGruppoClienteAction = l; }
    public void setOnSalvaHotelAction(OnSalvaHotelListener l) { this.onSalvaHotelAction = l; }
    public void setOnSalvaDipendenteCompletoAction(OnSalvaDipendenteCompletoListener l) { this.onSalvaDipendenteCompletoAction = l; }
    public void setOnSalvaStagioneAction(OnSalvaStagioneListener l) { this.onSalvaStagioneAction = l; }
    public void setOnSalvaTariffaZonaListener(OnSalvaTariffaZonaListener l) { this.onSalvaTariffaZonaAction = l; }
    public void setOnSalvaTariffaNoleggioListener(OnSalvaTariffaNoleggioListener l) { this.onSalvaTariffaNoleggioAction = l; }

    public void mostraMenu() {
        Label titolo = new Label("MENU CONFIGURAZIONE E AMMINISTRAZIONE");
        titolo.setFont(new Font("System Bold", 22));

        Button btnCliente = creaPulsanteSezione("Registra Nuovo Cliente", 40);
        Button btnGruppo = creaPulsanteSezione("Crea Nuovo Gruppo", 40);
        Button btnAssegnaGruppo = creaPulsanteSezione("Assegna Gruppo a Cliente", 40);
        Button btnHotel = creaPulsanteSezione("Aggiungi Hotel Convenzionato", 40);
        Button btnDipendente = creaPulsanteSezione("Aggiungi Dipendente", 40);
        Button btnStagione = creaPulsanteSezione("Registra Nuova Stagione", 40);
        Button btnTariffaZona = creaPulsanteSezione("Registra Tariffa Zona", 40);
        Button btnTariffaNol = creaPulsanteSezione("Registra Tariffa Noleggio", 40);
        Button btnIndietro = creaPulsanteIndietro("⬅ Torna alla Mappa", onTornaAllaMappa);

        btnCliente.setOnAction(e -> mostraFormNuovoClienteRapido(null, onSalvaClienteAction));
        btnGruppo.setOnAction(e -> mostraFormCreaGruppo());
        btnAssegnaGruppo.setOnAction(e -> mostraFormAssegnaGruppo());
        btnHotel.setOnAction(e -> mostraFormNuovoHotel());
        btnDipendente.setOnAction(e -> mostraFormNuovoDipendente());
        btnStagione.setOnAction(e -> mostraFormNuovaStagione());
        btnTariffaZona.setOnAction(e -> mostraFormTariffaZona());
        btnTariffaNol.setOnAction(e -> mostraFormTariffaNoleggio());

        VBox contentBox = new VBox(10, titolo, btnCliente, btnGruppo, btnAssegnaGruppo, btnHotel, btnDipendente, btnStagione, btnTariffaZona, btnTariffaNol, btnIndietro);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(30));

        ScrollPane scroll = new ScrollPane(contentBox);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        this.scenaMenuAltro = new Scene(scroll, stage.getScene().getWidth(), stage.getScene().getHeight());
        stage.setScene(this.scenaMenuAltro);
    }

    private void mostraFormCreaGruppo() {
        Dialog<ButtonType> dialog = new Dialog<>(); dialog.setTitle("Crea Nuovo Gruppo");
        GridPane grid = new GridPane(); grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20));
        TextField txtIdGruppo = new TextField(), txtSconto = new TextField();
        grid.add(new Label("ID Gruppo:"), 0, 0); grid.add(txtIdGruppo, 1, 0);
        grid.add(new Label("Sconto Gruppo (%):"), 0, 1); grid.add(txtSconto, 1, 1);
        dialog.getDialogPane().setContent(grid); dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.showAndWait().ifPresent(res -> {
            if (res == ButtonType.OK && onCreaGruppoCompletoAction != null) {
                try {
                    onCreaGruppoCompletoAction.accept(Integer.parseInt(txtIdGruppo.getText().trim()), Integer.parseInt(txtSconto.getText().trim()));
                } catch (Exception ex) {
                    mostraMessaggioEsito(false, "", "ID gruppo e sconto devono essere numeri interi.");
                }
            }
        });
    }

    private void mostraFormAssegnaGruppo() {
        Dialog<ButtonType> dialog = new Dialog<>(); dialog.setTitle("Assegna o Rimuovi Gruppo a Cliente");
        GridPane grid = new GridPane(); grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20));

        TextField txtCf = new TextField(); txtCf.setPromptText("Inserisci o cerca CF");
        Button btnCercaCf = new Button("🔍 Cerca");
        btnCercaCf.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-weight: bold;");
        btnCercaCf.setOnAction(e -> mostraSelezionatoreCliente(txtCf, onRichiediListaClienti));
        HBox boxCf = new HBox(8, txtCf, btnCercaCf);

        TextField txtIdGruppo = new TextField(); txtIdGruppo.setPromptText("ID Gruppo (lascia vuoto per rimuovere)");
        grid.add(new Label("CF Cliente:"), 0, 0); grid.add(boxCf, 1, 0);
        grid.add(new Label("ID Gruppo (o vuoto):"), 0, 1); grid.add(txtIdGruppo, 1, 1);

        dialog.getDialogPane().setContent(grid); dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.showAndWait().ifPresent(res -> {
            if (res == ButtonType.OK && onAssegnaGruppoClienteAction != null) {
                try {
                    String idStr = txtIdGruppo.getText().trim();
                    Integer idGruppo = idStr.isEmpty() ? null : Integer.parseInt(idStr);
                    onAssegnaGruppoClienteAction.accept(txtCf.getText().trim(), idGruppo);
                } catch (Exception ex) {
                    mostraMessaggioEsito(false, "", "Verifica che l'ID gruppo sia un numero intero valido.");
                }
            }
        });
    }

    private void mostraFormNuovoHotel() {
        Dialog<ButtonType> dialog = new Dialog<>(); dialog.setTitle("Nuovo Hotel Convenzionato");
        GridPane grid = new GridPane(); grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20));
        TextField txtCod = new TextField(), txtNome = new TextField(), txtSconto = new TextField();
        grid.add(new Label("Cod. Hotel:"), 0, 0); grid.add(txtCod, 1, 0);
        grid.add(new Label("Nome Hotel:"), 0, 1); grid.add(txtNome, 1, 1);
        grid.add(new Label("Sconto Hotel (%):"), 0, 2); grid.add(txtSconto, 1, 2);
        dialog.getDialogPane().setContent(grid); dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.showAndWait().ifPresent(res -> {
            if (res == ButtonType.OK && onSalvaHotelAction != null) {
                try {
                    onSalvaHotelAction.onSalva(Integer.parseInt(txtCod.getText().trim()), txtNome.getText().trim(), Integer.parseInt(txtSconto.getText().trim()));
                } catch (Exception ex) {
                    mostraMessaggioEsito(false, "", "Codice hotel e sconto devono essere numeri interi.");
                }
            }
        });
    }

    private void mostraFormNuovoDipendente() {
        Dialog<ButtonType> dialog = new Dialog<>(); dialog.setTitle("Registra Dipendente");
        GridPane grid = new GridPane(); grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20));
        TextField txtCod = new TextField(), txtNome = new TextField(), txtCognome = new TextField(), txtUser = new TextField();
        PasswordField txtPwd = new PasswordField(); TextField txtRuolo = new TextField();

        grid.add(new Label("Codice Dipendente:"), 0, 0); grid.add(txtCod, 1, 0);
        grid.add(new Label("Nome:"), 0, 1); grid.add(txtNome, 1, 1);
        grid.add(new Label("Cognome:"), 0, 2); grid.add(txtCognome, 1, 2);
        grid.add(new Label("Username:"), 0, 3); grid.add(txtUser, 1, 3);
        grid.add(new Label("Password:"), 0, 4); grid.add(txtPwd, 1, 4);
        grid.add(new Label("Ruolo:"), 0, 5); grid.add(txtRuolo, 1, 5);

        dialog.getDialogPane().setContent(grid); dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.showAndWait().ifPresent(res -> {
            if (res == ButtonType.OK && onSalvaDipendenteCompletoAction != null) {
                try {
                    onSalvaDipendenteCompletoAction.onSalva(Integer.parseInt(txtCod.getText().trim()), txtNome.getText().trim(),
                            txtCognome.getText().trim(), txtUser.getText().trim(), txtPwd.getText().trim(), txtRuolo.getText().trim());
                } catch (Exception ex) {
                    mostraMessaggioEsito(false, "", "Il codice dipendente deve essere un numero intero.");
                }
            }
        });
    }

    private void mostraFormNuovaStagione() {
        Dialog<ButtonType> dialog = new Dialog<>(); dialog.setTitle("Registra Nuova Stagione");
        GridPane grid = new GridPane(); grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20));
        TextField txtCod = new TextField(); DatePicker dpInizio = new DatePicker(LocalDate.now()), dpFine = new DatePicker(LocalDate.now().plusMonths(3));
        grid.add(new Label("Cod. Stagione (es. Alta26):"), 0, 0); grid.add(txtCod, 1, 0);
        grid.add(new Label("Data Inizio:"), 0, 1); grid.add(dpInizio, 1, 1);
        grid.add(new Label("Data Fine:"), 0, 2); grid.add(dpFine, 1, 2);
        dialog.getDialogPane().setContent(grid); dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.showAndWait().ifPresent(res -> {
            if (res == ButtonType.OK && onSalvaStagioneAction != null) {
                onSalvaStagioneAction.onSalva(txtCod.getText().trim(), dpInizio.getValue(), dpFine.getValue());
            }
        });
    }

    private void mostraFormTariffaZona() {
        Dialog<ButtonType> dialog = new Dialog<>(); dialog.setTitle("Registra Tariffa Zona");
        GridPane grid = new GridPane(); grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20));
        TextField txtZona = new TextField(), txtStagione = new TextField(), txtTariffa = new TextField();
        grid.add(new Label("Cod. Zona (es. 1):"), 0, 0); grid.add(txtZona, 1, 0);
        grid.add(new Label("Cod. Stagione:"), 0, 1); grid.add(txtStagione, 1, 1);
        grid.add(new Label("Tariffa Giornaliera (€):"), 0, 2); grid.add(txtTariffa, 1, 2);
        dialog.getDialogPane().setContent(grid); dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.showAndWait().ifPresent(res -> {
            if (res == ButtonType.OK && onSalvaTariffaZonaAction != null) {
                try {
                    onSalvaTariffaZonaAction.onSalva(txtStagione.getText().trim(), Integer.parseInt(txtZona.getText().trim()), Integer.parseInt(txtTariffa.getText().trim()));
                } catch (Exception ex) {
                    mostraMessaggioEsito(false, "", "Zona e tariffa devono essere numeri interi.");
                }
            }
        });
    }

    private void mostraFormTariffaNoleggio() {
        Dialog<ButtonType> dialog = new Dialog<>(); dialog.setTitle("Registra Tariffa Noleggio");
        GridPane grid = new GridPane(); grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20));
        TextField txtAttrez = new TextField(), txtStagione = new TextField(), txtTariffa = new TextField();
        grid.add(new Label("Cod. Attrezzatura (es. PED):"), 0, 0); grid.add(txtAttrez, 1, 0);
        grid.add(new Label("Cod. Stagione:"), 0, 1); grid.add(txtStagione, 1, 1);
        grid.add(new Label("Tariffa Oraria (€):"), 0, 2); grid.add(txtTariffa, 1, 2);
        dialog.getDialogPane().setContent(grid); dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.showAndWait().ifPresent(res -> {
            if (res == ButtonType.OK && onSalvaTariffaNoleggioAction != null) {
                try {
                    onSalvaTariffaNoleggioAction.onSalva(txtStagione.getText().trim(), txtAttrez.getText().trim(), Integer.parseInt(txtTariffa.getText().trim()));
                } catch (Exception ex) {
                    mostraMessaggioEsito(false, "", "La tariffa deve essere un numero intero.");
                }
            }
        });
    }
}