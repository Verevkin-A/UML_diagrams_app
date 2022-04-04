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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
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

    public ArrayList<Button> addFieldButtons;
    public ArrayList<Button> addMethodButtons;
    public ArrayList<Button> deleteRowButtons;

    public Controller(AnchorPane root) {
        // set json extension filter
        this.fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        // set buttons handler
        this.menuItemLoad.setOnAction(this);
        this.menuItemSave.setOnAction(this);
        this.menuItemHelp.setOnAction(this);
        this.menuItemCredits.setOnAction(this);

        this.buttonCreateClass.setOnAction(this);

        this.addFieldButtons = new ArrayList<>();
        this.addMethodButtons = new ArrayList<>();
        this.deleteRowButtons = new ArrayList<>();

        this.root = root;
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
            // add all classes
            for (int i = 0; i < this.cd.classesLen(); i++) {
                CDClass clsToAdd = this.cd.getCDClass(i);
                this.addClass(this.root, clsToAdd);
            }
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
        } else if (actionEvent.getSource() == this.buttonCreateClass) {
            // TODO create class
            addClass(this.root, new CDClass());
        } else if (this.addFieldButtons.contains(actionEvent.getSource())) {
            this.addNewField((GridPane) ((Node) actionEvent.getSource()).getParent());
        } else if (this.addMethodButtons.contains(actionEvent.getSource())) {
            this.addNewMethod((GridPane) ((Node) actionEvent.getSource()).getParent());
        } else if (this.deleteRowButtons.contains(actionEvent.getSource())) {
            this.deleteRow((Button) actionEvent.getSource());
        }
    }

    public void addClass(AnchorPane root, CDClass cls) {
        TitledPane titledPane = new TitledPane();

        titledPane.setContent(this.populateClass(cls));

        titledPane.setText(cls.getName());
        titledPane.setExpanded(false);
        titledPane.setAnimated(true);

        for (Node node: root.getChildren()) {
            if (node instanceof ScrollPane) {
                Node vbox = ((ScrollPane) node).getContent();
                ((VBox) vbox).getChildren().add(titledPane);
                break;
            }
        }
    }

    public GridPane populateClass(CDClass cls) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        // set row counter
        int cnt = 0;
        // create add add field button
        Button addField = new Button("Add");
        addField.setOnAction(this);
        this.addFieldButtons.add(addField);
        // add fields row
        gridPane.addRow(cnt, new Label("Attributes:"), addField);
        cnt++;
        for (CDField field: cls.getFields()) {
            // crete field name
            TextField textField = new TextField(field.getName());
            textField.setPrefColumnCount(10);
            // create delete button
            Button delField = new Button("Del");
            delField.setOnAction(this);
            this.deleteRowButtons.add(delField);
            // add field row
            gridPane.addRow(cnt, textField, delField);
            cnt++;
        }
        // create add method button
        Button addMethod = new Button("Add");
        addMethod.setOnAction(this);
        this.addMethodButtons.add(addMethod);
        // add methods row
        gridPane.addRow(cnt, new Label("Methods:"), addMethod);
        cnt++;
        for (CDField method: cls.getMethods()) {
            // create method name
            TextField textField = new TextField(method.getName());
            textField.setPrefColumnCount(10);
            // create delete button
            Button delMethod = new Button("Del");
            delMethod.setOnAction(this);
            this.deleteRowButtons.add(delMethod);
            // add method row
            gridPane.addRow(cnt, textField, delMethod);
            cnt++;
        }

        return gridPane;
    }

    public void addNewField(GridPane gridPane) {
        boolean incRows = false;
        // move other rows
        for (Node child: gridPane.getChildren()) {
            out.println(GridPane.getRowIndex(child));
            if (!incRows && child instanceof Label && Objects.equals(((Label) child).getText(), "Methods:")) {
                incRows = true;
                out.println("here");
            }
            if (incRows) {
                Integer index = GridPane.getRowIndex(child);
                GridPane.setRowIndex(child, index + 1);
            }
        }

        TextField textField = new TextField("");
        textField.setPrefColumnCount(10);

        Button delField = new Button("Del");
        delField.setOnAction(this);
        this.deleteRowButtons.add(delField);

        gridPane.addRow(1, textField, delField);
        out.println("Method");
    }

    public void addNewMethod(GridPane gridPane) {
        TextField textField = new TextField("");
        textField.setPrefColumnCount(10);

        Button delMethod = new Button("Del");
        delMethod.setOnAction(this);
        this.deleteRowButtons.add(delMethod);

        gridPane.addRow(gridPane.getChildren().size() / 2, textField, delMethod);

        out.println("Method");
    }

    public void deleteRow(Button button) {
        for (Node child: ((GridPane) ((Node) button).getParent()).getChildren()) {
            // TODO
        }
    }

    public void refresh() {
        // TODO needed?
    }
}
