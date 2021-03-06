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
 * Main window controller
 * Build with Singleton pattern
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
    public MenuItem menuItemClear = new MenuItem("Clear");
    public MenuItem menuItemUndo = new MenuItem("Undo");
    public MenuItem menuItemHelp = new MenuItem("Help");
    public MenuItem menuItemCredits = new MenuItem("Credits");
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

    public ArrayList<ClassDiagram> undoMemory;
    // sequence diagrams counter
    private int SDIdentifNum = 1;
    // CDController singleton instance
    private static CDController CD_controller;

    /**
     * CDController constructor
     * @param root main pane
     */
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
        this.menuItemClear.setOnAction(this);
        this.menuItemUndo.setOnAction(this);

        this.buttonCreateNode.setOnAction(this);
        this.buttonCreateSD.setOnAction(this);
        // initialize UI connectors
        uiClassConnectors = new ArrayList<>();
        uiNodeConnectors = new ArrayList<>();
        uiSDConnectors = new ArrayList<>();

        undoMemory = new ArrayList<>();
    }

    /**
     * CDController singleton setter
     * @param root main pane
     * @return CDController singleton instance
     */
    public static CDController setController(AnchorPane root) {
        if (CD_controller == null) {
            CD_controller = new CDController(root);
        }
        return CD_controller;
    }

    /**
     * Controller singleton instance getter
     * @return CDController singleton instance
     */
    public static CDController getController() {
        return CD_controller;
    }

    /**
     * Window GridPanes setter
     * @param gridPaneClasses GridPane with classes
     * @param gridPaneNodes GridPane with nodes
     * @param gridPaneSD GridPane with Sequence Diagrams
     */
    public void setGridPanes(GridPane gridPaneClasses, GridPane gridPaneNodes, GridPane gridPaneSD) {
        this.gridPaneClasses = gridPaneClasses;
        this.gridPaneNodes = gridPaneNodes;
        this.gridPaneSD = gridPaneSD;
    }

    /**
     * Handler of the main window static buttons
     * @param actionEvent button click event
     */
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
            openFXML("ClassDiagramFXML/helpWindow.fxml", "Help");       // help menu
        } else if (actionEvent.getSource() == this.menuItemCredits) {
            openFXML("ClassDiagramFXML/creditsWindow.fxml", "Credits");     // credits menu
        } else if (actionEvent.getSource() == this.buttonCreateNode) {
            openFXML("ClassDiagramFXML/nodeForm.fxml", "Node form");        // add new node
        } else if (actionEvent.getSource() == this.buttonCreateSD) {
            undoSave();
            // add new sequence diagram and window with it
            SequenceDiagram sd = new SequenceDiagram();
            saveSD(sd);
            for (UISDConnector c : uiSDConnectors) {
                if (c.getSequenceDiagram() == sd) {
                    c.getbEditSD().fire();
                    break;
                }
            }
        } else if (actionEvent.getSource() == this.menuItemClear) {
            if (!showConformation("Remove everything?", "All classes, nodes and sequence diagrams will be removed. Proceed?")) {
                return;
            }
            undoSave();
            clearScreen();      // clear pane from all user objects
        } else if (actionEvent.getSource() == this.menuItemUndo) {
            undo();     // undo last action
        }
    }

    /**
     * Open specifed FXML window
     * @param fxml FXML file name
     * @param title new window title
     */
    private void openFXML(String fxml, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml));
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setResizable(false);
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Open conformation window
     * @param title conformation window title
     * @param content conformation window content
     * @return TRUE in case of conformation, FALSE otherwise
     */
    public boolean showConformation(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();
        return alert.getResult() == ButtonType.OK;
    }

    /**
     * Delete class event handler
     */
    public EventHandler<ActionEvent> deleteClass = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            for (UIClassConnector cClass: uiClassConnectors) {
                if (event.getSource() == cClass.getBtnDelete()) {
                    undoSave();

                    ClassDiagram currentCD = CDController.getController().saveCD();
                    saveSDs(currentCD);
                    // check if deleting class would cause inconsistency in one of the class diagrams
                    if (currentCD.checkDeleteClass(currentCD.getCDClass(uiClassConnectors.indexOf(cClass)))) {
                        if (!showConformation("Inconsistency", "Action will cause an inconsistency in one of the sequence diagrams. Still proceed?")) {
                            return;
                        }
                    }

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

    /**
     * Edit class event handler
     */
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

    /**
     * Delete node event handler
     */
    public EventHandler<ActionEvent> deleteNode = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            for (UINodeConnector c: uiNodeConnectors) {
                if (event.getSource() == c.getBtnDelete()) {
                    undoSave();

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

    /**
     * Canvas mouse click event handler
     */
    public EventHandler<MouseEvent> canvasMousePress = event -> {
        if (event.getTarget() == event.getSource())
        {
            if (buttonCreateClass.isSelected()) {
                if (event.getX() > 930 || (event.getX() < 260 && event.getY() > 610)) {
                    System.out.println("Warning: pane clicked, restricted section");
                } else {
                    axisX = event.getX();
                    axisY = event.getY();
                    // open new class form window
                    openFXML("ClassDiagramFXML/classForm.fxml", "Class form");
                    buttonCreateClass.setSelected(false);
                }
            }
        }
    };

    /**
     * Delete Sequence Diagram event handler
     */
    public EventHandler<ActionEvent> deleteSD = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            for (UISDConnector c: uiSDConnectors) {
                if (event.getSource() == c.getbDeleteSD()) {
                    undoSave();

                    gridPaneSD.getChildren().removeAll(c.getlName(), c.getbEditSD(), c.getbDeleteSD());
                    uiSDConnectors.remove(c);
                    break;
                }
            }
        }
    };

    /**
     * Edit Sequence Diagram event handler
     */
    public EventHandler<ActionEvent> editSD = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            for (UISDConnector c: uiSDConnectors) {
                if (event.getSource() == c.getbEditSD()) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("SequenceDiagramFXML/SDWindow.fxml"));
                        Stage stage = new Stage();
                        stage.setTitle(c.getlName().getText());
                        stage.setResizable(false);
                        Parent root = fxmlLoader.load();
                        // initialize diagram
                        ((SDController) fxmlLoader.getController()).setConnector(c);

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

    /**
     * Save new Sequence diagram
     * @param sd Sequence Diagram to save
     */
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

    /**
     * Save diagrams for possible UNDO action
     */
    public void undoSave() {
        // clone existing diagram
        ClassDiagram cdToSave = saveCD();
        saveSDs(cdToSave);

        undoMemory.add(cdToSave);
    }

    /**
     * Undo last action
     */
    public void undo() {
        if (undoMemory.size() != 0) {
            // clear screen and memory from all classes
            clearScreen();

            ClassDiagram cdToLoad = undoMemory.remove(undoMemory.size() - 1);
            // load class diagram
            loadClasses(cdToLoad);
            // wait before all classes are saved and load nodes
            PauseTransition wait = new PauseTransition(Duration.seconds(0.1));
            wait.setOnFinished((e) -> loadNodes(cdToLoad));
            wait.play();
            // load sequence diagrams
            loadSDs(cdToLoad.getSequenceDiagrams());
        }
    }

    /**
     * Clear screen action
     */
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

    /**
     * Load all Sequence Diagrams into pane
     * @param sds array with Sequence Diagrams
     */
    private void loadSDs(ArrayList<SequenceDiagram> sds) {
        for (SequenceDiagram sd: sds) {
            saveSD(sd);
        }
    }

    /**
     * Load all classes into pane
     * @param cd Class Diagram with classes
     */
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

    /**
     * Load all nodes into pane
     * @param cd Class Diagram with nodes
     */
    public void loadNodes(ClassDiagram cd) {
        for (int i = 0; i < cd.nodesLen(); i++) {
            CDNode nodeToAdd = cd.getCDNode(i);
            putNode(uiClassConnectors.get(nodeToAdd.getFromAsInt(cd)), uiClassConnectors.get(nodeToAdd.getToAsInt(cd)),
                    nodeToAdd.getfAnchor(), nodeToAdd.gettAnchor(),
                    nodeToAdd.getfCard(), nodeToAdd.gettCard(),
                    classDiagram.NodeType.valueOfLabel(nodeToAdd.getType()));
        }
    }

    /**
     * Fetch all pane elements of the Class Diagram and save it
     * @return Class Diagram with all pane elements
     */
    public ClassDiagram saveCD() {
        ClassDiagram cd = new ClassDiagram();
        saveClasses(cd);
        saveNodes(cd);
        return cd;
    }

    /**
     * Save all pane classes into given Class Diagram
     * @param cd Class Diagram to save classes into
     */
    private void saveClasses(ClassDiagram cd) {
        for (UIClassConnector cClass: uiClassConnectors) {
            // extract fields and methods from connector
            ArrayList<CDField> fields = new ArrayList<>();
            ArrayList<CDField> methods = new ArrayList<>();
            for (FormField ff: cClass.getTableView().getItems()) {
                if (Objects.equals(ff.getType(), "Field")) {
                    fields.add(new CDField(ff.getName(), Visibility.valueOfLabel(ff.getVisibilitySymbol())));
                }
                if (Objects.equals(ff.getType(), "Method")) {
                    methods.add(new CDField(ff.getName(), Visibility.valueOfLabel(ff.getVisibilitySymbol())));
                }
            }
            // assign parent class
            int parent = -1;
            for (UINodeConnector cNode: uiNodeConnectors) {
                if (cNode.getNodeType() == NodeType.GENERALIZATION && cNode.getFrom() == cClass) {
                    parent = uiClassConnectors.indexOf(cNode.getTo());
                }
            }
            // create and add new class to class diagram
            CDClass cdClass = new CDClass(cClass.getClassNameLabel().getText(), parent,
                    fields, methods, cClass.getInterface_(),
                    cClass.getAxisX().intValue(), cClass.getAxisY().intValue());
            cd.addClass(cdClass);
        }
    }

    /**
     * Save all pane nodes into given Class Diagram
     * @param cd Class Diagram to save nodes into
     */
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

    /**
     * Save all pane Sequence Diagrams into given Class Diagram
     * @param cd Class Diagram to save Sequence Diagrams into
     */
    public void saveSDs(ClassDiagram cd) {
        ArrayList<SequenceDiagram> sds = new ArrayList<>();
        for (UISDConnector c: uiSDConnectors) {
            sds.add(c.getSequenceDiagram());
        }
        cd.setSequenceDiagrams(sds);
    }

    /**
     * Edit existing class action
     * @param cClass connector with class to edit
     */
    public void editClass(UIClassConnector cClass) {
        // set class x and y
        axisX = cClass.getAxisX();
        axisY = cClass.getAxisY();
        // open form window and fill up it with class attributes
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("ClassDiagramFXML/classForm.fxml"));
            Stage stage = new Stage();
            Parent root = fxmlLoader.load();

            ((ClassFormController) fxmlLoader.getController()).setEdit(cClass);

            Scene scene = new Scene(root);

            TextField textField = (TextField) scene.lookup("#tfClassName");
            textField.setText(cClass.getClassNameLabel().getText());   // set class name in form

            TableView<FormField> tableView = (TableView<FormField>) scene.lookup("#tvFields");
            ToggleButton tbInterface =  (ToggleButton) scene.lookup("#tbInterface");
            tbInterface.setSelected(cClass.getInterface_());

            tableView.getItems().addAll(cClass.getTableView().getItems());
            stage.setTitle("Class form");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Put class into pane
     * @param className name of the new class
     * @param interface_ is class an interface
     * @param tableView TableView with class fields and methods
     */
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
        TitledPane titledPane = new TitledPane(String.format("%s%s", (interface_ ? "<<interface>>\n" : ""), className), vbFields);
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

    /**
     * Put node into pane
     * @param fromClass connector with node FROM class
     * @param toClass connector with node TO class
     * @param anchorFrom FROM class anchor type
     * @param anchorTo TO class anchor type
     * @param fCard FROM class cardinality
     * @param tCard TO class cardinality
     * @param nodeType type of the node
     */
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

    /**
     * Calculate coordinates and shape of the node arrow head
     * @param nodeType type of the node
     * @param fCrds FROM point coordinates
     * @param tCrds TO point coordinates
     * @return positioned arrow head shape
     */
    private Shape getArrowHead(NodeType nodeType, double[] fCrds, double[] tCrds) {
        double L1 = 15;     // arrow head wings length
        double L2 = Math.sqrt((tCrds[1] - fCrds[1]) * (tCrds[1] - fCrds[1]) + (tCrds[0] - fCrds[0]) * (tCrds[0] - fCrds[0]));
        // calculate arrow head coordinates (big brain math)
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

    /**
     * Get node X and Y coordinates based on the bounds and anchor type
     * @param bounds TitledPane bounds
     * @param anchor node anchor type
     * @return X and Y coordinates as doubles array
     */
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

    /**
     * Set cardinality labels based on the anchor type and coordinates
     * @param cardLabel label to set
     * @param anchor anchor type
     * @param crds coordinates of the node
     */
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
