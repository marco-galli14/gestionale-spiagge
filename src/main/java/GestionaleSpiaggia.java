import controller.MainController;
import javafx.application.Application;
import javafx.stage.Stage;

public class GestionaleSpiaggia extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // 1. Creiamo il Controller passandogli lo Stage principale.
            // Il Controller si occuperà internamente di istanziare la MainView, 
            // collegare gli eventi e preparare i DAO.
            MainController controller = new MainController(primaryStage);
            
            // 2. Avviamo l'applicazione tramite il controller.
            // Questo metodo richiama view.mostraFinestra() costruendo la grafica
            // e subito dopo carica i dati nella griglia (celle degli ombrelloni, ecc.).
            controller.avvia();
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Errore critico durante l'avvio dell'applicazione.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}