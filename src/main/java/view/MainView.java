package view;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import common.Pair;
import common.StoricoNoleggio;
import common.StoricoPrenotazione;
import dao.MappaDAO;
import dao.ZonaDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Dipendente;
import model.Prenotazione;
import model.PrenotazioneCampo;

public class MainView {

    private final Stage stage;
    private GridPane gridOmbrelloni;
    
    // --- SCENE PER IL SINGLE PAGE APP ---
    private Scene scenaPrincipale;
    private Scene scenaMenuAttrezzature;
    
    // Variabile per salvare lo storico aggiornato
    private List<StoricoNoleggio> listaStoricoNoleggiAttuali = new ArrayList<>();

    // --- CALLBACK / HANDLER PER IL CONTROLLER ---
    private Consumer<Integer> onCellaOmbrelloneClicked;
    private Consumer<LocalDate> onCambioDataMappa;
    private OnSalvaClienteListener onSalvaClienteAction;
    private BiConsumer<String, Integer> onAssegnaGruppoCliente;
    private OnCreaPrenotazioneListener onCreaPrenotazioneAction;
    private OnCreaNoleggioListener onCreaNoleggioAction;
    private Consumer<String> onEliminaNoleggioAction;
    private BiConsumer<LocalDate, LocalTime> onVerificaCampiAction;
    private OnCreaPrenotazioneCampoListener onCreaPrenotazioneCampoAction;
    private OnRegistraPagamentoListener onRegistraPagamentoPrenotazione;
    private Runnable onRichiediStatisticheDipendenti;
    private BiConsumer<LocalDate, LocalDate> onRichiediOccupazioneZone;
    private Consumer<LocalDate> onGeneraReportGiornaliero;

    @FunctionalInterface
    public interface OnSalvaClienteListener {
        void onSalva(String cf, String nome, String cognome, String email, String telefono, Integer codHotel);
    }
    @FunctionalInterface
    public interface OnCreaPrenotazioneListener {
        void onCrea(String codPrenotazione, LocalDate dataInizio, LocalDate dataFine, int codDipendente, String cf);
    }
    @FunctionalInterface
    public interface OnCreaNoleggioListener {
        void onCrea(LocalDate data, LocalTime oraInizio, int durata, String cf, String codDipendente, String codAttrezzatura);
    }
    @FunctionalInterface
    public interface OnCreaPrenotazioneCampoListener {
        void onCrea(String codPren, LocalTime oraInizio, LocalTime oraFine, String codCampo, String cf, String codDip);
    }
    @FunctionalInterface
    public interface OnRegistraPagamentoListener {
        void onRegistra(String codPag, int importo, LocalDate data, String metodo, String codPren);
    }

    public MainView(Stage stage) {
        this.stage = stage;
    }

    // --- SETTER ---
    public void setOnCellaOmbrelloneClicked(Consumer<Integer> listener) { this.onCellaOmbrelloneClicked = listener; }
    public void setOnCambioDataMappa(Consumer<LocalDate> listener) { this.onCambioDataMappa = listener; }
    public void setOnSalvaClienteAction(OnSalvaClienteListener listener) { this.onSalvaClienteAction = listener; }
    public void setOnAssegnaGruppoCliente(BiConsumer<String, Integer> listener) { this.onAssegnaGruppoCliente = listener; }
    public void setOnCreaPrenotazioneAction(OnCreaPrenotazioneListener listener) { this.onCreaPrenotazioneAction = listener; }
    public void setOnCreaNoleggioAction(OnCreaNoleggioListener listener) { this.onCreaNoleggioAction = listener; }
    public void setOnEliminaNoleggioAction(Consumer<String> listener) { this.onEliminaNoleggioAction = listener; }
    public void setOnVerificaCampiAction(BiConsumer<LocalDate, LocalTime> listener) { this.onVerificaCampiAction = listener; }
    public void setOnCreaPrenotazioneCampoAction(OnCreaPrenotazioneCampoListener listener) { this.onCreaPrenotazioneCampoAction = listener; }
    public void setOnRegistraPagamentoPrenotazione(OnRegistraPagamentoListener listener) { this.onRegistraPagamentoPrenotazione = listener; }
    public void setOnRichiediStatisticheDipendenti(Runnable listener) { this.onRichiediStatisticheDipendenti = listener; }
    public void setOnRichiediOccupazioneZone(BiConsumer<LocalDate, LocalDate> listener) { this.onRichiediOccupazioneZone = listener; }
    public void setOnGeneraReportGiornaliero(Consumer<LocalDate> listener) { this.onGeneraReportGiornaliero = listener; }

    public void mostraFinestra() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f8f9fa;");
        root.setPadding(new Insets(15));

        Node mappaSpiaggia = creaMappaSpiaggia();
        ScrollPane scrollPane = new ScrollPane(mappaSpiaggia);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        root.setCenter(scrollPane);

        root.setRight(creaPannelloDestro());

        this.scenaPrincipale = new Scene(root, 900, 600);
        
        stage.setTitle("Mappa Stabilimento Balneare - Lido");
        stage.setScene(scenaPrincipale);
        stage.setMinWidth(800);
        stage.setMinHeight(500);
        stage.show();
    }

    private Node creaMappaSpiaggia() {
        HBox containerMappa = new HBox();
        containerMappa.setSpacing(15.0);
        containerMappa.setAlignment(Pos.CENTER);
        containerMappa.setPadding(new Insets(5));

        gridOmbrelloni = new GridPane();
        gridOmbrelloni.setAlignment(Pos.CENTER);
        gridOmbrelloni.setHgap(5);
        gridOmbrelloni.setVgap(5);

        containerMappa.getChildren().add(gridOmbrelloni);
        return containerMappa;
    }

    public void aggiornaGrigliaSpiaggia(List<MappaDAO.MappaOmbrelloneInfo> infoCelle) {
        gridOmbrelloni.getChildren().clear();
        int colonne = 10;
        int r = 0;
        int c = 0;

        for (MappaDAO.MappaOmbrelloneInfo cella : infoCelle) {
            Button btnOmbrellone = new Button(String.valueOf(cella.getNumero()));
            btnOmbrellone.setPrefSize(52, 38);
            btnOmbrellone.setFont(Font.font(10));
            
            impostaStatoCell(btnOmbrellone, cella.isOccupato());
            btnOmbrellone.setOnAction(e -> {
                if (onCellaOmbrelloneClicked != null) onCellaOmbrelloneClicked.accept(cella.getNumero());
            });

            gridOmbrelloni.add(btnOmbrellone, c, r);
            c++;
            if (c >= colonne) { c = 0; r++; }
        }
    }

    private void impostaStatoCell(Button button, boolean isOccupato) {
        button.setStyle(isOccupato ? 
            "-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 4;" : 
            "-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 4;");
    }

    // =====================================================================
    // --- GESTIONE VISTE E SCENE ---
    // =====================================================================

    private void cambiaScenaAttrezzature() {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(40));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #f8f9fa;");

        Label titolo = new Label("GESTIONE ATTREZZATURE");
        titolo.setFont(new Font("System Bold", 28));

        Button btnAggiungi = creaPulsanteSezione("Aggiungi Noleggio", 50);
        Button btnStorico = creaPulsanteSezione("Storico Noleggi", 50);
        Button btnElimina = creaPulsanteSezione("Elimina Noleggio", 50);
        
        Button btnIndietro = new Button("⬅ Torna alla Mappa");
        btnIndietro.setPrefSize(160, 40);
        btnIndietro.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");

        btnAggiungi.setOnAction(e -> mostraFormAggiungiNoleggio());
        btnStorico.setOnAction(e -> mostraStoricoNoleggi());
        btnElimina.setOnAction(e -> mostraFormEliminaNoleggio());
        btnIndietro.setOnAction(e -> stage.setScene(scenaPrincipale));

        layout.getChildren().addAll(titolo, btnAggiungi, btnStorico, btnElimina, btnIndietro);
        
        this.scenaMenuAttrezzature = new Scene(layout, stage.getScene().getWidth(), stage.getScene().getHeight());
        stage.setScene(scenaMenuAttrezzature);
    }

    // --- 1. VISTA AGGIUNGI NOLEGGIO ---
    private void mostraFormAggiungiNoleggio() {
        GridPane grid = new GridPane();
        grid.setVgap(10); grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);

        DatePicker datePicker = new DatePicker(LocalDate.now());
        TextField txtOra = new TextField("10:00"); 
        TextField txtDurata = new TextField();
        TextField txtCf = new TextField();
        TextField txtDipendente = new TextField();
        
        ComboBox<String> cmbAttrezzatura = new ComboBox<>();
        cmbAttrezzatura.getItems().addAll("PED", "CAN", "SUP");
        cmbAttrezzatura.setValue("PED");

        grid.add(new Label("Data Noleggio:"), 0, 0);   grid.add(datePicker, 1, 0);
        grid.add(new Label("Ora Inizio (HH:mm):"), 0, 1); grid.add(txtOra, 1, 1);
        grid.add(new Label("Durata (Ore):"), 0, 2);    grid.add(txtDurata, 1, 2);
        grid.add(new Label("CF Cliente:"), 0, 3);      grid.add(txtCf, 1, 3);
        grid.add(new Label("Cod. Dipendente:"), 0, 4); grid.add(txtDipendente, 1, 4);
        grid.add(new Label("Attrezzatura:"), 0, 5);    grid.add(cmbAttrezzatura, 1, 5);

        Button btnSalva = new Button("Salva Noleggio");
        btnSalva.setOnAction(e -> {
            try {
                if(onCreaNoleggioAction != null) {
                    onCreaNoleggioAction.onCrea(
                        datePicker.getValue(), LocalTime.parse(txtOra.getText()),
                        Integer.parseInt(txtDurata.getText()), txtCf.getText(), 
                        txtDipendente.getText(), cmbAttrezzatura.getValue()
                    );
                }
            } catch (Exception ex) {
                mostraMessaggioEsito(false, "", "Formato dati non valido (es. durata o ora).");
            }
        });

        Button btnAnnulla = new Button("Indietro");
        btnAnnulla.setOnAction(e -> stage.setScene(scenaMenuAttrezzature));

        HBox buttons = new HBox(10, btnSalva, btnAnnulla);
        buttons.setAlignment(Pos.CENTER);
        
        VBox layout = new VBox(20, new Label("INSERISCI NUOVO NOLEGGIO"), grid, buttons);
        layout.setAlignment(Pos.CENTER);
        stage.setScene(new Scene(layout, stage.getScene().getWidth(), stage.getScene().getHeight()));
    }

    // --- 2. VISTA STORICO NOLEGGI (AGGIORNATA CON LA LISTA FRESCA) ---
    private void mostraStoricoNoleggi() {
        TableView<StoricoNoleggio> table = new TableView<>();

        TableColumn<StoricoNoleggio, String> colCf = new TableColumn<>("CF");
        colCf.setCellValueFactory(new PropertyValueFactory<>("cf"));
        
        TableColumn<StoricoNoleggio, String> colCod = new TableColumn<>("Cod Noleggio");
        colCod.setCellValueFactory(new PropertyValueFactory<>("codNoleggio"));
        
        TableColumn<StoricoNoleggio, LocalDate> colData = new TableColumn<>("Data");
        colData.setCellValueFactory(new PropertyValueFactory<>("dataNoleggio"));

        TableColumn<StoricoNoleggio, Integer> colCosto = new TableColumn<>("Costo Tot (€)");
        colCosto.setCellValueFactory(new PropertyValueFactory<>("costoTotale"));

        table.getColumns().addAll(colCf, colCod, colData, colCosto);
        
        // Riempiamo la tabella con la lista aggiornata
        table.getItems().addAll(listaStoricoNoleggiAttuali);

        Button btnIndietro = new Button("Indietro");
        btnIndietro.setOnAction(e -> stage.setScene(scenaMenuAttrezzature));

        VBox layout = new VBox(15, new Label("STORICO COMPLETO NOLEGGI"), table, btnIndietro);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        
        stage.setScene(new Scene(layout, stage.getScene().getWidth(), stage.getScene().getHeight()));
    }

    // --- 3. VISTA ELIMINA NOLEGGIO ---
    private void mostraFormEliminaNoleggio() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);

        TextField txtCod = new TextField();
        txtCod.setMaxWidth(200);
        
        Button btnElimina = new Button("Elimina Definitivamente");
        btnElimina.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        btnElimina.setOnAction(e -> {
            if (onEliminaNoleggioAction != null) {
                onEliminaNoleggioAction.accept(txtCod.getText());
                stage.setScene(scenaMenuAttrezzature);
            }
        });

        Button btnIndietro = new Button("Annulla");
        btnIndietro.setOnAction(e -> stage.setScene(scenaMenuAttrezzature));

        layout.getChildren().addAll(
            new Label("Inserisci il Codice Numerico del Noleggio da eliminare:"),
            txtCod, btnElimina, btnIndietro
        );

        stage.setScene(new Scene(layout, stage.getScene().getWidth(), stage.getScene().getHeight()));
    }

    // =====================================================================
    // --- METODI DI FEEDBACK E AGGIORNAMENTO UI ---
    // =====================================================================

    public void mostraMessaggioEsito(boolean successo, String msgSuccesso, String msgErrore) {
        Alert alert = new Alert(successo ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(successo ? "Operazione completata" : "Errore");
        alert.setHeaderText(null);
        alert.setContentText(successo ? msgSuccesso : msgErrore);
        alert.showAndWait();
    }

    public void impostaTabellaNoleggi(List<StoricoNoleggio> storico) {
        this.listaStoricoNoleggiAttuali = storico; 
    }

    public void aggiornaTabellaNoleggi(List<StoricoNoleggio> storico) {
        impostaTabellaNoleggi(storico);
    }

    public void mostraDettagliPrenotazioneCella(String codZona, String nomeZona, String codPrenotazione, String clientePrenotato) { }
    public void apriFormNuovaPrenotazionePerCella(int numeroCella) { }
    public void mostraCampiOccupati(List<PrenotazioneCampo> campiOccupati) { }
    public void mostraReportIncassi(int incassiPrenotazioni, int incassiNoleggi) { }
    public void mostraStatisticheDipendenti(Pair<Dipendente, Integer> topPrenotazioni, Pair<Dipendente, Integer> topNoleggi) { }
    public void mostraStatisticheZone(List<ZonaDAO.ZonaOccupazioneInfo> percentuali) { }
    public void impostaTabellaPrenotazioni(List<StoricoPrenotazione> storico) { }
    public void aggiornaTabellaPrenotazioni(List<StoricoPrenotazione> storico) { }
    public void aggiornaListaPrenotazioniNonSaldate(List<Prenotazione> nonSaldate) { }

    private VBox creaPannelloDestro() {
        VBox pannello = new VBox(15);
        pannello.setPadding(new Insets(5, 5, 5, 20));
        pannello.setAlignment(Pos.TOP_CENTER);
        pannello.setPrefWidth(190);

        Button btnAttrezzature = creaPulsanteSezione("ATTREZZATURE", 75);
        btnAttrezzature.setOnAction(e -> cambiaScenaAttrezzature());

        VBox boxCampi = new VBox(8);
        boxCampi.setAlignment(Pos.CENTER);
        
        Label lblCampiTitle = new Label("CAMPI SPORTIVI");
        lblCampiTitle.setFont(new Font("System Bold", 12));

        Button btnCalcetto = creaPulsanteCampo("CAMPO CALCETTO");
        Button btnBeachVolley = creaPulsanteCampo("CAMPO BEACH VOLLEY");
        Button btnBeachTennis = creaPulsanteCampo("CAMPO BEACH TENNIS");
        Button btnBocceSabbia = creaPulsanteCampo("CAMPO BOCCE SABBIA");
        Button btnBocceAsfalto = creaPulsanteCampo("CAMPO BOCCE ASFALTO");
        Button btnStoricoReport = creaPulsanteCampo("STORICO E REPORT");

        btnStoricoReport.setOnAction(e -> {
            if (onGeneraReportGiornaliero != null) onGeneraReportGiornaliero.accept(LocalDate.now());
        });

        boxCampi.getChildren().addAll(lblCampiTitle, btnCalcetto, btnBeachVolley, btnBeachTennis, btnBocceSabbia, btnBocceAsfalto, btnStoricoReport);
        pannello.getChildren().addAll(btnAttrezzature, boxCampi);
        return pannello;
    }

    private Button creaPulsanteSezione(String testo, double altezza) {
        Button btn = new Button(testo);
        btn.setPrefSize(160, altezza);
        btn.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6;");
        return btn;
    }

    private Button creaPulsanteCampo(String testo) {
        Button btn = new Button(testo);
        btn.setPrefSize(160, 38);
        btn.setStyle("-fx-background-color: #ffffff; -fx-border-color: #bdc3c7; -fx-border-radius: 4; -fx-text-fill: #2c3e50; -fx-font-weight: bold;");
        return btn;
    }
}