package userInterface;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;



/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) {
        SetWindow window = new SetWindow();
        AnchorPane root = window.createWindow();

        scene = new Scene(root, 1200, 800);
        stage.setTitle("Random Title");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}