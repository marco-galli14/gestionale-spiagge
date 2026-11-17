package view;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import dao.MappaDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Allestimento;
import model.Cliente;
import model.PrenotazioneCampo;

public class MainView {

    private final Stage stage;
    private GridPane gridOmbrelloni;
    private DatePicker datePickerPrincipale;
    private TextField txtOraPrincipale;
    private Scene scenaPrincipale;

    private final Map<String, Button> bottoniCampiMap = new HashMap<>();

    // Sotto-viste
    private final OmbrelloniView ombrelloniView;
    private final AttrezzatureCampiView attrezzatureCampiView;
    private final ContabilitaStatisticheView contabilitaStatisticheView;
    private final AmministrazioneView amministrazioneView;

    // Listener principali
    private Consumer<Integer> onCellaOmbrelloneClicked;
    private BiConsumer<LocalDate, LocalTime> onCambioDataOraMappa;
    private Runnable onVisualizzaContabilitaAction;

    // Interfacce funzionali condivise
    @FunctionalInterface public interface OnSalvaClienteListener { void onSalva(String cf, String nome, String cognome, String email, String telefono, Integer codHotel, Integer idGruppo); }
    @FunctionalInterface public interface OnCreaPrenotazioneListener { void onCrea(LocalDate dataInizio, LocalDate dataFine, int codDipendente, String cf, List<Integer> numeriOmbrelloni, int qtaLettini, int qtaSdraio, int qtaSedie); }
    @FunctionalInterface public interface OnCreaNoleggioListener { void onCrea(LocalDate data, LocalTime oraInizio, int durata, String cf, int codDipendente, String codAttrezzatura); }
    @FunctionalInterface public interface OnCreaPrenotazioneCampoListener { void onCrea(LocalDate dataPrenotazione, LocalTime oraInizio, LocalTime oraFine, String codCampo, String cf, int codDip); }
    @FunctionalInterface public interface OnRegistraPagamentoListener { void onRegistra(int importo, LocalDate data, String metodo, int codPren); }
    @FunctionalInterface public interface OnRegistraPagamentoNoleggioListener { void onRegistraNol(int importo, LocalDate data, String metodo, int codNoleggio); }
    @FunctionalInterface public interface OnModificaAllestimentoGiornalieroListener { void onModifica(int codPrenotazione, int numOmbrellone, LocalDate data, int qtaLettini, int qtaSdraio, int qtaSedie); }
    @FunctionalInterface public interface OnRichiediAllestimentiGiornoListener { List<Allestimento> onRichiedi(int codPrenotazione, int numOmbrellone, LocalDate data); }
    @FunctionalInterface public interface OnSalvaHotelListener { void onSalva(int codHotel, String nomeHotel, int scontoHotel); }
    @FunctionalInterface public interface OnSalvaDipendenteCompletoListener { void onSalva(int codDip, String nome, String cognome, String user, String pwd, String ruolo); }
    @FunctionalInterface public interface OnSalvaStagioneListener { void onSalva(String codStagione, LocalDate inizio, LocalDate fine); }
    @FunctionalInterface public interface OnSalvaTariffaZonaListener { void onSalva(String codStagione, int codZona, int tariffa); }
    @FunctionalInterface public interface OnSalvaTariffaNoleggioListener { void onSalva(String codStagione, String codAttrezzatura, int tariffaOraria); }
    @FunctionalInterface public interface OnModificaOmbrelloneListener { void onModifica(int codPren, int vecchioOmb, int nuovoOmb); }

    public MainView(Stage stage) {
        this.stage = stage;
        Runnable tornaAllaMappa = () -> stage.setScene(scenaPrincipale);

        this.ombrelloniView = new OmbrelloniView(stage, tornaAllaMappa, this::getDataSelezionata);
        this.attrezzatureCampiView = new AttrezzatureCampiView(stage, tornaAllaMappa);
        this.contabilitaStatisticheView = new ContabilitaStatisticheView(stage, tornaAllaMappa, this::getDataSelezionata);
        this.amministrazioneView = new AmministrazioneView(stage, tornaAllaMappa);
    }

    // Getter per i sotto-controller
    public OmbrelloniView getOmbrelloniView() { return ombrelloniView; }
    public AttrezzatureCampiView getAttrezzatureCampiView() { return attrezzatureCampiView; }
    public ContabilitaStatisticheView getContabilitaStatisticheView() { return contabilitaStatisticheView; }
    public AmministrazioneView getAmministrazioneView() { return amministrazioneView; }

    public LocalDate getDataSelezionata() {
        return datePickerPrincipale != null ? datePickerPrincipale.getValue() : LocalDate.now();
    }

    public void setOnCellaOmbrelloneClicked(Consumer<Integer> listener) { this.onCellaOmbrelloneClicked = listener; }
    public void setOnCambioDataOraMappa(BiConsumer<LocalDate, LocalTime> listener) { this.onCambioDataOraMappa = listener; }
    public void setOnVisualizzaContabilitaAction(Runnable listener) { this.onVisualizzaContabilitaAction = listener; }

    // Inoltro dei listener condivisi sui clienti alle sotto-viste che li usano
    public void setOnRichiediListaClienti(Supplier<List<Cliente>> listener) {
        ombrelloniView.setOnRichiediListaClienti(listener);
        attrezzatureCampiView.setOnRichiediListaClienti(listener);
        amministrazioneView.setOnRichiediListaClienti(listener);
    }

    public void setOnSalvaClienteAction(OnSalvaClienteListener listener) {
        ombrelloniView.setOnSalvaClienteAction(listener);
        attrezzatureCampiView.setOnSalvaClienteAction(listener);
        amministrazioneView.setOnSalvaClienteAction(listener);
    }

    public void mostraFinestra() {
        BorderPane root = new BorderPane();
        try {
            String imageUrl = getClass().getResource("/pictures/spiaggia.jpg").toExternalForm();
            root.setStyle("-fx-background-image: url('" + imageUrl + "'); -fx-background-size: cover; -fx-background-position: center center; -fx-background-repeat: no-repeat;");
        } catch (Exception e) {
            root.setStyle("-fx-background-color: #f8f9fa;");
        }
        root.setPadding(new Insets(15));
        root.setTop(creaBarraSelezioneDataOra());

        ScrollPane scrollPane = new ScrollPane(creaMappaSpiaggia());
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        root.setCenter(scrollPane);
        root.setRight(creaPannelloDestro());

        this.scenaPrincipale = new Scene(root, 1000, 650);
        stage.setTitle("Mappa Stabilimento Balneare - Lido");
        stage.setScene(scenaPrincipale);
        stage.setMinWidth(900);
        stage.setMinHeight(550);
        stage.show();
    }

    private HBox creaBarraSelezioneDataOra() {
        HBox bar = new HBox(10);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(0, 0, 15, 0));

        Label lblData = new Label("Data:"); lblData.setStyle("-fx-font-weight: bold;");
        datePickerPrincipale = new DatePicker(LocalDate.now()); datePickerPrincipale.setPrefWidth(140);

        Label lblOra = new Label("Ora (HH:mm):"); lblOra.setStyle("-fx-font-weight: bold;");
        txtOraPrincipale = new TextField(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
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
                if (data != null) onCambioDataOraMappa.accept(data, ora);
            } catch (Exception ex) {
                mostraMessaggioEsito(false, "", "Formato ora non valido (es. 10:00).");
            }
        }
    }

    private Node creaMappaSpiaggia() {
        HBox containerMappa = new HBox(15.0);
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
        int r = 0, c = 0;
        for (MappaDAO.MappaOmbrelloneInfo cella : infoCelle) {
            Button btnOmbrellone = new Button(String.valueOf(cella.getNumero()));
            btnOmbrellone.setPrefSize(52, 38);
            btnOmbrellone.setFont(Font.font(10));
            impostaStatoCell(btnOmbrellone, cella.isOccupato());
            btnOmbrellone.setOnAction(e -> {
                if (onCellaOmbrelloneClicked != null) onCellaOmbrelloneClicked.accept(cella.getNumero());
            });
            gridOmbrelloni.add(btnOmbrellone, c, r);
            if (++c >= 10) { c = 0; r++; }
        }
    }

    private void impostaStatoCell(Button button, boolean isOccupato) {
        button.setStyle(isOccupato
                ? "-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 4;"
                : "-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 4;");
    }

    public void mostraCampiOccupati(List<PrenotazioneCampo> campiOccupati) {
        List<String> codici = campiOccupati.stream().map(PrenotazioneCampo::getCodCampo).toList();
        for (Map.Entry<String, Button> entry : bottoniCampiMap.entrySet()) {
            impostaStatoCell(entry.getValue(), codici.contains(entry.getKey()));
        }
    }

    public void mostraDettagliPrenotazioneCella(int codZona, String nomeZona, int codPrenotazione, String clientePrenotato) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Dettagli Ombrellone Occupato");
        alert.setHeaderText("Ombrellone Occupato");
        alert.setContentText("Zona: " + nomeZona + " (" + codZona + ")\nCodice Prenotazione: " + codPrenotazione + "\nCliente: " + clientePrenotato);
        alert.showAndWait();
    }

    public void mostraMessaggioEsito(boolean successo, String msgSuccesso, String msgErrore) {
        Alert alert = new Alert(successo ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(successo ? "Operazione completata" : "Errore");
        alert.setHeaderText(null);
        alert.setContentText(successo ? msgSuccesso : msgErrore);
        alert.showAndWait();
    }

    private VBox creaPannelloDestro() {
        VBox pannello = new VBox(15);
        pannello.setPadding(new Insets(5, 5, 5, 20));
        pannello.setAlignment(Pos.TOP_CENTER);
        pannello.setPrefWidth(190);

        Button btnOmbrelloniMenu = creaPulsanteSezione("OMBRELLONI", 75);
        btnOmbrelloniMenu.setOnAction(e -> ombrelloniView.mostraMenu());

        Button btnAttrezzature = creaPulsanteSezione("ATTREZZATURE", 75);
        btnAttrezzature.setOnAction(e -> attrezzatureCampiView.mostraMenuAttrezzature());

        Button btnAltro = creaPulsanteSezione("⚙ ALTRO / SETUP", 75);
        btnAltro.setOnAction(e -> amministrazioneView.mostraMenu());

        VBox boxCampi = new VBox(8);
        boxCampi.setAlignment(Pos.CENTER);
        Label lblCampiTitle = new Label("CAMPI SPORTIVI");
        lblCampiTitle.setFont(new Font("System Bold", 12));

        Button btnBeachVolley = creaPulsanteCampo("BEACH VOLLEY");
        Button btnBeachTennis = creaPulsanteCampo("BEACH TENNIS");
        Button btnCalcetto = creaPulsanteCampo("CALCETTO");
        Button btnBocceSabbia = creaPulsanteCampo("BOCCE SABBIA");
        Button btnBocceCemento = creaPulsanteCampo("BOCCE CEMENTO");
        Button btnCentroContabilita = creaPulsanteCampo("CENTRO CONTABILITÀ");
        Button btnStatistiche = creaPulsanteCampo("STATISTICHE");

        bottoniCampiMap.put("C1", btnBeachVolley);
        bottoniCampiMap.put("C2", btnBeachTennis);
        bottoniCampiMap.put("C3", btnCalcetto);
        bottoniCampiMap.put("C4", btnBocceSabbia);
        bottoniCampiMap.put("C5", btnBocceCemento);
        for (Button btn : bottoniCampiMap.values()) impostaStatoCell(btn, false);

        btnBeachVolley.setOnAction(e -> attrezzatureCampiView.mostraMenuCampi());
        btnBeachTennis.setOnAction(e -> attrezzatureCampiView.mostraMenuCampi());
        btnCalcetto.setOnAction(e -> attrezzatureCampiView.mostraMenuCampi());
        btnBocceSabbia.setOnAction(e -> attrezzatureCampiView.mostraMenuCampi());
        btnBocceCemento.setOnAction(e -> attrezzatureCampiView.mostraMenuCampi());

        btnCentroContabilita.setOnAction(e -> { if (onVisualizzaContabilitaAction != null) onVisualizzaContabilitaAction.run(); });
        btnStatistiche.setOnAction(e -> contabilitaStatisticheView.mostraMenuStatistiche());

        boxCampi.getChildren().addAll(lblCampiTitle, btnBeachVolley, btnBeachTennis, btnCalcetto, btnBocceSabbia, btnBocceCemento, btnCentroContabilita, btnStatistiche);
        pannello.getChildren().addAll(btnOmbrelloniMenu, btnAttrezzature, boxCampi, btnAltro);
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