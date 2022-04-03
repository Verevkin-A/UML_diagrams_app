package userInterface;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // create and initialize starting window
        Scene scene = new Scene(loadFXML(), 1200, 800);
        stage.setTitle("UML Editor");
        stage.setScene(scene);
        stage.show();
    }

    private static Parent loadFXML() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("mainWindow.fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        // launch application
        launch();
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