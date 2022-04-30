package userInterface.CDInterface;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.lang.Math;

import classDiagram.*;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
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
import javafx.util.Duration;
import parser.Parser;
import sequenceDiagram.SequenceDiagram;
import userInterface.App;
import userInterface.SDInterface.SDController;

/**
 * Main window CDController
 * @author Aleksandr Verevkin (xverev00)
 * @since 2022-04-02
 */
public class CDController implements EventHandler<ActionEvent> {
    // main static objects
    public AnchorPane root;
    public GridPane gridPaneClasses;
    public GridPane gridPaneNodes;
    public GridPane gridPaneSD;

    private final FileChooser fileChooser = new FileChooser();
    // Menu buttons
    public MenuItem menuItemLoad = new MenuItem("Load");
    public MenuItem menuItemSave = new MenuItem("Save");
    public MenuItem menuItemHelp = new MenuItem("Help");
    public MenuItem menuItemCredits = new MenuItem("Credits");
    public MenuItem menuItemUndo = new MenuItem("Undo");
    // pane static buttons
    public final ToggleButton buttonCreateClass = new ToggleButton("New class");
    public final Button buttonCreateNode = new Button("New node");
    public final Button buttonCreateSD = new Button("New SD");
    // class editing flag
    public UIClassConnector connectorEditing = null;
    // current class coordinates
    public double axisX = 0.0;
    public double axisY = 0.0;
    // lists with depending on each other GUI objects
    public ArrayList<UIClassConnector> uiClassConnectors;
    public ArrayList<UINodeConnector> uiNodeConnectors;
    public ArrayList<UISDConnector> uiSDConnectors;
    // sequence diagrams counter
    private int SDIdentifNum = 1;
    // CDController singleton instance
    private static CDController CD_controller;

    private CDController(AnchorPane root) {
        this.root = root;
        this.root.setOnMouseClicked(this.canvasMousePress);
        // set json extension filter
        this.fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        // set buttons handler
        this.menuItemLoad.setOnAction(this);
        this.menuItemSave.setOnAction(this);
        this.menuItemHelp.setOnAction(this);
        this.menuItemCredits.setOnAction(this);
        this.menuItemUndo.setOnAction(this);

        this.buttonCreateNode.setOnAction(this);
        this.buttonCreateSD.setOnAction(this);
        // initialize UI connectors
        uiClassConnectors = new ArrayList<>();
        uiNodeConnectors = new ArrayList<>();
        uiSDConnectors = new ArrayList<>();
    }

    public static CDController setController(AnchorPane root) {
        if (CD_controller == null) {
            CD_controller = new CDController(root);
        }
        return CD_controller;
    }

    public static CDController getController() {
        return CD_controller;
    }

    public void setGridPanes(GridPane gridPaneClasses, GridPane gridPaneNodes, GridPane gridPaneSD) {
        this.gridPaneClasses = gridPaneClasses;
        this.gridPaneNodes = gridPaneNodes;
        this.gridPaneSD = gridPaneSD;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        if (actionEvent.getSource() == menuItemLoad) {
            // load class diagram from .json
            File file = this.fileChooser.showOpenDialog(null);
            ClassDiagram cd = new ClassDiagram();
            // read input
            try {
                String diagString = Files.readString(Paths.get(file.getAbsolutePath()));
                cd = Parser.decodeJSON(diagString);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // clear screen and memory from all classes
            clearScreen();
            // load class diagram
            loadClasses(cd);
            // wait before all classes are saved and load nodes
            PauseTransition wait = new PauseTransition(Duration.seconds(0.1));
            ClassDiagram finalCd = cd;
            wait.setOnFinished((e) -> loadNodes(finalCd));
            wait.play();
            // load sequence diagrams
            loadSDs(cd.getSequenceDiagrams());
        } else if (actionEvent.getSource() == this.menuItemSave) {
            // save class diagram into .json
            ClassDiagram cd = saveCD();
            saveSDs(cd);
            // show file chooser
            File file = this.fileChooser.showSaveDialog(null);
            // check if file have extension
            if (!file.getName().contains(".")) {
                file = new File(file.getAbsolutePath() + ".json");
            }
            // generate output json
            try {
                FileWriter outFile = new FileWriter(file.getAbsolutePath());
                outFile.write(Parser.encodeJSON(cd));
                outFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (actionEvent.getSource() == this.menuItemHelp) {
            openFXML("helpWindow.fxml", "Help");       // help menu
        } else if (actionEvent.getSource() == this.menuItemCredits) {
            openFXML("creditsWindow.fxml", "Credits");     // credits menu
        } else if (actionEvent.getSource() == this.buttonCreateNode) {
            addNode();      // add new node button
        } else if (actionEvent.getSource() == this.buttonCreateSD) {
            openFXML("SDWindow.fxml", "New Sequence Diagram");      // add new sequence diagram
        } else if (actionEvent.getSource() == this.menuItemUndo) {
            undo();     // undo last action
        }
    }

    private void openFXML(String fxml, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml));
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public EventHandler<ActionEvent> deleteClass = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            for (UIClassConnector cClass: uiClassConnectors) {
                if (event.getSource() == cClass.getBtnDelete()) {
                    root.getChildren().remove(cClass.getTpClass());
                    gridPaneClasses.getChildren().removeAll(cClass.getClassNameLabel(), cClass.getBtnEdit(), cClass.getBtnDelete());

                    // delete all nodes, connected with deleting class
                    ArrayList<UINodeConnector> nodesToDelete = new ArrayList<>();
                    for (UINodeConnector cNode: uiNodeConnectors) {
                        if (cNode.getFrom() == cClass || cNode.getTo() == cClass) {
                            root.getChildren().removeAll(cNode.getNode(), cNode.getArrowHead(), cNode.getfCard(), cNode.gettCard());
                            gridPaneNodes.getChildren().removeAll(cNode.getNodeNameLabel(), cNode.getBtnDelete());
                            nodesToDelete.add(cNode);
                        }
                    }

                    for (UINodeConnector cNode: nodesToDelete) {
                        // if deleted node type is generalization, remove possible highlighting on inherited methods
                        if (cNode.getNodeType() == NodeType.GENERALIZATION) {
                            for (Node fromField: cNode.getFrom().getVbFields().getChildren()) {
                                if (((Label) fromField).getText().endsWith("()") && ((Label) fromField).getTextFill() == Color.RED) {
                                    for (Node toField: cNode.getTo().getVbFields().getChildren())
                                        if (Objects.equals(((Label) fromField).getText(), ((Label) toField).getText())) {
                                            ((Label) fromField).setTextFill(Color.BLACK);
                                            break;
                                        }
                                }
                            }
                        }
                    }

                    uiNodeConnectors.removeAll(nodesToDelete);

                    uiClassConnectors.remove(cClass);
                    break;
                }
            }
        }
    };

    public EventHandler<ActionEvent> editClass = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            for (UIClassConnector c: uiClassConnectors) {
                if (event.getSource() == c.getBtnEdit()) {
                    editClass(c);
                    connectorEditing = c;
                    break;
                }
            }
        }
    };

    public EventHandler<ActionEvent> deleteNode = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            for (UINodeConnector c: uiNodeConnectors) {
                if (event.getSource() == c.getBtnDelete()) {
                    root.getChildren().removeAll(c.getNode(), c.getArrowHead(), c.getfCard(), c.gettCard());
                    gridPaneNodes.getChildren().removeAll(c.getNodeNameLabel(), c.getBtnDelete());

                    // if deleted node type is generalization, remove possible highlighting on inherited methods
                    if (c.getNodeType() == NodeType.GENERALIZATION) {
                        for (Node fromField: c.getFrom().getVbFields().getChildren()) {
                            if (((Label) fromField).getText().endsWith("()") && ((Label) fromField).getTextFill() == Color.RED) {
                                for (Node toField: c.getTo().getVbFields().getChildren())
                                    if (Objects.equals(((Label) fromField).getText(), ((Label) toField).getText())) {
                                        ((Label) fromField).setTextFill(Color.BLACK);
                                        break;
                                    }
                            }
                        }
                    }

                    uiNodeConnectors.remove(c);
                    break;
                }
            }
        }
    };

    public EventHandler<MouseEvent> canvasMousePress = event -> {
        if (event.getTarget() == event.getSource())
        {
            if (buttonCreateClass.isSelected()) {
                if (event.getX() > 930 || (event.getX() < 260 && event.getY() > 610)) {
                    System.out.println("Warning: pane clicked, restricted section");
                } else {
                    axisX = event.getX();
                    axisY = event.getY();
                    addClass();
                    buttonCreateClass.setSelected(false);
                }
            }
        }
    };

    public EventHandler<ActionEvent> deleteSD = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            for (UISDConnector c: uiSDConnectors) {
                if (event.getSource() == c.getbDeleteSD()) {
                    gridPaneSD.getChildren().removeAll(c.getlName(), c.getbEditSD(), c.getbDeleteSD());
                    uiSDConnectors.remove(c);
                    break;
                }
            }
        }
    };

    public EventHandler<ActionEvent> editSD = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            for (UISDConnector c: uiSDConnectors) {
                if (event.getSource() == c.getbEditSD()) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("SDWindow.fxml"));
                        Stage stage = new Stage();
                        stage.setTitle(c.getlName().getText());
                        Parent root = fxmlLoader.load();
                        // initialize diagram
                        SDController sdController = fxmlLoader.getController();
                        sdController.loadSD(c.getSequenceDiagram());

                        stage.setScene(new Scene(root));
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    };

    public void saveSD(SequenceDiagram sd) {
        // create row elements
        Label lName = new Label("SD #" + SDIdentifNum++);
        Button bEdit = new Button("Edit");
        bEdit.setOnAction(editSD);
        Button bDelete = new Button("Delete");
        bDelete.setOnAction(deleteSD);
        // push all rows, creating place for the new sequence diagram
        for (Node child: gridPaneSD.getChildren()) {
            GridPane.setRowIndex(child, GridPane.getRowIndex(child) + 1);
        }
        gridPaneSD.addRow(0, lName, bEdit, bDelete);
        // save diagram and his UI attributes in the memory
        uiSDConnectors.add(new UISDConnector(sd, lName, bEdit, bDelete));
    }

    public void undo() {
        // TODO
    }

    public void clearScreen() {
        // delete all classes
        for (UIClassConnector cClass: uiClassConnectors) {
            root.getChildren().remove(cClass.getTpClass());
            gridPaneClasses.getChildren().removeAll(cClass.getClassNameLabel(), cClass.getBtnEdit(), cClass.getBtnDelete());
        }
        uiClassConnectors.clear();
        // delete all nodes
        for (UINodeConnector cNode: uiNodeConnectors) {
            root.getChildren().removeAll(cNode.getNode(), cNode.getArrowHead(), cNode.getfCard(), cNode.gettCard());
            gridPaneNodes.getChildren().removeAll(cNode.getNodeNameLabel(), cNode.getBtnDelete());
        }
        uiNodeConnectors.clear();
        // delete all sequence diagrams
        for (UISDConnector cSD: uiSDConnectors) {
            gridPaneSD.getChildren().removeAll(cSD.getlName(), cSD.getbEditSD(), cSD.getbDeleteSD());
        }
        uiSDConnectors.clear();
        // update sequence diagrams counter
        SDIdentifNum = 1;
    }

    private void loadSDs(ArrayList<SequenceDiagram> sds) {
        for (SequenceDiagram sd: sds) {
            saveSD(sd);
        }
    }

    private void loadClasses(ClassDiagram cd) {
        for (int i = 0; i < cd.classesLen(); i++) {
            CDClass clsToAdd = cd.getCDClass(i);
            axisX = clsToAdd.getXposition();
            axisY = clsToAdd.getYposition();
            // fill up each class with his attributes into TableView
            TableView<FormField> tableView = new TableView<>();
            TableColumn<FormField, String> nameColumn = new TableColumn<>("Name");
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            TableColumn<FormField, String> typeColumn = new TableColumn<>("Type");
            typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            TableColumn<FormField, String> visibilityColumn = new TableColumn<>("Visibility");
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

    public void loadNodes(ClassDiagram cd) {
        for (int i = 0; i < cd.nodesLen(); i++) {
            CDNode nodeToAdd = cd.getCDNode(i);
            putNode(uiClassConnectors.get(nodeToAdd.getFromAsInt(cd)), uiClassConnectors.get(nodeToAdd.getToAsInt(cd)),
                    nodeToAdd.getfAnchor(), nodeToAdd.gettAnchor(),
                    nodeToAdd.getfCard(), nodeToAdd.gettCard(),
                    classDiagram.NodeType.valueOfLabel(nodeToAdd.getType()));
        }
    }

    public ClassDiagram saveCD() {
        ClassDiagram cd = new ClassDiagram();
        saveClasses(cd);
        saveNodes(cd);
        return cd;
    }

    private void saveClasses(ClassDiagram cd) {
        for (UIClassConnector connector: uiClassConnectors) {
            // extract fields and methods from connector
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
            // create and add new class to class diagram
            CDClass cdClass = new CDClass(connector.getClassNameLabel().getText(), 99,
                    fields, methods, connector.getInterface_(),
                    connector.getAxisX().intValue(), connector.getAxisY().intValue());
            cd.addClass(cdClass);
        }
    }

    private void saveNodes(ClassDiagram cd) {
        for (UINodeConnector connector: uiNodeConnectors) {
            // get nodes classes
            CDClass fClass = null, tClass = null;
            for (UIClassConnector cClass: uiClassConnectors) {
                if (connector.getFrom() == cClass) {
                    fClass = cd.getCDClass(uiClassConnectors.indexOf(cClass));
                    break;
                }
            }
            for (UIClassConnector cClass: uiClassConnectors) {
                if (connector.getTo() == cClass) {
                    tClass = cd.getCDClass(uiClassConnectors.indexOf(cClass));
                    break;
                }
            }
            // create and add new node to class diagram
            CDNode cdNode = new CDNode(fClass, connector.getfAnchor(), tClass, connector.gettAnchor(),
                    connector.getfCard().getText(), connector.gettCard().getText(), connector.getNodeType());
            cd.addNode(cdNode);
        }
    }

    private void saveSDs(ClassDiagram cd) {
        ArrayList<SequenceDiagram> sds = new ArrayList<>();
        for (UISDConnector c: uiSDConnectors) {
            sds.add(c.getSequenceDiagram());
        }
        cd.setSequenceDiagrams(sds);
    }

    public void editClass(UIClassConnector uiConnector) {
        // set class x and y
        axisX = uiConnector.getAxisX();
        axisY = uiConnector.getAxisY();
        // open form window and fill up it with class attributes
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
        VBox vbFields = new VBox(10.0);
        for (FormField ff: tableView.getItems()) {
            if (Objects.equals(ff.getType(), "Field")) {
                vbFields.getChildren().add(new Label(ff.getVisibilitySymbol() + ff.getName()));
            }
        }
        for (FormField ff: tableView.getItems()) {
            if (Objects.equals(ff.getType(), "Method")) {
                vbFields.getChildren().add(new Label(ff.getVisibilitySymbol() + ff.getName() + "()"));
            }
        }
        TitledPane titledPane = new TitledPane(String.format("%s%s", (interface_? "<<interface>>\n" : ""), className), vbFields);
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
        for (Node child: gridPaneClasses.getChildren()) {
            GridPane.setRowIndex(child, GridPane.getRowIndex(child) + 1);
        }
        // insert new class row
        Label nameLabel = new Label(className);
        gridPaneClasses.addRow(0, nameLabel, btnClassEdit, btnClassDelete);

        UIClassConnector newCon = new UIClassConnector(titledPane, vbFields, axisX, axisY, interface_,
                tableView, nameLabel, btnClassEdit, btnClassDelete);
        // if class is edited, delete old one and redraw his nodes
        if (connectorEditing != null) {
            ArrayList<UINodeConnector> nodesToChange = new ArrayList<>();
            ArrayList<UIClassConnector> classesNewFrom = new ArrayList<>(), classesNewTo = new ArrayList<>();
            // check if edited class has any connected nodes
            for (UINodeConnector cNode: uiNodeConnectors) {
                UIClassConnector newFrom = cNode.getFrom(), newTo = cNode.getTo();
                boolean change = false;
                if (cNode.getFrom() == connectorEditing) {
                    newFrom = newCon;
                    change = true;
                }
                if (cNode.getTo() == connectorEditing) {
                    newTo = newCon;
                    change = true;
                }
                if (change) {
                    nodesToChange.add(cNode);
                    classesNewFrom.add(newFrom);
                    classesNewTo.add(newTo);
                }
            }
            // rewrite edited class nodes (for new nodes positions)
            PauseTransition wait = new PauseTransition(Duration.seconds(0.1));
            wait.setOnFinished((e) -> {
                for (int i = 0; i < nodesToChange.size(); i++) {
                    UINodeConnector node = nodesToChange.get(i);
                    putNode(classesNewFrom.get(i), classesNewTo.get(i),
                            node.getfAnchor(), node.gettAnchor(),
                            node.getfCard().getText(), node.gettCard().getText(),
                            node.getNodeType());
                    root.getChildren().removeAll(node.getNode(), node.getArrowHead(), node.getfCard(), node.gettCard());
                    gridPaneNodes.getChildren().removeAll(node.getNodeNameLabel(), node.getBtnDelete());
                    uiNodeConnectors.remove(node);
                }
            });
            wait.play();

            // remove previous class state, if editing
            root.getChildren().remove(connectorEditing.getTpClass());
            gridPaneClasses.getChildren().removeAll(connectorEditing.getClassNameLabel(), connectorEditing.getBtnEdit(), connectorEditing.getBtnDelete());
            uiClassConnectors.remove(connectorEditing);
            connectorEditing = null;
        }
        // save class objects in list with classes
        uiClassConnectors.add(newCon);
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
        if (nodeType == NodeType.GENERALIZATION) {
            // if node type is generalization, cardinality is empty
            fCard = "";
            tCard = "";
            // check for inherited methods
            for (Node fromField: fromClass.getVbFields().getChildren()) {
                if (((Label) fromField).getText().endsWith("()")) {
                    boolean inherit = false;
                    for (Node toField: toClass.getVbFields().getChildren()) {
                        if (Objects.equals(((Label) toField).getText(), ((Label) fromField).getText())) {
                            inherit = true;
                            break;
                        }
                    }
                    if (inherit) {
                        ((Label) fromField).setTextFill(Color.RED);
                    } else {
                        ((Label) fromField).setTextFill(Color.BLACK);
                    }
                }
            }
        }
        // get titled panes bounds
        TitledPane tpFrom = fromClass.getTpClass();
        Bounds fromBounds = tpFrom.localToScene(tpFrom.getBoundsInLocal());
        TitledPane tpTo = toClass.getTpClass();
        Bounds toBounds = tpTo.localToScene(tpTo.getBoundsInLocal());
        // get line to-from coordinates
        double[] fCrds = getLineXY(fromBounds, anchorFrom);
        double[] tCrds = getLineXY(toBounds, anchorTo);
        // get arrow line and head
        Line node = new Line(fCrds[0], fCrds[1], tCrds[0], tCrds[1]);
        Shape arrowHead = getArrowHead(nodeType, fCrds, tCrds);
        // create and put cardinality labels
        Label fCardLabel = new Label(fCard);
        putCard(fCardLabel, anchorFrom, fCrds);
        Label tCardLabel = new Label(tCard);
        putCard(tCardLabel, anchorTo, tCrds);
        // add node on the pane
        root.getChildren().addAll(node, arrowHead, fCardLabel, tCardLabel);

        Button btnNodeDelete = new Button("Delete");
        btnNodeDelete.setOnAction(deleteNode);
        // create place for new row
        for (Node child: gridPaneNodes.getChildren()) {
            GridPane.setRowIndex(child, GridPane.getRowIndex(child) + 1);
        }
        // insert new node row
        Label nameLabel = new Label(fromClass.getClassNameLabel().getText() + "->" + toClass.getClassNameLabel().getText());
        gridPaneNodes.addRow(0, nameLabel, btnNodeDelete);

        uiNodeConnectors.add(new UINodeConnector(fromClass, toClass, node, arrowHead, fCardLabel, tCardLabel,
                nameLabel, btnNodeDelete, anchorFrom, anchorTo, nodeType));
    }

    private Shape getArrowHead(NodeType nodeType, double[] fCrds, double[] tCrds) {
        double L1 = 15;     // arrow head wings length
        double L2 = Math.sqrt((tCrds[1] - fCrds[1]) * (tCrds[1] - fCrds[1]) + (tCrds[0] - fCrds[0]) * (tCrds[0] - fCrds[0]));
        // calculate arrow head coordinates
        double arrowX1 = tCrds[0] + L1 * ((fCrds[0] - tCrds[0]) * (Math.sqrt(3)/2) + (fCrds[1] - tCrds[1]) * (Math.sqrt(1)/2)) / L2;
        double arrowY1 = tCrds[1] + L1 * ((fCrds[1] - tCrds[1]) * (Math.sqrt(3)/2) - (fCrds[0] - tCrds[0]) * (Math.sqrt(1)/2)) / L2;
        double arrowX2 = tCrds[0] + L1 * ((fCrds[0] - tCrds[0]) * (Math.sqrt(3)/2) - (fCrds[1] - tCrds[1]) * (Math.sqrt(1)/2)) / L2;
        double arrowY2 = tCrds[1] + L1 * ((fCrds[1] - tCrds[1]) * (Math.sqrt(3)/2) + (fCrds[0] - tCrds[0]) * (Math.sqrt(1)/2)) / L2;
        // return arrow head base on the node type
        switch (nodeType.getNumVal()) {
            case 0:     // aggregation
                // count point on the line
                double aggX = arrowX1 - tCrds[0] + arrowX2;
                double aggY = arrowY1 - tCrds[1] + arrowY2;
                Polygon aggPolygon = new Polygon(arrowX1, arrowY1, tCrds[0], tCrds[1], arrowX2, arrowY2, aggX, aggY);
                aggPolygon.setFill(Color.WHITESMOKE);
                aggPolygon.setStroke(Color.BLACK);
                return aggPolygon;
            case 1:     // association
                return new Polyline(arrowX1, arrowY1, tCrds[0], tCrds[1], arrowX2, arrowY2);
            case 2:     // generalization
                Polygon polygon = new Polygon(arrowX1, arrowY1, tCrds[0], tCrds[1], arrowX2, arrowY2);
                polygon.setFill(Color.WHITESMOKE);
                polygon.setStroke(Color.BLACK);
                return polygon;
            default:    // composition
                // count point on the line
                double compX = arrowX1 - tCrds[0] + arrowX2;
                double compY = arrowY1 - tCrds[1] + arrowY2;
                Polygon compPolygon = new Polygon(arrowX1, arrowY1, tCrds[0], tCrds[1], arrowX2, arrowY2, compX, compY);
                compPolygon.setFill(Color.BLACK);
                return compPolygon;
        }
    }

    private double[] getLineXY(Bounds bounds, AnchorType anchor) {
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

    private void putCard(Label cardLabel, AnchorType anchor, double[] crds) {
        switch (anchor.getSymb()) {
            case "LEFT":
                AnchorPane.setLeftAnchor(cardLabel, crds[0] - 30);
                AnchorPane.setTopAnchor(cardLabel, crds[1] - 20);
                return;
            case "RIGHT":
                AnchorPane.setLeftAnchor(cardLabel, crds[0] + 10);
                AnchorPane.setTopAnchor(cardLabel, crds[1] - 20);
                return;
            case "DOWN":
                AnchorPane.setLeftAnchor(cardLabel, crds[0] + 5);
                AnchorPane.setTopAnchor(cardLabel, crds[1] + 5);
                return;
            default:    // "UP"
                AnchorPane.setLeftAnchor(cardLabel, crds[0] + 10);
                AnchorPane.setTopAnchor(cardLabel, crds[1] - 20);
        }
    }
}
