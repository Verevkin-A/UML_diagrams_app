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
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import parser.Parser;

import static java.lang.System.out;

public class Controller implements EventHandler<ActionEvent> {
    public AnchorPane root;
    public GridPane classesGridPane;
    private ClassDiagram cd = new ClassDiagram();

    private final FileChooser fileChooser = new FileChooser();

    public MenuItem menuItemLoad = new MenuItem("Load");
    public MenuItem menuItemSave = new MenuItem("Save");
    public MenuItem menuItemHelp = new MenuItem("Help");
    public MenuItem menuItemCredits = new MenuItem("Credits");

    public final Button buttonCreateClass = new Button("New class");
    public boolean classCreation;

    public double axisX = 0.0;
    public double axisY = 0.0;

    private static Controller _controller;

    private Controller(AnchorPane root) {
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

    public static Controller setController(AnchorPane root) {
        if (_controller == null) {
            _controller = new Controller(root);
        }
        return _controller;
    }

    public static Controller getController() {
        return _controller;
    }

    public void setGridPane(GridPane gridPane) {
        this.classesGridPane = gridPane;
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
                    if (event.getX() > 930) {
                        System.out.println("CANVAS: canvas clicked, restricted section");
                    } else {
                        axisX = event.getX();
                        axisY = event.getY();
                        addClass();

                        classCreation = false;
                    }
                } else {
                    System.out.println("CANVAS: canvas clicked, no action");
                }
            }
        }
    };

    public void addClass() {
        // open form window
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("formWindow.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Class form");
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void putClass(String className, Boolean interface_, TableView<FormField> tableView) {
        VBox vBox = new VBox(10.0);
        for (FormField ff: tableView.getItems()) {
            if (Objects.equals(ff.getType(), "Field")) {
                vBox.getChildren().add(new Label(ff.getVisibilitySymbol() + ff.getName()));
            }
        }
        for (FormField ff: tableView.getItems()) {
            if (Objects.equals(ff.getType(), "Method")) {
                vBox.getChildren().add(new Label(ff.getVisibilitySymbol() + ff.getName() + "()"));
            }
        }
        TitledPane titledPane = new TitledPane(String.format("%s%s", (interface_? "<<interface>>\n" : ""), className), vBox);
        titledPane.setCollapsible(false);

        AnchorPane.setLeftAnchor(titledPane, axisX);
        AnchorPane.setTopAnchor(titledPane, axisY);

        this.root.getChildren().add(titledPane);

        Button btnClassEdit = new Button("Edit");
        Button btnClassDelete = new Button("Delete");
        // create place for new row
        for (Node child : classesGridPane.getChildren()) {
            GridPane.setRowIndex(child, GridPane.getRowIndex(child) + 1);
        }
        // insert new row
        classesGridPane.addRow(0, new Label(className), btnClassEdit, btnClassDelete);
    }
}
