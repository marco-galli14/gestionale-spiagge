package view;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import common.Pair;
import common.StoricoNoleggio;
import common.StoricoPrenotazione;
import dao.MappaDAO;
import dao.ZonaDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Allestimento;
import model.Cliente;
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
    private Supplier<List<Cliente>> onRichiediListaClienti;
    
    private OnModificaAllestimentoGiornalieroListener onModificaAllestimentoGiornaliero;
    private OnRichiediAllestimentiGiornoListener onRichiediAllestimentiGiorno;
    
    private Runnable onVisualizzaContabilitaAction;
    private Consumer<LocalDate> onReportPerDataAction;

    @FunctionalInterface
    public interface OnSalvaClienteListener {
        void onSalva(String cf, String nome, String cognome, String email, String telefono, Integer codHotel);
    }
    @FunctionalInterface
    public interface OnCreaPrenotazioneListener {
        void onCrea(LocalDate dataInizio, LocalDate dataFine, int codDipendente, String cf, List<Integer> numeriOmbrelloni, int qtaLettini, int qtaSdraio, int qtaSedie);
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
    @FunctionalInterface
    public interface OnModificaAllestimentoGiornalieroListener {
        void onModifica(int codPrenotazione, int numOmbrellone, LocalDate data, int qtaLettini, int qtaSdraio, int qtaSedie);
    }
    @FunctionalInterface
    public interface OnRichiediAllestimentiGiornoListener {
        List<Allestimento> onRichiedi(int codPrenotazione, int numOmbrellone, LocalDate data);
    }

    public MainView(Stage stage) {
        this.stage = stage;
    }

    public LocalDate getDataSelezionata() {
        return datePickerPrincipale != null ? datePickerPrincipale.getValue() : LocalDate.now();
    }

    public void setOnRichiediListaClienti(Supplier<List<Cliente>> listener) { this.onRichiediListaClienti = listener; }
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
    public void setOnModificaAllestimentoGiornaliero(OnModificaAllestimentoGiornalieroListener listener) { this.onModificaAllestimentoGiornaliero = listener; }
    public void setOnRichiediAllestimentiGiorno(OnRichiediAllestimentiGiornoListener listener) { this.onRichiediAllestimentiGiorno = listener; }
    public void setOnVisualizzaContabilitaAction(Runnable listener) { this.onVisualizzaContabilitaAction = listener; }
    public void setOnReportPerDataAction(Consumer<LocalDate> listener) { this.onReportPerDataAction = listener; }

    public void mostraFinestra() {
        BorderPane root = new BorderPane();
        
        try {
            String imageUrl = getClass().getResource("/pictures/spiaggia.jpg").toExternalForm();
            root.setStyle("-fx-background-image: url('" + imageUrl + "'); " +
                          "-fx-background-size: cover; " +
                          "-fx-background-position: center center; " +
                          "-fx-background-repeat: no-repeat;");
        } catch (Exception e) {
            root.setStyle("-fx-background-color: #f8f9fa;");
        }
        
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
        Button btnModificaAllestimentoGiorno = creaPulsanteSezione("Modifica Allestimento Giornaliero", 40);

        Button btnIndietro = new Button("⬅ Torna alla Mappa");
        btnIndietro.setPrefSize(180, 38);
        btnIndietro.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");

        btnInserisciPren.setOnAction(e -> mostraFormInserisciPrenotazione(null));
        btnEliminaPren.setOnAction(e -> mostraFormEliminaPrenotazione());
        btnStoricoPren.setOnAction(e -> {
            if (onRichiediStoricoPrenotazioni != null) onRichiediStoricoPrenotazioni.run();
        });
        btnModificaAllestimentoGiorno.setOnAction(e -> mostraFormModificaAllestimentoGiornaliero());
        btnIndietro.setOnAction(e -> stage.setScene(scenaPrincipale));

        VBox contentBox = new VBox(12, titolo, btnInserisciPren, btnEliminaPren, btnStoricoPren, 
            btnModificaAllestimentoGiorno, btnIndietro);
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

    public void apriFormNuovaPrenotazionePerCella(int numeroCella) { 
        mostraFormInserisciPrenotazione(numeroCella);
    }

    public void mostraFormInserisciPrenotazione() {
        mostraFormInserisciPrenotazione(null);
    }

    private void mostraFormInserisciPrenotazione(Integer numeroOmbrellonePreselezionato) {
        GridPane grid = new GridPane();
        grid.setVgap(12); grid.setHgap(15);
        grid.setAlignment(Pos.CENTER);

        LocalDate dataBase = getDataSelezionata();
        DatePicker dpInizio = new DatePicker(dataBase);
        DatePicker dpFine = new DatePicker(dataBase.plusDays(7));
        
        TextField txtCf = new TextField();
        
        Button btnCercaCf = new Button("🔍 Cerca");
        btnCercaCf.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-weight: bold;");
        btnCercaCf.setOnAction(e -> mostraSelezionatoreCliente(txtCf));

        Button btnNuovoCliente = new Button("➕ Nuovo");
        btnNuovoCliente.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        btnNuovoCliente.setOnAction(e -> mostraFormNuovoClienteRapido(txtCf));

        HBox boxCf = new HBox(8, txtCf, btnCercaCf, btnNuovoCliente);
        boxCf.setAlignment(Pos.CENTER_LEFT);

        TextField txtDip = new TextField();
        TextField txtOmbrelloni = new TextField();
        
        if (numeroOmbrellonePreselezionato != null) {
            txtOmbrelloni.setText(String.valueOf(numeroOmbrellonePreselezionato));
        } else {
            txtOmbrelloni.setPromptText("Es. 1, 5, 12");
        }

        Spinner<Integer> spnLettini = new Spinner<>(0, 10, 0);
        Spinner<Integer> spnSdraio = new Spinner<>(0, 10, 0);
        Spinner<Integer> spnSedie = new Spinner<>(0, 10, 0);
        spnLettini.setPrefWidth(70); spnSdraio.setPrefWidth(70); spnSedie.setPrefWidth(70);

        HBox boxAllestimenti = new HBox(10, 
            new Label("Lettini:"), spnLettini, 
            new Label("Sdraio:"), spnSdraio, 
            new Label("Sedie:"), spnSedie
        );
        boxAllestimenti.setAlignment(Pos.CENTER_LEFT);

        grid.add(new Label("Data Inizio:"), 0, 0);          grid.add(dpInizio, 1, 0);
        grid.add(new Label("Data Fine:"), 0, 1);              grid.add(dpFine, 1, 1);
        grid.add(new Label("CF Cliente:"), 0, 2);             grid.add(boxCf, 1, 2);
        grid.add(new Label("Cod. Dipendente:"), 0, 3);        grid.add(txtDip, 1, 3);
        grid.add(new Label("Num. Ombrelloni (,):"), 0, 4);   grid.add(txtOmbrelloni, 1, 4);
        grid.add(new Label("Allestimento Extra:"), 0, 5);     grid.add(boxAllestimenti, 1, 5);

        Button btnSalva = new Button("Salva Prenotazione");
        btnSalva.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");
        btnSalva.setOnAction(e -> {
            if (onCreaPrenotazioneAction != null) {
                try {
                    List<Integer> listaOmbrelloni = Arrays.stream(txtOmbrelloni.getText().split(","))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .map(Integer::parseInt)
                            .collect(Collectors.toList());

                    onCreaPrenotazioneAction.onCrea(
                        dpInizio.getValue(),
                        dpFine.getValue(),
                        Integer.parseInt(txtDip.getText()),
                        txtCf.getText(),
                        listaOmbrelloni,
                        spnLettini.getValue(),
                        spnSdraio.getValue(),
                        spnSedie.getValue()
                    );
                    stage.setScene(scenaPrincipale);
                } catch (Exception ex) {
                    mostraMessaggioEsito(false, "", "Controlla i dati inseriti (es. ombrelloni separati da virgola e dipendente numerico).");
                }
            }
        });

        Button btnIndietro = new Button("Indietro");
        btnIndietro.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        btnIndietro.setOnAction(e -> stage.setScene(scenaPrincipale));

        HBox boxBottoni = new HBox(15, btnSalva, btnIndietro);
        boxBottoni.setAlignment(Pos.CENTER);

        VBox layout = new VBox(25, new Label("INSERISCI NUOVA PRENOTAZIONE"), grid, boxBottoni);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        stage.setScene(new Scene(layout, stage.getScene().getWidth(), stage.getScene().getHeight()));
    }

    public void mostraFormNuovoClienteRapido(TextField targetField) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Registra Nuovo Cliente");

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(20));

        TextField txtCf = new TextField();
        TextField txtNome = new TextField();
        TextField txtCognome = new TextField();
        TextField txtEmail = new TextField();
        TextField txtTelefono = new TextField();
        TextField txtCodHotel = new TextField();

        grid.add(new Label("Codice Fiscale:"), 0, 0); grid.add(txtCf, 1, 0);
        grid.add(new Label("Nome:"), 0, 1);          grid.add(txtNome, 1, 1);
        grid.add(new Label("Cognome:"), 0, 2);       grid.add(txtCognome, 1, 2);
        grid.add(new Label("Email:"), 0, 3);         grid.add(txtEmail, 1, 3);
        grid.add(new Label("Telefono:"), 0, 4);      grid.add(txtTelefono, 1, 4);
        grid.add(new Label("Cod. Hotel:"), 0, 5);    grid.add(txtCodHotel, 1, 5);

        Button btnSalva = new Button("Salva e Seleziona");
        btnSalva.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");
        
        btnSalva.setOnAction(e -> {
            if (txtCf.getText().trim().isEmpty() || txtNome.getText().trim().isEmpty() || txtCognome.getText().trim().isEmpty()) {
                mostraMessaggioEsito(false, "", "CF, Nome e Cognome sono obbligatori.");
                return;
            }

            Integer codHotel = null;
            if (!txtCodHotel.getText().trim().isEmpty()) {
                try {
                    codHotel = Integer.parseInt(txtCodHotel.getText().trim());
                } catch (NumberFormatException ex) {
                    mostraMessaggioEsito(false, "", "Il codice hotel deve essere un numero.");
                    return;
                }
            }

            if (onSalvaClienteAction != null) {
                onSalvaClienteAction.onSalva(
                    txtCf.getText().trim(),
                    txtNome.getText().trim(),
                    txtCognome.getText().trim(),
                    txtEmail.getText().trim(),
                    txtTelefono.getText().trim(),
                    codHotel
                );
                targetField.setText(txtCf.getText().trim());
                popup.close();
            }
        });

        Button btnAnnulla = new Button("Annulla");
        btnAnnulla.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        btnAnnulla.setOnAction(e -> popup.close());

        HBox boxBottoni = new HBox(10, btnSalva, btnAnnulla);
        boxBottoni.setAlignment(Pos.CENTER_RIGHT);

        VBox layout = new VBox(15, new Label("REGISTRAZIONE RAPIDA CLIENTE"), grid, boxBottoni);
        layout.setPadding(new Insets(15));
        popup.setScene(new Scene(layout, 380, 360));
        popup.showAndWait();
    }

    public void mostraSelezionatoreCliente(TextField targetField) {
        if (onRichiediListaClienti == null) {
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
        tabella.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        List<Cliente> clienti = onRichiediListaClienti.get();
        ObservableList<Cliente> masterData = FXCollections.observableArrayList(clienti != null ? clienti : new ArrayList<>());
        FilteredList<Cliente> filteredData = new FilteredList<>(masterData, p -> true);

        txtFiltro.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(cliente -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String filter = newValue.toLowerCase();
                return (cliente.getCf() != null && cliente.getCf().toLowerCase().contains(filter)) ||
                       (cliente.getNome() != null && cliente.getNome().toLowerCase().contains(filter)) ||
                       (cliente.getCognome() != null && cliente.getCognome().toLowerCase().contains(filter));
            });
        });

        tabella.setItems(filteredData);

        tabella.setRowFactory(tv -> {
            TableRow<Cliente> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Cliente selezionato = row.getItem();
                    targetField.setText(selezionato.getCf());
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
                    stage.setScene(scenaPrincipale);
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

    private void mostraFormModificaAllestimentoGiornaliero() {
        GridPane grid = new GridPane();
        grid.setVgap(12); grid.setHgap(15);
        grid.setAlignment(Pos.CENTER);

        TextField txtCodPren = new TextField();
        TextField txtNumOmb = new TextField();
        DatePicker dpData = new DatePicker(LocalDate.now());

        Spinner<Integer> spnLettini = new Spinner<>(0, 10, 0);
        Spinner<Integer> spnSdraio = new Spinner<>(0, 10, 0);
        Spinner<Integer> spnSedie = new Spinner<>(0, 10, 0);
        spnLettini.setPrefWidth(70); spnSdraio.setPrefWidth(70); spnSedie.setPrefWidth(70);

        Button btnCarica = new Button("🔄 Cerca Allestimento Attuale");
        btnCarica.setOnAction(e -> {
            try {
                int cod = Integer.parseInt(txtCodPren.getText().trim());
                int omb = Integer.parseInt(txtNumOmb.getText().trim());
                LocalDate d = dpData.getValue();

                if (onRichiediAllestimentiGiorno != null) {
                    List<Allestimento> lista = onRichiediAllestimentiGiorno.onRichiedi(cod, omb, d);
                    int l = 0, s = 0, sd = 0;
                    for (Allestimento a : lista) {
                        if ("LETTINO".equalsIgnoreCase(a.getCodSeduta())) l = a.getQuantita();
                        if ("SDRAIO".equalsIgnoreCase(a.getCodSeduta())) s = a.getQuantita();
                        if ("SEDIA".equalsIgnoreCase(a.getCodSeduta())) sd = a.getQuantita();
                    }
                    spnLettini.getValueFactory().setValue(l);
                    spnSdraio.getValueFactory().setValue(s);
                    spnSedie.getValueFactory().setValue(sd);
                }
            } catch (Exception ex) {
                mostraMessaggioEsito(false, "", "Inserisci un codice prenotazione e un numero ombrellone validi.");
            }
        });

        HBox boxAllestimenti = new HBox(10, 
            new Label("Lettini:"), spnLettini, 
            new Label("Sdraio:"), spnSdraio, 
            new Label("Sedie:"), spnSedie
        );
        boxAllestimenti.setAlignment(Pos.CENTER_LEFT);

        grid.add(new Label("Cod. Prenotazione:"), 0, 0); grid.add(txtCodPren, 1, 0);
        grid.add(new Label("Num. Ombrellone:"), 0, 1);   grid.add(txtNumOmb, 1, 1);
        grid.add(new Label("Data Riferimento:"), 0, 2);  grid.add(dpData, 1, 2);
        grid.add(btnCarica, 1, 3);
        grid.add(new Label("Nuovo Allestimento:"), 0, 4); grid.add(boxAllestimenti, 1, 4);

        Button btnSalva = new Button("Salva Modifica Giorno");
        btnSalva.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");
        btnSalva.setOnAction(e -> {
            try {
                if (onModificaAllestimentoGiornaliero != null) {
                    onModificaAllestimentoGiornaliero.onModifica(
                        Integer.parseInt(txtCodPren.getText().trim()),
                        Integer.parseInt(txtNumOmb.getText().trim()),
                        dpData.getValue(),
                        spnLettini.getValue(),
                        spnSdraio.getValue(),
                        spnSedie.getValue()
                    );
                    stage.setScene(scenaPrincipale);
                }
            } catch (Exception ex) {
                mostraMessaggioEsito(false, "", "Verifica i parametri inseriti.");
            }
        });

        Button btnIndietro = new Button("Indietro");
        btnIndietro.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        btnIndietro.setOnAction(e -> stage.setScene(scenaMenuOmbrelloni));

        HBox boxBottoni = new HBox(15, btnSalva, btnIndietro);
        boxBottoni.setAlignment(Pos.CENTER);

        VBox layout = new VBox(20, new Label("MODIFICA ALLESTIMENTO SINGOLO GIORNO"), grid, boxBottoni);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(25));
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
        Button btnCercaCf = new Button("🔍 Cerca");
        btnCercaCf.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-weight: bold;");
        btnCercaCf.setOnAction(e -> mostraSelezionatoreCliente(txtCf));

        Button btnNuovoCliente = new Button("➕ Nuovo");
        btnNuovoCliente.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        btnNuovoCliente.setOnAction(e -> mostraFormNuovoClienteRapido(txtCf));

        HBox boxCf = new HBox(8, txtCf, btnCercaCf, btnNuovoCliente);
        boxCf.setAlignment(Pos.CENTER_LEFT);

        TextField txtDipendente = new TextField();
        
        ComboBox<String> cmbAttrezzatura = new ComboBox<>();
        cmbAttrezzatura.getItems().addAll("PED", "CAN", "SUP");
        cmbAttrezzatura.setValue("PED");

        grid.add(new Label("Data Noleggio:"), 0, 0);   grid.add(datePicker, 1, 0);
        grid.add(new Label("Ora Inizio (HH:mm):"), 0, 1); grid.add(txtOra, 1, 1);
        grid.add(new Label("Durata (Ore):"), 0, 2);    grid.add(txtDurata, 1, 2);
        grid.add(new Label("CF Cliente:"), 0, 3);      grid.add(boxCf, 1, 3);
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
                    stage.setScene(scenaPrincipale);
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
        Button btnCercaCf = new Button("🔍 Cerca");
        btnCercaCf.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-weight: bold;");
        btnCercaCf.setOnAction(e -> mostraSelezionatoreCliente(txtCf));

        Button btnNuovoCliente = new Button("➕ Nuovo");
        btnNuovoCliente.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        btnNuovoCliente.setOnAction(e -> mostraFormNuovoClienteRapido(txtCf));

        HBox boxCf = new HBox(8, txtCf, btnCercaCf, btnNuovoCliente);
        boxCf.setAlignment(Pos.CENTER_LEFT);

        TextField txtCodDip = new TextField();

        grid.add(new Label("Data Prenotazione:"), 0, 0);   grid.add(datePicker, 1, 0);
        grid.add(new Label("Seleziona Campo:"), 0, 1);      grid.add(cmbCampo, 1, 1);
        grid.add(new Label("Ora Inizio (HH:mm):"), 0, 2); grid.add(txtOraInizio, 1, 2);
        grid.add(new Label("Ora Fine (HH:mm):"), 0, 3);   grid.add(txtOraFine, 1, 3);
        grid.add(new Label("CF Cliente:"), 0, 4);          grid.add(boxCf, 1, 4);
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
                    stage.setScene(scenaPrincipale);
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
                    stage.setScene(scenaPrincipale);
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
        colCf.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().cf()));
        
        TableColumn<StoricoNoleggio, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().nome()));

        TableColumn<StoricoNoleggio, String> colCognome = new TableColumn<>("Cognome");
        colCognome.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().cognome()));

        TableColumn<StoricoNoleggio, String> colCodAttrezzatura = new TableColumn<>("Codice Attrezzatura");
        colCodAttrezzatura.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().codAttrezzatura()));

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
        table.setItems(FXCollections.observableArrayList(listaPulita));

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
        colCf.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().cf()));

        TableColumn<StoricoPrenotazione, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().nome()));

        TableColumn<StoricoPrenotazione, String> colCognome = new TableColumn<>("Cognome");
        colCognome.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().cognome()));

        table.getColumns().addAll(colCod, colInizio, colFine, colCf, colNome, colCognome);
        table.setItems(FXCollections.observableArrayList(storico));

        Button btnIndietro = new Button("Indietro");
        btnIndietro.setOnAction(e -> stage.setScene(scenaMenuOmbrelloni));

        VBox layout = new VBox(15, new Label("STORICO PRENOTAZIONI SPIAGGIA"), table, btnIndietro);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        stage.setScene(new Scene(layout, stage.getScene().getWidth(), stage.getScene().getHeight()));
    }

    public void mostraCentroContabilita(List<Prenotazione> nonSaldate, int incassiPren, int incassiNol) {
        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.TOP_CENTER);
        mainLayout.setStyle("-fx-background-color: #f8f9fa;");

        Label lblTitle = new Label("CENTRO CONTABILITÀ E PAGAMENTI");
        lblTitle.setFont(new Font("System Bold", 22));

        VBox boxReport = new VBox(8);
        boxReport.setPadding(new Insets(10));
        boxReport.setStyle("-fx-border-color: #bdc3c7; -fx-border-radius: 5; -fx-background-color: white;");
        
        Label lblReportOggi = new Label("Incassi Odierni - Prenotazioni: " + incassiPren + "€ | Noleggi: " + incassiNol + "€ | Totale: " + (incassiPren + incassiNol) + "€");
        lblReportOggi.setFont(new Font("System Bold", 13));

        DatePicker dpReport = new DatePicker(LocalDate.now());
        Button btnCercaReport = new Button("Visualizza Report Data");
        btnCercaReport.setOnAction(e -> {
            if (onReportPerDataAction != null && dpReport.getValue() != null) {
                onReportPerDataAction.accept(dpReport.getValue());
            }
        });

        HBox boxCercaData = new HBox(10, new Label("Seleziona Data:"), dpReport, btnCercaReport);
        boxCercaData.setAlignment(Pos.CENTER_LEFT);
        boxReport.getChildren().addAll(lblReportOggi, boxCercaData);

        Label lblNonSaldate = new Label("PRENOTAZIONI NON SALDATE");
        lblNonSaldate.setFont(new Font("System Bold", 14));

        TableView<Prenotazione> tblNonSaldate = new TableView<>();
        tblNonSaldate.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Prenotazione, Integer> colCod = new TableColumn<>("Cod. Pren.");
        colCod.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getCodPrenotazione()));

        TableColumn<Prenotazione, String> colCf = new TableColumn<>("CF Cliente");
        colCf.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCf()));

        TableColumn<Prenotazione, Integer> colCosto = new TableColumn<>("Costo Totale (€)");
        colCosto.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getPrezzoTotale()));

        tblNonSaldate.getColumns().addAll(colCod, colCf, colCosto);
        tblNonSaldate.setItems(FXCollections.observableArrayList(nonSaldate));

        GridPane gridPagamento = new GridPane();
        gridPagamento.setVgap(8); gridPagamento.setHgap(10);
        gridPagamento.setAlignment(Pos.CENTER_LEFT);

        TextField txtCodPag = new TextField();
        TextField txtImporto = new TextField();
        DatePicker dpDataPag = new DatePicker(LocalDate.now());
        ComboBox<String> cmbMetodo = new ComboBox<>();
        cmbMetodo.getItems().addAll("CONTANTI", "CARTA", "POS", "BONIFICO");
        cmbMetodo.setValue("CONTANTI");

        gridPagamento.add(new Label("Codice Pagamento:"), 0, 0); gridPagamento.add(txtCodPag, 1, 0);
        gridPagamento.add(new Label("Importo (€):"), 0, 1);       gridPagamento.add(txtImporto, 1, 1);
        gridPagamento.add(new Label("Data Pagamento:"), 2, 0);   gridPagamento.add(dpDataPag, 3, 0);
        gridPagamento.add(new Label("Metodo:"), 2, 1);           gridPagamento.add(cmbMetodo, 3, 1);

        tblNonSaldate.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                txtImporto.setText(String.valueOf(newSel.getPrezzoTotale()));
            }
        });

        Button btnRegistra = new Button("💳 Registra Pagamento");
        btnRegistra.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");
        btnRegistra.setOnAction(e -> {
            Prenotazione sel = tblNonSaldate.getSelectionModel().getSelectedItem();
            if (sel == null) {
                mostraMessaggioEsito(false, "", "Seleziona prima una prenotazione dalla tabella.");
                return;
            }
            try {
                if (onRegistraPagamentoPrenotazione != null) {
                    onRegistraPagamentoPrenotazione.onRegistra(
                        txtCodPag.getText().trim(),
                        Integer.parseInt(txtImporto.getText().trim()),
                        dpDataPag.getValue(),
                        cmbMetodo.getValue(),
                        sel.getCodPrenotazione()
                    );
                }
            } catch (Exception ex) {
                mostraMessaggioEsito(false, "", "Verifica i dati del pagamento inseriti.");
            }
        });

        Button btnIndietro = new Button("⬅ Torna alla Mappa");
        btnIndietro.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        btnIndietro.setOnAction(e -> stage.setScene(scenaPrincipale));

        HBox boxAzioni = new HBox(15, btnRegistra, btnIndietro);
        boxAzioni.setAlignment(Pos.CENTER);

        mainLayout.getChildren().addAll(
            lblTitle, boxReport, lblNonSaldate, tblNonSaldate, gridPagamento, boxAzioni
        );

        ScrollPane scroll = new ScrollPane(mainLayout);
        scroll.setFitToWidth(true);
        stage.setScene(new Scene(scroll, stage.getScene().getWidth(), stage.getScene().getHeight()));
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
                    stage.setScene(scenaPrincipale);
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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Dettagli Ombrellone Occupato");
        alert.setHeaderText("Ombrellone Occupato");
        alert.setContentText("Zona: " + nomeZona + " (" + codZona + ")\n" +
                             "Codice Prenotazione: " + codPrenotazione + "\n" +
                             "Cliente: " + clientePrenotato);
        alert.showAndWait();
    }

    public void mostraCampiOccupati(List<PrenotazioneCampo> campiOccupati) {
        List<String> codici = campiOccupati.stream()
                .map(PrenotazioneCampo::getCodCampo)
                .toList();
        aggiornaStatoCampi(codici);
    }

    public void mostraReportIncassi(int incassiPrenotazioni, int incassiNoleggi) { 
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Report Incassi");
        alert.setHeaderText("Dettaglio Incassi per la Data Selezionata");
        alert.setContentText("Incasso Prenotazioni Spiaggia: " + incassiPrenotazioni + " €\n" +
                             "Incasso Noleggio Attrezzature: " + incassiNoleggi + " €\n" +
                             "Totale Incassato: " + (incassiPrenotazioni + incassiNoleggi) + " €");
        alert.showAndWait();
    }

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
        Button btnStoricoReport = creaPulsanteCampo("STORICO E CONTABILITÀ");

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
            if (onVisualizzaContabilitaAction != null) {
                onVisualizzaContabilitaAction.run();
            }
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