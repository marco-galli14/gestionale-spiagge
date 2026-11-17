package view;

import java.time.LocalDate;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import common.Pair;
import common.StoricoNoleggio;
import dao.ZonaDAO;
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
import model.Dipendente;
import model.Prenotazione;
import view.MainView.*;

public class ContabilitaStatisticheView extends BaseView {

    private Scene scenaStatistiche;
    private final Supplier<LocalDate> dataSelezionataSupplier;

    private OnRegistraPagamentoListener onRegistraPagamentoPrenotazione;
    private OnRegistraPagamentoNoleggioListener onRegistraPagamentoNoleggio;
    private Consumer<LocalDate> onReportPerDataAction;
    private Runnable onRichiediTopDipendentePrenotazioni;
    private Runnable onRichiediTopDipendenteNoleggi;
    private BiConsumer<LocalDate, LocalDate> onRichiediOccupazioneZone;

    public ContabilitaStatisticheView(Stage stage, Runnable onTornaAllaMappa, Supplier<LocalDate> dataSelezionataSupplier) {
        super(stage, onTornaAllaMappa);
        this.dataSelezionataSupplier = dataSelezionataSupplier;
    }

    public void setOnRegistraPagamentoPrenotazione(OnRegistraPagamentoListener l) { this.onRegistraPagamentoPrenotazione = l; }
    public void setOnRegistraPagamentoNoleggio(OnRegistraPagamentoNoleggioListener l) { this.onRegistraPagamentoNoleggio = l; }
    public void setOnReportPerDataAction(Consumer<LocalDate> l) { this.onReportPerDataAction = l; }
    public void setOnRichiediTopDipendentePrenotazioni(Runnable l) { this.onRichiediTopDipendentePrenotazioni = l; }
    public void setOnRichiediTopDipendenteNoleggi(Runnable l) { this.onRichiediTopDipendenteNoleggi = l; }
    public void setOnRichiediOccupazioneZone(BiConsumer<LocalDate, LocalDate> l) { this.onRichiediOccupazioneZone = l; }

    @SuppressWarnings("unchecked")
    public void mostraCentroContabilita(List<Pair<Prenotazione, Integer>> nonSaldatePren, List<StoricoNoleggio> nonSaldateNol, int incassiPren, int incassiNol) {
        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20)); mainLayout.setAlignment(Pos.TOP_CENTER); mainLayout.setStyle("-fx-background-color: #f8f9fa;");

        Label lblTitle = new Label("CENTRO CONTABILITÀ E PAGAMENTI");
        lblTitle.setFont(new Font("System Bold", 22));

        VBox boxReport = new VBox(8);
        boxReport.setPadding(new Insets(10)); boxReport.setStyle("-fx-border-color: #bdc3c7; -fx-border-radius: 5; -fx-background-color: white;");
        Label lblReportOggi = new Label("Incassi Odierni - Prenotazioni: " + incassiPren + "€ | Noleggi: " + incassiNol + "€ | Totale: " + (incassiPren + incassiNol) + "€");
        lblReportOggi.setFont(new Font("System Bold", 13));

        DatePicker dpReport = new DatePicker(LocalDate.now());
        Button btnCercaReport = new Button("Visualizza Report Data");
        btnCercaReport.setOnAction(e -> {
            if (onReportPerDataAction != null && dpReport.getValue() != null) onReportPerDataAction.accept(dpReport.getValue());
        });
        HBox boxCercaData = new HBox(10, new Label("Seleziona Data:"), dpReport, btnCercaReport);
        boxCercaData.setAlignment(Pos.CENTER_LEFT);
        boxReport.getChildren().addAll(lblReportOggi, boxCercaData);

        ComboBox<String> cmbTipoPagamento = new ComboBox<>();
        cmbTipoPagamento.getItems().addAll("Prenotazioni Spiaggia", "Noleggi Esterni");
        cmbTipoPagamento.setValue("Prenotazioni Spiaggia");
        HBox boxScelta = new HBox(10, new Label("Seleziona Categoria:"), cmbTipoPagamento);
        boxScelta.setAlignment(Pos.CENTER_LEFT);

        StackPane containerTabella = new StackPane();

        TableView<Pair<Prenotazione, Integer>> tblPrenotazioni = new TableView<>();
        tblPrenotazioni.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        TableColumn<Pair<Prenotazione, Integer>, Integer> colCodP = new TableColumn<>("Cod. Pren.");
        colCodP.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().key().getCodPrenotazione()));
        TableColumn<Pair<Prenotazione, Integer>, String> colCfP = new TableColumn<>("CF Cliente");
        colCfP.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().key().getCf()));
        TableColumn<Pair<Prenotazione, Integer>, Integer> colCostoP = new TableColumn<>("Totale (€)");
        colCostoP.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().key().getPrezzoTotale()));
        TableColumn<Pair<Prenotazione, Integer>, Integer> colResiduo = new TableColumn<>("Da Pagare (€)");
        colResiduo.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().value()));
        tblPrenotazioni.getColumns().addAll(colCodP, colCfP, colCostoP, colResiduo);
        tblPrenotazioni.setItems(FXCollections.observableArrayList(nonSaldatePren));

        TableView<StoricoNoleggio> tblNoleggi = new TableView<>();
        tblNoleggi.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        TableColumn<StoricoNoleggio, Integer> colCodN = new TableColumn<>("Cod. Noleggio");
        colCodN.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().codNoleggio()));
        TableColumn<StoricoNoleggio, String> colCfN = new TableColumn<>("CF Cliente");
        colCfN.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().cf()));
        TableColumn<StoricoNoleggio, String> colAttrezN = new TableColumn<>("Attrezzatura");
        colAttrezN.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().codAttrezzatura()));
        TableColumn<StoricoNoleggio, Integer> colCostoN = new TableColumn<>("Totale (€)");
        colCostoN.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().costoTotale()));
        tblNoleggi.getColumns().addAll(colCodN, colCfN, colAttrezN, colCostoN);
        tblNoleggi.setItems(FXCollections.observableArrayList(nonSaldateNol));

        containerTabella.getChildren().add(tblPrenotazioni);

        GridPane gridPagamento = new GridPane();
        gridPagamento.setVgap(8); gridPagamento.setHgap(10); gridPagamento.setAlignment(Pos.CENTER_LEFT);
        TextField txtImporto = new TextField();
        DatePicker dpDataPag = new DatePicker(LocalDate.now());
        ComboBox<String> cmbMetodo = new ComboBox<>();
        cmbMetodo.getItems().addAll("CONTANTI", "CARTA", "POS", "BONIFICO");
        cmbMetodo.setValue("CONTANTI");

        gridPagamento.add(new Label("Importo (€):"), 0, 0); gridPagamento.add(txtImporto, 1, 0);
        gridPagamento.add(new Label("Data Pagamento:"), 0, 1); gridPagamento.add(dpDataPag, 1, 1);
        gridPagamento.add(new Label("Metodo:"), 2, 0); gridPagamento.add(cmbMetodo, 3, 0);

        cmbTipoPagamento.setOnAction(e -> {
            if ("Prenotazioni Spiaggia".equals(cmbTipoPagamento.getValue())) {
                containerTabella.getChildren().setAll(tblPrenotazioni);
                txtImporto.clear();
                txtImporto.setEditable(true);
            } else {
                containerTabella.getChildren().setAll(tblNoleggi);
                txtImporto.clear();
                txtImporto.setEditable(false);
            }
        });

        tblPrenotazioni.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) txtImporto.setText(String.valueOf(newSel.value()));
        });
        tblNoleggi.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) txtImporto.setText(String.valueOf(newSel.costoTotale()));
        });

        Button btnRegistra = new Button("💳 Registra Pagamento");
        btnRegistra.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");
        btnRegistra.setOnAction(e -> {
            if ("Prenotazioni Spiaggia".equals(cmbTipoPagamento.getValue())) {
                Pair<Prenotazione, Integer> sel = tblPrenotazioni.getSelectionModel().getSelectedItem();
                if (sel == null) {
                    mostraMessaggioEsito(false, "", "Seleziona prima una prenotazione dalla tabella.");
                    return;
                }
                try {
                    int importoInserito = Integer.parseInt(txtImporto.getText().trim());
                    int residuo = sel.value();
                    if (importoInserito > residuo) {
                        mostraMessaggioEsito(false, "", "L'importo supera il residuo (" + residuo + "€).");
                        return;
                    }
                    if (onRegistraPagamentoPrenotazione != null) {
                        onRegistraPagamentoPrenotazione.onRegistra(importoInserito, dpDataPag.getValue(), cmbMetodo.getValue(), sel.key().getCodPrenotazione());
                    }
                } catch (Exception ex) {
                    mostraMessaggioEsito(false, "", "Verifica i dati del pagamento inseriti.");
                }
            } else {
                StoricoNoleggio sel = tblNoleggi.getSelectionModel().getSelectedItem();
                if (sel == null) {
                    mostraMessaggioEsito(false, "", "Seleziona prima un noleggio dalla tabella.");
                    return;
                }
                try {
                    int importoInserito = Integer.parseInt(txtImporto.getText().trim());
                    if (importoInserito != sel.costoTotale()) {
                        mostraMessaggioEsito(false, "", "Per i noleggi l'importo deve essere pari al costo totale.");
                        return;
                    }
                    if (onRegistraPagamentoNoleggio != null) {
                        onRegistraPagamentoNoleggio.onRegistraNol(importoInserito, dpDataPag.getValue(), cmbMetodo.getValue(), sel.codNoleggio());
                    }
                } catch (Exception ex) {
                    mostraMessaggioEsito(false, "", "Verifica i dati del pagamento inseriti.");
                }
            }
        });

        Button btnIndietro = creaPulsanteIndietro("⬅ Torna alla Mappa", onTornaAllaMappa);
        HBox boxAzioni = new HBox(15, btnRegistra, btnIndietro);
        boxAzioni.setAlignment(Pos.CENTER);

        mainLayout.getChildren().addAll(lblTitle, boxReport, boxScelta, containerTabella, gridPagamento, boxAzioni);
        ScrollPane scroll = new ScrollPane(mainLayout);
        scroll.setFitToWidth(true);
        stage.setScene(new Scene(scroll, stage.getScene().getWidth(), stage.getScene().getHeight()));
    }

    public void mostraMenuStatistiche() {
        Label titolo = new Label("STATISTICHE");
        titolo.setFont(new Font("System Bold", 22));

        Button btnTopDipPren = creaPulsanteSezione("Dipendente con Più Prenotazioni", 45);
        Button btnTopDipNol = creaPulsanteSezione("Dipendente con Più Noleggi", 45);
        Button btnOccupazioneZone = creaPulsanteSezione("Percentuale Occupazione Spiaggia", 45);
        Button btnIndietro = creaPulsanteIndietro("⬅ Torna alla Mappa", onTornaAllaMappa);

        btnTopDipPren.setOnAction(e -> { if (onRichiediTopDipendentePrenotazioni != null) onRichiediTopDipendentePrenotazioni.run(); });
        btnTopDipNol.setOnAction(e -> { if (onRichiediTopDipendenteNoleggi != null) onRichiediTopDipendenteNoleggi.run(); });
        btnOccupazioneZone.setOnAction(e -> mostraFormOccupazioneZone());

        VBox contentBox = new VBox(15, titolo, btnTopDipPren, btnTopDipNol, btnOccupazioneZone, btnIndietro);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(30));

        ScrollPane scroll = new ScrollPane(contentBox);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        this.scenaStatistiche = new Scene(scroll, stage.getScene().getWidth(), stage.getScene().getHeight());
        stage.setScene(this.scenaStatistiche);
    }

    private void mostraFormOccupazioneZone() {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Occupazione Zone Mensile");

        LocalDate oggi = dataSelezionataSupplier.get();
        DatePicker dpInizio = new DatePicker(oggi.withDayOfMonth(1));
        DatePicker dpFine = new DatePicker(oggi.withDayOfMonth(oggi.lengthOfMonth()));

        Button btnCalcola = new Button("Calcola Occupazione");
        btnCalcola.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");
        btnCalcola.setOnAction(e -> {
            if (dpInizio.getValue() != null && dpFine.getValue() != null && onRichiediOccupazioneZone != null) {
                onRichiediOccupazioneZone.accept(dpInizio.getValue(), dpFine.getValue());
                popup.close();
            }
        });

        GridPane grid = new GridPane(); grid.setVgap(10); grid.setHgap(10); grid.setAlignment(Pos.CENTER);
        grid.add(new Label("Data Inizio:"), 0, 0); grid.add(dpInizio, 1, 0);
        grid.add(new Label("Data Fine:"), 0, 1); grid.add(dpFine, 1, 1);

        VBox layout = new VBox(15, new Label("Seleziona il periodo mensile:"), grid, btnCalcola);
        layout.setAlignment(Pos.CENTER); layout.setPadding(new Insets(20));
        popup.setScene(new Scene(layout, 360, 230));
        popup.showAndWait();
    }

    public void mostraReportIncassi(int incassiPrenotazioni, int incassiNoleggi) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Report Incassi");
        alert.setHeaderText("Dettaglio Incassi per la Data Selezionata");
        alert.setContentText("Incasso Prenotazioni Spiaggia: " + incassiPrenotazioni + " €\n"
                + "Incasso Noleggio Attrezzature: " + incassiNoleggi + " €\n"
                + "Totale Incassato: " + (incassiPrenotazioni + incassiNoleggi) + " €");
        alert.showAndWait();
    }

    public void mostraTopDipendentePrenotazioni(Pair<Dipendente, Integer> topPrenotazioni) {
        String msg = (topPrenotazioni != null && topPrenotazioni.key() != null)
                ? "Nome: " + topPrenotazioni.key().getNome() + " " + topPrenotazioni.key().getCognome()
                + "\nCodice Dipendente: " + topPrenotazioni.key().getCodDipendente()
                + "\nTotale Prenotazioni: " + topPrenotazioni.value()
                : "Nessuna prenotazione trovata.";
        mostraMessaggioEsito(true, msg, "");
    }

    public void mostraTopDipendenteNoleggi(Pair<Dipendente, Integer> topNoleggi) {
        String msg = (topNoleggi != null && topNoleggi.key() != null)
                ? "Nome: " + topNoleggi.key().getNome() + " " + topNoleggi.key().getCognome()
                + "\nCodice Dipendente: " + topNoleggi.key().getCodDipendente()
                + "\nTotale Noleggi: " + topNoleggi.value()
                : "Nessun noleggio trovato.";
        mostraMessaggioEsito(true, msg, "");
    }

    public void mostraStatisticheZone(List<ZonaDAO.ZonaOccupazioneInfo> percentuali) {
        StringBuilder sb = new StringBuilder();
        if (percentuali == null || percentuali.isEmpty()) {
            sb.append("Nessun dato sull'occupazione disponibile per il periodo selezionato.");
        } else {
            for (ZonaDAO.ZonaOccupazioneInfo info : percentuali) {
                sb.append("Zona ").append(info.getCodZona()).append(": ").append(info.getPercentualeOccupazione())
                  .append("% di occupazione (").append(info.getOmbrelloniOccupati()).append(" su ")
                  .append(info.getCapienzaMassima()).append(" presenze max)\n");
            }
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Percentuale Occupazione Zone");
        alert.setHeaderText("Statistiche Occupazione Zone");
        alert.setContentText(sb.toString());
        alert.showAndWait();
    }
}