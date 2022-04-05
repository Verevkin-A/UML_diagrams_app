package userInterface;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

import classDiagram.CDClass;
import classDiagram.CDField;
import classDiagram.ClassDiagram;
import controllers.Command;
import controllers.ToolState;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import parser.Parser;

import static java.lang.System.out;

public class Controller implements EventHandler<ActionEvent> {
    public AnchorPane root;
    private ClassDiagram cd = new ClassDiagram();

    private final FileChooser fileChooser = new FileChooser();

    public MenuItem menuItemLoad = new MenuItem("Load");
    public MenuItem menuItemSave = new MenuItem("Save");
    public MenuItem menuItemHelp = new MenuItem("Help");
    public MenuItem menuItemCredits = new MenuItem("Credits");

    public final Button buttonCreateClass = new Button("New class");
    public boolean classCreation;

    public ArrayList<Button> addFieldButtons;
    public ArrayList<Button> addMethodButtons;
    public ArrayList<Button> deleteRowButtons;

    public Controller(AnchorPane root) {
        this.root = root;
        this.root.setOnMouseClicked(this.canvasMousePress);
        // set json extension filter
        this.fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        // set buttons handler
        this.menuItemLoad.setOnAction(this);
        this.menuItemSave.setOnAction(this);
        this.menuItemHelp.setOnAction(this);
        this.menuItemCredits.setOnAction(this);

        this.buttonCreateClass.setOnAction(createClass);
        this.classCreation = false;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        System.out.println(actionEvent.getSource());
        if (actionEvent.getSource() == menuItemLoad) {
            File file = this.fileChooser.showOpenDialog(null);

            System.out.println(file.getAbsolutePath());     // TODO call read method
            // read input
            try {
                String filepath = file.getAbsolutePath();
                String diagString = Files.readString(Paths.get(filepath));
                this.cd = Parser.decodeJSON(diagString);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // TODO add all classes
        } else if (actionEvent.getSource() == this.menuItemSave) {
            File file = this.fileChooser.showSaveDialog(null);
            // check if file have extension
            if (!file.getName().contains(".")) {
                file = new File(file.getAbsolutePath() + ".json");
            }

            System.out.println(file.getAbsolutePath());     // TODO call save method
            try {
                FileWriter outFile = new FileWriter(file.getAbsolutePath());
                String output = Parser.encodeJSON(this.cd);
                outFile.write(output);
                outFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (actionEvent.getSource() == this.menuItemHelp) {
            System.out.println("Help");     // TODO help window
        } else if (actionEvent.getSource() == this.menuItemCredits) {
            System.out.println("Credits");      // TODO credits window
        }
    }

    public EventHandler<ActionEvent> createClass = new EventHandler<ActionEvent>()
    {
        @Override
        public void handle(ActionEvent event)
        {
            classCreation = true;
        }
    };

    public EventHandler<MouseEvent> canvasMousePress = new EventHandler<MouseEvent>()
    {
        @Override
        public void handle(MouseEvent event)
        {
            if (event.getTarget() == event.getSource())
            {
                if (classCreation) {
                    System.out.println(event.getX());
                    System.out.println(event.getY());
                    addClass(event.getX(), event.getY());

                    classCreation = false;
                } else {
                    System.out.println("CANVAS: canvas clicked, no action");
                }
            }
        }
    };

    public void addClass(double axisX, double axisY) {

        Stage stage = new Stage();
        // create and initialize starting window
        AnchorPane root = new AnchorPane();

        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("New window");
        stage.setScene(scene);
        stage.show();


        TitledPane titledPane = new TitledPane();
        titledPane.setText("Class");
        titledPane.setCollapsible(false);

        AnchorPane.setLeftAnchor(titledPane, axisX);
        AnchorPane.setTopAnchor(titledPane, axisY);

        this.root.getChildren().add(titledPane);
    }

//    public void addClass(AnchorPane root, CDClass cls) {
//        TitledPane titledPane = new TitledPane();
//
//        titledPane.setContent(this.populateClass(cls));
//
//        titledPane.setText(cls.getName());
//        titledPane.setExpanded(false);
//        titledPane.setAnimated(true);
//
//        for (Node node: root.getChildren()) {
//            if (node instanceof ScrollPane) {
//                Node vbox = ((ScrollPane) node).getContent();
//                ((VBox) vbox).getChildren().add(titledPane);
//                break;
//            }
//        }
//    }
}
