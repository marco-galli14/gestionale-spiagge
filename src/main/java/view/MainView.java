package view;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Dipendente;
import model.Prenotazione;
import model.PrenotazioneCampo;

public class MainView {

    private final Stage stage;
    private GridPane gridOmbrelloni;
    
    private DatePicker datePickerPrincipale;
    private TextField txtOraPrincipale;
    
    private Scene scenaPrincipale;
    private Scene scenaMenuAttrezzature;
    private Scene scenaMenuCampi;
    private Scene scenaMenuOmbrelloni;
    
    private final Map<String, Button> bottoniCampiMap = new HashMap<>();
    private List<StoricoNoleggio> listaStoricoNoleggiAttuali = new ArrayList<>();

    private Consumer<Integer> onCellaOmbrelloneClicked;
    private BiConsumer<LocalDate, LocalTime> onCambioDataOraMappa;
    private OnSalvaClienteListener onSalvaClienteAction;
    private BiConsumer<String, Integer> onAssegnaGruppoCliente;
    private OnCreaPrenotazioneListener onCreaPrenotazioneAction;
    private OnCreaNoleggioListener onCreaNoleggioAction;
    private Consumer<Integer> onEliminaNoleggioAction;
    private BiConsumer<LocalDate, LocalTime> onVerificaCampiAction;
    private OnCreaPrenotazioneCampoListener onCreaPrenotazioneCampoAction;
    private Consumer<Integer> onEliminaPrenotazioneCampoAction;
    private OnRegistraPagamentoListener onRegistraPagamentoPrenotazione;
    private Runnable onRichiediStatisticheDipendenti;
    private BiConsumer<LocalDate, LocalDate> onRichiediOccupazioneZone;
    private Consumer<LocalDate> onGeneraReportGiornaliero;
    private Runnable onRichiediStoricoPrenotazioni;
    private Consumer<Integer> onEliminaPrenotazioneSpiaggiaAction;

    @FunctionalInterface
    public interface OnSalvaClienteListener {
        void onSalva(String cf, String nome, String cognome, String email, String telefono, Integer codHotel);
    }
    @FunctionalInterface
    public interface OnCreaPrenotazioneListener {
        void onCrea(LocalDate dataInizio, LocalDate dataFine, int codDipendente, String cf);
    }
    @FunctionalInterface
    public interface OnCreaNoleggioListener {
        void onCrea(LocalDate data, LocalTime oraInizio, int durata, String cf, String codDipendente, String codAttrezzatura);
    }
    @FunctionalInterface
    public interface OnCreaPrenotazioneCampoListener {
        void onCrea(LocalDate dataPrenotazione, LocalTime oraInizio, LocalTime oraFine, String codCampo, String cf, String codDip);
    }
    @FunctionalInterface
    public interface OnRegistraPagamentoListener {
        void onRegistra(String codPag, int importo, LocalDate data, String metodo, int codPren);
    }

    public MainView(Stage stage) {
        this.stage = stage;
    }

    public void setOnCellaOmbrelloneClicked(Consumer<Integer> listener) { this.onCellaOmbrelloneClicked = listener; }
    public void setOnCambioDataOraMappa(BiConsumer<LocalDate, LocalTime> listener) { this.onCambioDataOraMappa = listener; }
    public void setOnSalvaClienteAction(OnSalvaClienteListener listener) { this.onSalvaClienteAction = listener; }
    public void setOnAssegnaGruppoCliente(BiConsumer<String, Integer> listener) { this.onAssegnaGruppoCliente = listener; }
    public void setOnCreaPrenotazioneAction(OnCreaPrenotazioneListener listener) { this.onCreaPrenotazioneAction = listener; }
    public void setOnCreaNoleggioAction(OnCreaNoleggioListener listener) { this.onCreaNoleggioAction = listener; }
    public void setOnEliminaNoleggioAction(Consumer<Integer> listener) { this.onEliminaNoleggioAction = listener; }
    public void setOnVerificaCampiAction(BiConsumer<LocalDate, LocalTime> listener) { this.onVerificaCampiAction = listener; }
    public void setOnCreaPrenotazioneCampoAction(OnCreaPrenotazioneCampoListener listener) { this.onCreaPrenotazioneCampoAction = listener; }
    public void setOnEliminaPrenotazioneCampoAction(Consumer<Integer> listener) { this.onEliminaPrenotazioneCampoAction = listener; }
    public void setOnRegistraPagamentoPrenotazione(OnRegistraPagamentoListener listener) { this.onRegistraPagamentoPrenotazione = listener; }
    public void setOnRichiediStatisticheDipendenti(Runnable listener) { this.onRichiediStatisticheDipendenti = listener; }
    public void setOnRichiediOccupazioneZone(BiConsumer<LocalDate, LocalDate> listener) { this.onRichiediOccupazioneZone = listener; }
    public void setOnGeneraReportGiornaliero(Consumer<LocalDate> listener) { this.onGeneraReportGiornaliero = listener; }
    public void setOnRichiediStoricoPrenotazioni(Runnable listener) { this.onRichiediStoricoPrenotazioni = listener; }
    public void setOnEliminaPrenotazioneSpiaggiaAction(Consumer<Integer> listener) { this.onEliminaPrenotazioneSpiaggiaAction = listener; }

    public void mostraFinestra() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f8f9fa;");
        root.setPadding(new Insets(15));

        HBox topBar = creaBarraSelezioneDataOra();
        root.setTop(topBar);

        Node mappaSpiaggia = creaMappaSpiaggia();
        ScrollPane scrollPane = new ScrollPane(mappaSpiaggia);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        root.setCenter(scrollPane);

        root.setRight(creaPannelloDestro());

        this.scenaPrincipale = new Scene(root, 950, 650);
        stage.setTitle("Mappa Stabilimento Balneare - Lido");
        stage.setScene(scenaPrincipale);
        stage.setMinWidth(850);
        stage.setMinHeight(550);
        stage.show();
    }

    private HBox creaBarraSelezioneDataOra() {
        HBox bar = new HBox(10);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(0, 0, 15, 0));

        Label lblData = new Label("Data:");
        lblData.setStyle("-fx-font-weight: bold;");
        
        datePickerPrincipale = new DatePicker(LocalDate.now());
        datePickerPrincipale.setPrefWidth(140);

        Label lblOra = new Label("Ora (HH:mm):");
        lblOra.setStyle("-fx-font-weight: bold;");

        String oraCorrenteFormattata = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        txtOraPrincipale = new TextField(oraCorrenteFormattata);
        txtOraPrincipale.setPrefWidth(80);

        Button btnAggiorna = new Button("Aggiorna Mappa");
        btnAggiorna.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-weight: bold;");
        btnAggiorna.setOnAction(e -> triggerAggiornamentoDataOra());

        datePickerPrincipale.setOnAction(e -> triggerAggiornamentoDataOra());

        bar.getChildren().addAll(lblData, datePickerPrincipale, lblOra, txtOraPrincipale, btnAggiorna);
        return bar;
    }

    private void triggerAggiornamentoDataOra() {
        if (onCambioDataOraMappa != null) {
            try {
                LocalDate data = datePickerPrincipale.getValue();
                LocalTime ora = LocalTime.parse(txtOraPrincipale.getText(), DateTimeFormatter.ofPattern("HH:mm"));
                if (data != null) {
                    onCambioDataOraMappa.accept(data, ora);
                }
            } catch (Exception ex) {
                mostraMessaggioEsito(false, "", "Formato ora non valido (usa il formato HH:mm es. 10:00).");
            }
        }
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

    public void aggiornaStatoCampi(List<String> codiciCampiOccupati) {
        for (Map.Entry<String, Button> entry : bottoniCampiMap.entrySet()) {
            String codCampo = entry.getKey();
            Button btn = entry.getValue();
            boolean occupato = codiciCampiOccupati.contains(codCampo);
            impostaStatoCell(btn, occupato);
        }
    }

    public void cambiaScenaOmbrelloni() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #f8f9fa;");

        Label titolo = new Label("GESTIONE OMBRELLONI E PRENOTAZIONI");
        titolo.setFont(new Font("System Bold", 22));

        Button btnInserisciPren = creaPulsanteSezione("Inserisci Prenotazione", 40);
        Button btnEliminaPren = creaPulsanteSezione("Elimina Prenotazione", 40);
        Button btnStoricoPren = creaPulsanteSezione("Storico Prenotazioni", 40);
        Button btnAddGiornaliera = creaPulsanteSezione("Aggiungi Prenotazione Giornaliera", 40);
        Button btnAggiornaOmb = creaPulsanteSezione("Aggiorna Ombrellone", 40);
        Button btnDelGiornaliera = creaPulsanteSezione("Elimina Prenotazione Giornaliera", 40);
        Button btnAddAllestimento = creaPulsanteSezione("Aggiungi Allestimento", 40);
        Button btnAggiornaAllestimento = creaPulsanteSezione("Aggiorna Allestimento", 40);
        Button btnDelAllestimento = creaPulsanteSezione("Elimina Allestimento", 40);

        Button btnIndietro = new Button("⬅ Torna alla Mappa");
        btnIndietro.setPrefSize(180, 38);
        btnIndietro.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");

        btnInserisciPren.setOnAction(e -> mostraFormInserisciPrenotazione());
        btnEliminaPren.setOnAction(e -> mostraFormEliminaPrenotazione());
        btnStoricoPren.setOnAction(e -> {
            if (onRichiediStoricoPrenotazioni != null) onRichiediStoricoPrenotazioni.run();
        });
        btnAddGiornaliera.setOnAction(e -> mostraFormAggiungiGiornaliera());
        btnAggiornaOmb.setOnAction(e -> mostraFormAggiornaOmbrellone());
        btnDelGiornaliera.setOnAction(e -> mostraFormEliminaGiornaliera());
        btnAddAllestimento.setOnAction(e -> mostraFormAggiungiAllestimento());
        btnAggiornaAllestimento.setOnAction(e -> mostraFormAggiornaAllestimento());
        btnDelAllestimento.setOnAction(e -> mostraFormEliminaAllestimento());
        btnIndietro.setOnAction(e -> stage.setScene(scenaPrincipale));

        VBox contentBox = new VBox(12, titolo, btnInserisciPren, btnEliminaPren, btnStoricoPren, 
            btnAddGiornaliera, btnAggiornaOmb, btnDelGiornaliera, btnAddAllestimento, 
            btnAggiornaAllestimento, btnDelAllestimento, btnIndietro);
        contentBox.setAlignment(Pos.CENTER);

        ScrollPane scroll = new ScrollPane(contentBox);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        this.scenaMenuOmbrelloni = new Scene(scroll, stage.getScene().getWidth(), stage.getScene().getHeight());
        stage.setScene(scenaMenuOmbrelloni);
    }

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

    private void cambiaScenaCampi() {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(40));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #f8f9fa;");

        Label titolo = new Label("GESTIONE CAMPI SPORTIVI");
        titolo.setFont(new Font("System Bold", 28));

        Button btnInserisci = creaPulsanteSezione("Inserisci Prenotazione Campo", 50);
        Button btnElimina = creaPulsanteSezione("Elimina Prenotazione Campo", 50);
        
        Button btnIndietro = new Button("⬅ Torna alla Mappa");
        btnIndietro.setPrefSize(160, 40);
        btnIndietro.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");

        btnInserisci.setOnAction(e -> mostraFormAggiungiPrenotazioneCampo());
        btnElimina.setOnAction(e -> mostraFormEliminaPrenotazioneCampo());
        btnIndietro.setOnAction(e -> stage.setScene(scenaPrincipale));

        layout.getChildren().addAll(titolo, btnInserisci, btnElimina, btnIndietro);
        this.scenaMenuCampi = new Scene(layout, stage.getScene().getWidth(), stage.getScene().getHeight());
        stage.setScene(scenaMenuCampi);
    }

    private void mostraFormInserisciPrenotazione() {
        GridPane grid = new GridPane();
        grid.setVgap(12); grid.setHgap(15);
        grid.setAlignment(Pos.CENTER);

        DatePicker dpInizio = new DatePicker(LocalDate.now());
        DatePicker dpFine = new DatePicker(LocalDate.now().plusDays(7));
        TextField txtCf = new TextField();
        TextField txtDip = new TextField();

        grid.add(new Label("Data Inizio:"), 0, 0);      grid.add(dpInizio, 1, 0);
        grid.add(new Label("Data Fine:"), 0, 1);          grid.add(dpFine, 1, 1);
        grid.add(new Label("CF Cliente:"), 0, 2);         grid.add(txtCf, 1, 2);
        grid.add(new Label("Cod. Dipendente:"), 0, 3);    grid.add(txtDip, 1, 3);

        Button btnSalva = new Button("Salva Prenotazione");
        btnSalva.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");
        btnSalva.setOnAction(e -> {
            if (onCreaPrenotazioneAction != null) {
                try {
                    onCreaPrenotazioneAction.onCrea(
                        dpInizio.getValue(),
                        dpFine.getValue(),
                        Integer.parseInt(txtDip.getText()),
                        txtCf.getText()
                    );
                    stage.setScene(scenaMenuOmbrelloni);
                } catch (Exception ex) {
                    mostraMessaggioEsito(false, "", "Controlla i dati inseriti (es. dipendente numerico).");
                }
            }
        });

        Button btnIndietro = new Button("Indietro");
        btnIndietro.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        btnIndietro.setOnAction(e -> stage.setScene(scenaMenuOmbrelloni));

        // CORRETTO: HBox centrato per i pulsanti del form
        HBox boxBottoni = new HBox(15, btnSalva, btnIndietro);
        boxBottoni.setAlignment(Pos.CENTER);

        VBox layout = new VBox(25, new Label("INSERISCI NUOVA PRENOTAZIONE"), grid, boxBottoni);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        stage.setScene(new Scene(layout, stage.getScene().getWidth(), stage.getScene().getHeight()));
    }

    private void mostraFormEliminaPrenotazione() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        TextField txtCod = new TextField();
        txtCod.setMaxWidth(200);

        Button btnElimina = new Button("Elimina Definitivamente");
        btnElimina.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        btnElimina.setOnAction(e -> {
            try {
                if (onEliminaPrenotazioneSpiaggiaAction != null) {
                    onEliminaPrenotazioneSpiaggiaAction.accept(Integer.parseInt(txtCod.getText()));
                    stage.setScene(scenaMenuOmbrelloni);
                }
            } catch (NumberFormatException ex) {
                mostraMessaggioEsito(false, "", "Il codice prenotazione deve essere un numero intero.");
            }
        });

        Button btnIndietro = new Button("Annulla");
        btnIndietro.setOnAction(e -> stage.setScene(scenaMenuOmbrelloni));

        layout.getChildren().addAll(new Label("Inserisci Codice Numerico della Prenotazione da eliminare:"), txtCod, new HBox(10, btnElimina, btnIndietro) {{ AlignmentPosCenter(this); }});
        stage.setScene(new Scene(layout, stage.getScene().getWidth(), stage.getScene().getHeight()));
    }
    
    private void AlignmentPosCenter(HBox hbox) {
        hbox.setAlignment(Pos.CENTER);
    }

    private void mostraFormAggiungiGiornaliera() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        Button btnIndietro = new Button("Indietro");
        btnIndietro.setOnAction(e -> stage.setScene(scenaMenuOmbrelloni));
        layout.getChildren().addAll(new Label("Form Aggiungi Prenotazione Giornaliera"), btnIndietro);
        stage.setScene(new Scene(layout, stage.getScene().getWidth(), stage.getScene().getHeight()));
    }

    private void mostraFormAggiornaOmbrellone() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        Button btnIndietro = new Button("Indietro");
        btnIndietro.setOnAction(e -> stage.setScene(scenaMenuOmbrelloni));
        layout.getChildren().addAll(new Label("Form Aggiorna Ombrellone"), btnIndietro);
        stage.setScene(new Scene(layout, stage.getScene().getWidth(), stage.getScene().getHeight()));
    }

    private void mostraFormEliminaGiornaliera() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        Button btnIndietro = new Button("Indietro");
        btnIndietro.setOnAction(e -> stage.setScene(scenaMenuOmbrelloni));
        layout.getChildren().addAll(new Label("Form Elimina Prenotazione Giornaliera"), btnIndietro);
        stage.setScene(new Scene(layout, stage.getScene().getWidth(), stage.getScene().getHeight()));
    }

    private void mostraFormAggiungiAllestimento() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        Button btnIndietro = new Button("Indietro");
        btnIndietro.setOnAction(e -> stage.setScene(scenaMenuOmbrelloni));
        layout.getChildren().addAll(new Label("Form Aggiungi Allestimento"), btnIndietro);
        stage.setScene(new Scene(layout, stage.getScene().getWidth(), stage.getScene().getHeight()));
    }

    private void mostraFormAggiornaAllestimento() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        Button btnIndietro = new Button("Indietro");
        btnIndietro.setOnAction(e -> stage.setScene(scenaMenuOmbrelloni));
        layout.getChildren().addAll(new Label("Form Aggiorna Allestimento"), btnIndietro);
        stage.setScene(new Scene(layout, stage.getScene().getWidth(), stage.getScene().getHeight()));
    }

    private void mostraFormEliminaAllestimento() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        Button btnIndietro = new Button("Indietro");
        btnIndietro.setOnAction(e -> stage.setScene(scenaMenuOmbrelloni));
        layout.getChildren().addAll(new Label("Form Elimina Allestimento"), btnIndietro);
        stage.setScene(new Scene(layout, stage.getScene().getWidth(), stage.getScene().getHeight()));
    }

    private void mostraFormAggiungiNoleggio() {
        GridPane grid = new GridPane();
        grid.setVgap(12); grid.setHgap(15);
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
        btnSalva.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");
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
        btnAnnulla.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        btnAnnulla.setOnAction(e -> stage.setScene(scenaMenuAttrezzature));

        HBox buttons = new HBox(15, btnSalva, btnAnnulla);
        buttons.setAlignment(Pos.CENTER);
        
        VBox layout = new VBox(25, new Label("INSERISCI NUOVO NOLEGGIO"), grid, buttons);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        stage.setScene(new Scene(layout, stage.getScene().getWidth(), stage.getScene().getHeight()));
    }

    private void mostraFormAggiungiPrenotazioneCampo() {
        GridPane grid = new GridPane();
        grid.setVgap(12); grid.setHgap(15);
        grid.setAlignment(Pos.CENTER);

        DatePicker datePicker = new DatePicker(LocalDate.now());

        ComboBox<String> cmbCampo = new ComboBox<>();
        cmbCampo.getItems().addAll("Beach Volley", "Beach Tennis", "Calcetto", "Bocce Sabbia", "Bocce Cemento");
        cmbCampo.setValue("Beach Volley");

        TextField txtOraInizio = new TextField("10:00");
        TextField txtOraFine = new TextField("11:00");
        TextField txtCf = new TextField();
        TextField txtCodDip = new TextField();

        grid.add(new Label("Data Prenotazione:"), 0, 0);   grid.add(datePicker, 1, 0);
        grid.add(new Label("Seleziona Campo:"), 0, 1);      grid.add(cmbCampo, 1, 1);
        grid.add(new Label("Ora Inizio (HH:mm):"), 0, 2); grid.add(txtOraInizio, 1, 2);
        grid.add(new Label("Ora Fine (HH:mm):"), 0, 3);   grid.add(txtOraFine, 1, 3);
        grid.add(new Label("CF Cliente:"), 0, 4);          grid.add(txtCf, 1, 4);
        grid.add(new Label("Cod. Dipendente:"), 0, 5);    grid.add(txtCodDip, 1, 5);

        Button btnSalva = new Button("Salva Prenotazione Campo");
        btnSalva.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");
        btnSalva.setOnAction(e -> {
            try {
                String nomeSelezionato = cmbCampo.getValue();
                String codCampoDb = switch (nomeSelezionato) {
                    case "Beach Volley" -> "C1";
                    case "Beach Tennis" -> "C2";
                    case "Calcetto" -> "C3";
                    case "Bocce Sabbia" -> "C4";
                    case "Bocce Cemento" -> "C5";
                    default -> "C1";
                };

                if (onCreaPrenotazioneCampoAction != null) {
                    onCreaPrenotazioneCampoAction.onCrea(
                        datePicker.getValue(),
                        LocalTime.parse(txtOraInizio.getText()),
                        LocalTime.parse(txtOraFine.getText()),
                        codCampoDb,
                        txtCf.getText(),
                        txtCodDip.getText()
                    );
                }
            } catch (Exception ex) {
                mostraMessaggioEsito(false, "", "Formato dati non valido.");
            }
        });

        Button btnAnnulla = new Button("Indietro");
        btnAnnulla.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        btnAnnulla.setOnAction(e -> stage.setScene(scenaMenuCampi));

        HBox buttons = new HBox(15, btnSalva, btnAnnulla);
        buttons.setAlignment(Pos.CENTER);

        VBox layout = new VBox(25, new Label("INSERISCI PRENOTAZIONE CAMPO"), grid, buttons);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        stage.setScene(new Scene(layout, stage.getScene().getWidth(), stage.getScene().getHeight()));
    }

    private void mostraFormEliminaPrenotazioneCampo() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);

        TextField txtCod = new TextField();
        txtCod.setMaxWidth(200);
        
        Button btnElimina = new Button("Elimina Definitivamente");
        btnElimina.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        btnElimina.setOnAction(e -> {
            try {
                if (onEliminaPrenotazioneCampoAction != null) {
                    onEliminaPrenotazioneCampoAction.accept(Integer.parseInt(txtCod.getText()));
                    stage.setScene(scenaMenuCampi);
                }
            } catch (NumberFormatException ex) {
                mostraMessaggioEsito(false, "", "Il codice prenotazione deve essere un numero intero.");
            }
        });

        Button btnIndietro = new Button("Annulla");
        btnIndietro.setOnAction(e -> stage.setScene(scenaMenuCampi));

        layout.getChildren().addAll(
            new Label("Inserisci il Codice Numerico della Prenotazione Campo da eliminare:"),
            txtCod, new HBox(10, btnElimina, btnIndietro) {{ AlignmentPosCenter(this); }}
        );

        stage.setScene(new Scene(layout, stage.getScene().getWidth(), stage.getScene().getHeight()));
    }

    private void mostraStoricoNoleggi() {
        TableView<StoricoNoleggio> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<StoricoNoleggio, String> colCf = new TableColumn<>("CF");
        colCf.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().cf()));
        
        TableColumn<StoricoNoleggio, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().nome()));

        TableColumn<StoricoNoleggio, String> colCognome = new TableColumn<>("Cognome");
        colCognome.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().cognome()));

        TableColumn<StoricoNoleggio, String> colCodAttrezzatura = new TableColumn<>("Codice Attrezzatura");
        colCodAttrezzatura.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().codAttrezzatura()));

        TableColumn<StoricoNoleggio, Integer> colCod = new TableColumn<>("Cod Noleggio");
        colCod.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().codNoleggio()));
        
        TableColumn<StoricoNoleggio, LocalDate> colData = new TableColumn<>("Data");
        colData.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().dataNoleggio()));

        TableColumn<StoricoNoleggio, LocalTime> colOra = new TableColumn<>("Ora Inizio");
        colOra.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().oraInizio()));

        TableColumn<StoricoNoleggio, Integer> colDurata = new TableColumn<>("Durata (h)");
        colDurata.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().durataOre()));

        TableColumn<StoricoNoleggio, Integer> colCosto = new TableColumn<>("Costo Tot (€)");
        colCosto.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().costoTotale()));

        table.getColumns().addAll(colCf, colNome, colCognome, colCod, colData, colCodAttrezzatura, colOra, colDurata, colCosto);
        
        List<StoricoNoleggio> listaPulita = listaStoricoNoleggiAttuali.stream()
            .filter(n -> n != null)
            .toList();
        table.setItems(javafx.collections.FXCollections.observableArrayList(listaPulita));

        Button btnIndietro = new Button("Indietro");
        btnIndietro.setOnAction(e -> stage.setScene(scenaMenuAttrezzature));

        VBox layout = new VBox(15, new Label("STORICO COMPLETO NOLEGGI"), table, btnIndietro);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        
        stage.setScene(new Scene(layout, stage.getScene().getWidth(), stage.getScene().getHeight()));
    }

    public void mostraStoricoPrenotazioni(List<StoricoPrenotazione> storico) {
        TableView<StoricoPrenotazione> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<StoricoPrenotazione, Integer> colCod = new TableColumn<>("Cod Prenotazione");
        colCod.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().codPrenotazione()));

        TableColumn<StoricoPrenotazione, LocalDate> colInizio = new TableColumn<>("Data Inizio");
        colInizio.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().dataInizio()));

        TableColumn<StoricoPrenotazione, LocalDate> colFine = new TableColumn<>("Data Fine");
        colFine.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().dataFine()));

        TableColumn<StoricoPrenotazione, String> colCf = new TableColumn<>("CF Cliente");
        colCf.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().cf()));

        TableColumn<StoricoPrenotazione, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().nome()));

        TableColumn<StoricoPrenotazione, String> colCognome = new TableColumn<>("Cognome");
        colCognome.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().cognome()));

        table.getColumns().addAll(colCod, colInizio, colFine, colCf, colNome, colCognome);
        table.setItems(javafx.collections.FXCollections.observableArrayList(storico));

        Button btnIndietro = new Button("Indietro");
        btnIndietro.setOnAction(e -> stage.setScene(scenaMenuOmbrelloni));

        VBox layout = new VBox(15, new Label("STORICO PRENOTAZIONI SPIAGGIA"), table, btnIndietro);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        stage.setScene(new Scene(layout, stage.getScene().getWidth(), stage.getScene().getHeight()));
    }

    private void mostraFormEliminaNoleggio() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);

        TextField txtCod = new TextField();
        txtCod.setMaxWidth(200);
        
        Button btnElimina = new Button("Elimina Definitivamente");
        btnElimina.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        btnElimina.setOnAction(e -> {
            try {
                if (onEliminaNoleggioAction != null) {
                    onEliminaNoleggioAction.accept(Integer.parseInt(txtCod.getText()));
                    stage.setScene(scenaMenuAttrezzature);
                }
            } catch (NumberFormatException ex) {
                mostraMessaggioEsito(false, "", "Il codice noleggio deve essere un numero intero.");
            }
        });

        Button btnIndietro = new Button("Annulla");
        btnIndietro.setOnAction(e -> stage.setScene(scenaMenuAttrezzature));

        layout.getChildren().addAll(
            new Label("Inserisci il Codice Numerico del Noleggio da eliminare:"),
            txtCod, new HBox(10, btnElimina, btnIndietro) {{ AlignmentPosCenter(this); }}
        );

        stage.setScene(new Scene(layout, stage.getScene().getWidth(), stage.getScene().getHeight()));
    }

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

    public void mostraDettagliPrenotazioneCella(String codZona, String nomeZona, int codPrenotazione, String clientePrenotato) { 
        cambiaScenaOmbrelloni();
    }
    
    public void apriFormNuovaPrenotazionePerCella(int numeroCella) { 
        cambiaScenaOmbrelloni();
    }
    
    public void mostraCampiOccupati(List<PrenotazioneCampo> campiOccupati) {
        List<String> codici = campiOccupati.stream()
                .map(PrenotazioneCampo::getCodCampo)
                .toList();
        aggiornaStatoCampi(codici);
    }

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

        Button btnOmbrelloniMenu = creaPulsanteSezione("OMBRELLONI", 75);
        btnOmbrelloniMenu.setOnAction(e -> cambiaScenaOmbrelloni());

        Button btnAttrezzature = creaPulsanteSezione("ATTREZZATURE", 75);
        btnAttrezzature.setOnAction(e -> cambiaScenaAttrezzature());

        VBox boxCampi = new VBox(8);
        boxCampi.setAlignment(Pos.CENTER);
        
        Label lblCampiTitle = new Label("CAMPI SPORTIVI");
        lblCampiTitle.setFont(new Font("System Bold", 12));

        Button btnBeachVolley = creaPulsanteCampo("BEACH VOLLEY");
        Button btnBeachTennis = creaPulsanteCampo("BEACH TENNIS");
        Button btnCalcetto = creaPulsanteCampo("CALCETTO");
        Button btnBocceSabbia = creaPulsanteCampo("BOCCE SABBIA");
        Button btnBocceCemento = creaPulsanteCampo("BOCCE CEMENTO");
        Button btnStoricoReport = creaPulsanteCampo("STORICO E REPORT");

        bottoniCampiMap.put("C1", btnBeachVolley);
        bottoniCampiMap.put("C2", btnBeachTennis);
        bottoniCampiMap.put("C3", btnCalcetto);
        bottoniCampiMap.put("C4", btnBocceSabbia);
        bottoniCampiMap.put("C5", btnBocceCemento);

        for (Button btn : bottoniCampiMap.values()) {
            impostaStatoCell(btn, false);
        }

        btnBeachVolley.setOnAction(e -> cambiaScenaCampi());
        btnBeachTennis.setOnAction(e -> cambiaScenaCampi());
        btnCalcetto.setOnAction(e -> cambiaScenaCampi());
        btnBocceSabbia.setOnAction(e -> cambiaScenaCampi());
        btnBocceCemento.setOnAction(e -> cambiaScenaCampi());

        btnStoricoReport.setOnAction(e -> {
            if (onGeneraReportGiornaliero != null) onGeneraReportGiornaliero.accept(LocalDate.now());
        });

        boxCampi.getChildren().addAll(lblCampiTitle, btnBeachVolley, btnBeachTennis, btnCalcetto, btnBocceSabbia, btnBocceCemento, btnStoricoReport);
        pannello.getChildren().addAll(btnOmbrelloniMenu, btnAttrezzature, boxCampi);
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