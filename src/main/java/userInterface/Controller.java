package userInterface;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.lang.Math;

import classDiagram.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Shape;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import parser.Parser;

/**
 * Main window controller
 * @author Aleksandr Verevkin (xverev00)
 * @since 2022-04-02
 */
public class Controller implements EventHandler<ActionEvent> {
    public AnchorPane root;
    public GridPane gridPaneClasses;
    public GridPane gridPaneNodes;

    private final FileChooser fileChooser = new FileChooser();
    // Menu buttons
    public MenuItem menuItemLoad = new MenuItem("Load");
    public MenuItem menuItemSave = new MenuItem("Save");
    public MenuItem menuItemHelp = new MenuItem("Help");
    public MenuItem menuItemCredits = new MenuItem("Credits");

    public final ToggleButton buttonCreateClass = new ToggleButton("New class");
    public final Button buttonCreateNode = new Button("New node");
    public UIClassConnector connectorEditing = null;

    public double axisX = 0.0;
    public double axisY = 0.0;

    public ArrayList<UIClassConnector> uiClassConnectors;
    public ArrayList<UINodeConnector> uiNodeConnectors;
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

        this.buttonCreateNode.setOnAction(this);

        uiClassConnectors = new ArrayList<>();
        uiNodeConnectors = new ArrayList<>();
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

    public void setGridPanes(GridPane gridPaneClasses, GridPane gridPaneNodes) {
        this.gridPaneClasses = gridPaneClasses;
        this.gridPaneNodes = gridPaneNodes;
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
        } else if (actionEvent.getSource() == this.buttonCreateNode) {
            addNode();
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
            for (UIClassConnector c: uiClassConnectors) {
                if (event.getSource() == c.getBtnDelete()) {
                    root.getChildren().remove(c.getTpClass());
                    gridPaneClasses.getChildren().removeAll(c.getClassNameLabel(), c.getBtnEdit(), c.getBtnDelete());
                    uiClassConnectors.remove(c);
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
            for (UIClassConnector c: uiClassConnectors) {
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
        for (UIClassConnector c: uiClassConnectors) {
            root.getChildren().remove(c.getTpClass());
            gridPaneClasses.getChildren().removeAll(c.getClassNameLabel(), c.getBtnEdit(), c.getBtnDelete());
        }
        uiClassConnectors.clear();
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
        for (UIClassConnector connector: uiClassConnectors) {
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

    public void editClass(UIClassConnector uiConnector) {
        // set class x and y
        axisX = uiConnector.getAxisX();
        axisY = uiConnector.getAxisY();
        // open form window
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("classForm.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());

            TextField textField = (TextField) scene.lookup("#tfClassName");
            textField.setText(uiConnector.getClassNameLabel().getText());   // set class name in form

            TableView<FormField> tableView = (TableView<FormField>) scene.lookup("#tvFields");
            ToggleButton tbInterface =  (ToggleButton) scene.lookup("#tbInterface");
            tbInterface.setSelected(uiConnector.getInterface_());

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
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("classForm.fxml"));
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
            gridPaneClasses.getChildren().removeAll(connectorEditing.getClassNameLabel(), connectorEditing.getBtnEdit(), connectorEditing.getBtnDelete());
            uiClassConnectors.remove(connectorEditing);
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
        // add class on the pane
        root.getChildren().add(titledPane);

        Button btnClassEdit = new Button("Edit");
        Button btnClassDelete = new Button("Delete");
        btnClassEdit.setOnAction(editClass);
        btnClassDelete.setOnAction(deleteClass);
        // create place for new row
        for (Node child : gridPaneClasses.getChildren()) {
            GridPane.setRowIndex(child, GridPane.getRowIndex(child) + 1);
        }
        // insert new class row
        Label nameLabel = new Label(className);
        gridPaneClasses.addRow(0, nameLabel, btnClassEdit, btnClassDelete);

        uiClassConnectors.add(new UIClassConnector(titledPane, axisX, axisY, interface_, tableView, nameLabel, btnClassEdit, btnClassDelete));
    }

    public void addNode() {
        // open form window
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("nodeForm.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Node form");
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void putNode(UIClassConnector fromClass, UIClassConnector toClass, AnchorType anchorFrom, AnchorType anchorTo,
                        String fCard, String tCard, NodeType nodeType) {
        TitledPane tpFrom = fromClass.getTpClass();
        Bounds fromBounds = tpFrom.localToScene(tpFrom.getBoundsInLocal());
        TitledPane tpTo = toClass.getTpClass();
        Bounds toBounds = tpTo.localToScene(tpTo.getBoundsInLocal());
        double[] fCrd = getLineXY(fromBounds, anchorFrom);
        double[] tCrd = getLineXY(toBounds, anchorTo);
        Line node = new Line(fCrd[0], fCrd[1], tCrd[0], tCrd[1]);

        Shape arrowHead = getArrowHead(nodeType, fCrd, tCrd);

        root.getChildren().addAll(node, arrowHead);
    }

    private Shape getArrowHead(NodeType nodeType, double[] fCrd, double[] tCrd) {
        double L1 = 15;     // arrow head wings length
        double L2 = Math.sqrt((tCrd[1] - fCrd[1]) * (tCrd[1] - fCrd[1]) + (tCrd[0] - fCrd[0]) * (tCrd[0] - fCrd[0]));

        double arrowX1 = tCrd[0] + L1 * ((fCrd[0] - tCrd[0]) * (Math.sqrt(3)/2) + (fCrd[1] - tCrd[1]) * (Math.sqrt(1)/2)) / L2;
        double arrowY1 = tCrd[1] + L1 * ((fCrd[1] - tCrd[1]) * (Math.sqrt(3)/2) - (fCrd[0] - tCrd[0]) * (Math.sqrt(1)/2)) / L2;
        double arrowX2 = tCrd[0] + L1 * ((fCrd[0] - tCrd[0]) * (Math.sqrt(3)/2) - (fCrd[1] - tCrd[1]) * (Math.sqrt(1)/2)) / L2;
        double arrowY2 = tCrd[1] + L1 * ((fCrd[1] - tCrd[1]) * (Math.sqrt(3)/2) + (fCrd[0] - tCrd[0]) * (Math.sqrt(1)/2)) / L2;

        switch (nodeType.getNumVal()) {
            case 0:     // aggregation
                return null;
            case 1:     // association
                return new Polyline(arrowX1, arrowY1, tCrd[0], tCrd[1], arrowX2, arrowY2);
            case 2:     // generalization
                Polygon polygon = new Polygon(arrowX1, arrowY1, tCrd[0], tCrd[1], arrowX2, arrowY2);
                polygon.setFill(Color.WHITESMOKE);
                polygon.setStroke(Color.BLACK);
                return polygon;
            default:    // composition
                return null;
        }
    }

    private double[] getLineXY (Bounds bounds, AnchorType anchor) {
        switch (anchor.getSymb()) {
            case "LEFT":
                return new double[] {bounds.getMinX(), bounds.getCenterY()};
            case "RIGHT":
                return new double[] {bounds.getMaxX(), bounds.getCenterY()};
            case "DOWN":
                return new double[] {bounds.getCenterX(), bounds.getMaxY()};
            default:    // "UP"
                return new double[] {bounds.getCenterX(), bounds.getMinY()};
        }
    }



//    public void putClassAnchors(TitledPane titledPane) {
//        PauseTransition wait = new PauseTransition(Duration.seconds(0.01));
//        wait.setOnFinished((e) -> {
//            Bounds tpBounds = titledPane.localToScene(titledPane.getBoundsInLocal());
//
//            btnAnchorUP.setLayoutX(tpBounds.getCenterX() - 15);
//            btnAnchorUP.setLayoutY(tpBounds.getMinY() - 20);
//
//            btnAnchorDOWN.setLayoutX(tpBounds.getCenterX() - 15);
//            btnAnchorDOWN.setLayoutY(tpBounds.getMaxY() - 10);
//
//            btnAnchorLEFT.setLayoutX(tpBounds.getMinX() - 20);
//            btnAnchorLEFT.setLayoutY(tpBounds.getCenterY() - 15);
//
//            btnAnchorRIGHT.setLayoutX(tpBounds.getMaxX() - 10);
//            btnAnchorRIGHT.setLayoutY(tpBounds.getCenterY() - 15);
//
//            Line line = new Line(btnAnchorUP.localToScene(btnAnchorUP.getBoundsInLocal()).getCenterX() + 15,
//                    btnAnchorUP.localToScene(btnAnchorUP.getBoundsInLocal()).getCenterY() + 15,
//                    btnAnchorUP.getLayoutX() + 100, 500);
//            Line line2 = new Line(btnAnchorUP.localToScene(btnAnchorUP.getBoundsInLocal()).getCenterX() + 15,
//                    btnAnchorUP.localToScene(btnAnchorUP.getBoundsInLocal()).getCenterY() + 15,
//                    btnAnchorUP.getLayoutX() - 100, 500);
//            line2.getStrokeDashArray().addAll(10d, 10d);
////            root.getChildren().addAll(line, line2);
//        });
//        wait.play();
//    }
}
