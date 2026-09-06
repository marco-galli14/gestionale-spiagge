import controller.MainController;
import javafx.application.Application;
import javafx.stage.Stage;
import view.MainView;

public class GestionaleSpiaggia extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // 1. Istanziamo la View passandole lo Stage principale
            MainView view = new MainView(primaryStage);
            
            // 2. FONDAMENTALE: Costruiamo la grafica PRIMA del controller.
            // In questo modo la 'gridOmbrelloni' e tutti gli altri componenti
            // vengono creati fisicamente in memoria.
            view.mostraFinestra();
            
            // 3. ORA creiamo il Controller.
            // Il controller si aggancerà agli eventi e caricherà i dati
            // in una griglia che ora esiste davvero.
            MainController controller = new MainController(view);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Errore critico durante l'avvio dell'applicazione.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}