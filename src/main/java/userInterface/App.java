package userInterface;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import userInterface.CDInterface.SetWindow;


/**
 * Graphical user interface starter
 * @author Aleksandr Verevkin (xverev00)
 * @since 2022-04-02
 */
public class App extends Application {
    public Stage stage;

    /**
     * Executed on the app start
     * @param stage application main stage
     */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        // create and initialize starting window
        SetWindow window = new SetWindow();
        AnchorPane root = window.createWindow();

        Scene scene = new Scene(root, 1200, 800);
        stage.setTitle("UML Editor");
        stage.setResizable(false);
        // close all opened windows on main window close
        stage.onCloseRequestProperty().setValue(e -> Platform.exit());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Application launch
     * @param args arguments
     */
    public static void main(String[] args) {
        // launch application
        launch();
    }
}
