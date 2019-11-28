package arena.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class to start application
 * @author Aaron WANG
 */
public class Main extends Application {

    /**
     * start the arena with FXML
     * @param primaryStage primary FXML window
     * @throws Exception errors
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/arena.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Tower Defence");
        primaryStage.setScene(new Scene(root, 680, 480));
        primaryStage.show();
        ArenaUI appController = loader.getController();
        appController.createArena();
    }


    /**
     * main function
     * @param args arguments to be passed to main
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Default Constructor
     */
    public Main() {
    }
}
