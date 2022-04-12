package userInterface;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

import classDiagram.CDClass;
import classDiagram.CDField;
import classDiagram.ClassDiagram;
import classDiagram.Visibility;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import parser.Parser;

import static java.lang.System.out;

/**
 * Main window controller
 * @author Aleksandr Verevkin (xverev00)
 * @since 2022-04-02
 */
public class Controller implements EventHandler<ActionEvent> {
    public AnchorPane root;
    public GridPane classesGridPane;

    private final FileChooser fileChooser = new FileChooser();
    // Menu buttons
    public MenuItem menuItemLoad = new MenuItem("Load");
    public MenuItem menuItemSave = new MenuItem("Save");
    public MenuItem menuItemHelp = new MenuItem("Help");
    public MenuItem menuItemCredits = new MenuItem("Credits");

    public final ToggleButton buttonCreateClass = new ToggleButton("New class");
    public UIConnector connectorEditing = null;

    public double axisX = 0.0;
    public double axisY = 0.0;

    public ArrayList<UIConnector> uiConnectors;
    // controller singleton instance
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

        uiConnectors = new ArrayList<>();
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
        if (actionEvent.getSource() == menuItemLoad) {
            File file = this.fileChooser.showOpenDialog(null);
            ClassDiagram cd = new ClassDiagram();
            // read input
            try {
                String filepath = file.getAbsolutePath();
                String diagString = Files.readString(Paths.get(filepath));
                cd = Parser.decodeJSON(diagString);
            } catch (IOException e) {
                e.printStackTrace();
            }

            clearScreen();      // clear screen and memory from all classes
            loadClasses(cd);      // load class diagram
        } else if (actionEvent.getSource() == this.menuItemSave) {
            ClassDiagram classes = saveClasses();

            File file = this.fileChooser.showSaveDialog(null);
            // check if file have extension
            if (!file.getName().contains(".")) {
                file = new File(file.getAbsolutePath() + ".json");
            }

            System.out.println(file.getAbsolutePath());
            try {
                FileWriter outFile = new FileWriter(file.getAbsolutePath());
                String output = Parser.encodeJSON(classes);
                outFile.write(output);
                outFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (actionEvent.getSource() == this.menuItemHelp) {
            openHelp();
        } else if (actionEvent.getSource() == this.menuItemCredits) {
            openCredits();
        }
    }

    private void openHelp() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("helpWindow.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Help");
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openCredits() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("creditsWindow.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Credits");
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public EventHandler<ActionEvent> deleteClass = new EventHandler<ActionEvent>()
    {
        @Override
        public void handle(ActionEvent event)
        {
            for (UIConnector c: uiConnectors) {
                if (event.getSource() == c.getBtnDelete()) {
                    root.getChildren().remove(c.getTpClass());
                    classesGridPane.getChildren().removeAll(c.getClassNameLabel(), c.getBtnEdit(), c.getBtnDelete());
                    uiConnectors.remove(c);
                    break;
                }
            }
        }
    };

    public EventHandler<ActionEvent> editClass = new EventHandler<ActionEvent>()
    {
        @Override
        public void handle(ActionEvent event)
        {
            for (UIConnector c: uiConnectors) {
                if (event.getSource() == c.getBtnEdit()) {
                    editClass(c);
                    connectorEditing = c;
                    break;
                }
            }
        }
    };

    public EventHandler<MouseEvent> canvasMousePress = new EventHandler<MouseEvent>()
    {
        @Override
        public void handle(MouseEvent event)
        {
            if (event.getTarget() == event.getSource())
            {
                if (buttonCreateClass.isSelected()) {
                    if (event.getX() > 930) {
                        System.out.println("canvas clicked, restricted section");
                    } else {
                        axisX = event.getX();
                        axisY = event.getY();
                        addClass();
                        buttonCreateClass.setSelected(false);
                    }
                }
            }
        }
    };

    public void clearScreen() {
        for (UIConnector c: uiConnectors) {
            root.getChildren().remove(c.getTpClass());
            classesGridPane.getChildren().removeAll(c.getClassNameLabel(), c.getBtnEdit(), c.getBtnDelete());
        }
        uiConnectors.clear();
    }

    public void loadClasses(ClassDiagram cd) {
        for (int i = 0; i < cd.classesLen(); i++) {
            CDClass clsToAdd = cd.getCDClass(i);
            axisX = clsToAdd.getXposition();
            axisY = clsToAdd.getYposition();
            TableView<FormField> tableView = new TableView<FormField>();
            TableColumn<FormField, String> nameColumn = new TableColumn<FormField, String>("Name");
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            TableColumn<FormField, String> typeColumn = new TableColumn<FormField, String>("Type");
            typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            TableColumn<FormField, String> visibilityColumn = new TableColumn<FormField, String>("Visibility");
            visibilityColumn.setCellValueFactory(new PropertyValueFactory<>("visibility"));

            tableView.getColumns().add(nameColumn);
            tableView.getColumns().add(typeColumn);
            tableView.getColumns().add(visibilityColumn);
            for (CDField method: clsToAdd.getMethods()) {
                tableView.getItems().add(new FormField(method.getName(), "Method", method.getVisibility().getName()));
            }
            for (CDField method: clsToAdd.getFields()) {
                tableView.getItems().add(new FormField(method.getName(), "Field", method.getVisibility().getName()));
            }
            putClass(clsToAdd.getName(), clsToAdd.getInterface(), tableView);
        }
    }

    public ClassDiagram saveClasses() {
        ClassDiagram cd = new ClassDiagram();
        for (UIConnector connector: uiConnectors) {
            ArrayList<CDField> fields = new ArrayList<>();
            ArrayList<CDField> methods = new ArrayList<>();
            for (FormField ff: connector.getTableView().getItems()) {
                if (Objects.equals(ff.getType(), "Field")) {
                    fields.add(new CDField(ff.getName(), Visibility.valueOfLabel(ff.getVisibilitySymbol())));
                }
                if (Objects.equals(ff.getType(), "Method")) {
                    methods.add(new CDField(ff.getName(), Visibility.valueOfLabel(ff.getVisibilitySymbol())));
                }
            }
            // TODO Parent
            CDClass cdClass = new CDClass(connector.getClassNameLabel().getText(), 99, fields, methods,
                    connector.getInterface_(), connector.getAxisX().intValue(), connector.getAxisY().intValue(), 99, 99);
            cd.addClass(cdClass);
        }
        return cd;
    }

    public void editClass(UIConnector uiConnector) {
        // set class x and y
        axisX = uiConnector.getAxisX();
        axisY = uiConnector.getAxisY();
        // open form window
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("formWindow.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());

            TextField textField = (TextField) scene.lookup("#tfClassName");
            textField.setText(uiConnector.getClassNameLabel().getText());   // set class name in form

            TableView<FormField> tableView = (TableView<FormField>) scene.lookup("#tvFields");
            ToggleButton tbInterface =  (ToggleButton) scene.lookup("#tbInterface");
            if (uiConnector.getInterface_()) {
                tbInterface.setSelected(true);
            }
            tableView.getItems().addAll(uiConnector.getTableView().getItems());
            stage.setTitle("Class form");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addClass() {
        // open form window
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("formWindow.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Class form");
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void putClass(String className, Boolean interface_, TableView<FormField> tableView) {
        if (connectorEditing != null) {
            root.getChildren().remove(connectorEditing.getTpClass());
            classesGridPane.getChildren().removeAll(connectorEditing.getClassNameLabel(), connectorEditing.getBtnEdit(), connectorEditing.getBtnDelete());
            uiConnectors.remove(connectorEditing);
            connectorEditing = null;
        }
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

        root.getChildren().add(titledPane);

        Button btnClassEdit = new Button("Edit");
        Button btnClassDelete = new Button("Delete");
        btnClassEdit.setOnAction(editClass);
        btnClassDelete.setOnAction(deleteClass);
        // create place for new row
        for (Node child : classesGridPane.getChildren()) {
            GridPane.setRowIndex(child, GridPane.getRowIndex(child) + 1);
        }
        // insert new row
        Label nameLabel = new Label(className);
        classesGridPane.addRow(0, nameLabel, btnClassEdit, btnClassDelete);

        uiConnectors.add(new UIConnector(titledPane, axisX, axisY, interface_, tableView, nameLabel, btnClassEdit, btnClassDelete));
    }
}
