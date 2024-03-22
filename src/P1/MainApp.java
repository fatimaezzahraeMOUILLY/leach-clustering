package P1;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("LEACH Clustering Example");

        // Créer une nouvelle instance de LeachUI avec son propre GridPane
        LeachUI leachUI = new LeachUI(primaryStage);

    }
}
