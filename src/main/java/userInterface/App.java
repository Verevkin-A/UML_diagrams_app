package userInterface;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Set;


/**
 * JavaFX App
 */
public class App extends Application {
    public SetWindow window;
    public Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        // create and initialize starting window
        window = new SetWindow();
        AnchorPane root = window.createWindow();

        Scene scene = new Scene(root, 1200, 800);
        stage.setTitle("UML Editor");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        // launch application
        launch();
    }

    public void refreshWindow() {
        Scene scene = new Scene(this.window.root, 1200, 800);
        this.stage.setTitle("UML Editor");
        this.stage.setScene(scene);
        this.stage.show();
    }
}

//public class App extends Application
//{
//    public static void main(String[] args)
//    {
//        launch(args);
//    }
//
//    @Override
//    public void start(Stage primaryStage) throws Exception
//    {
//        AppCtrl.getAppController(primaryStage);
//    }
//}