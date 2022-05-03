package userInterface.SDInterface;

import classDiagram.ClassDiagram;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import sequenceDiagram.*;
import userInterface.App;
import userInterface.CDInterface.CDController;
import userInterface.CDInterface.UISDConnector;

import java.io.IOException;
import java.util.ArrayList;

public class SDController {

    @FXML
    private AnchorPane root;

    @FXML
    private ScrollPane spActivations, spMessages, spObjects;

    private GridPane gpActivations, gpMessages, gpObjects;

    private UISDConnector uisdConnector;
    public SequenceDiagram sequenceDiagram;

    public double timeLineStartY = 93.0;
    public double timeLineEndY = 683.0;
    public double timeLineOneUnit = (timeLineEndY - timeLineStartY) / 100;

    public ArrayList<UIObjectConnector> uiObjectConnectors;
    public ArrayList<UIActivationConnector> uiActivationConnectors;
    public ArrayList<UIMessageConnector> uiMessageConnectors;

    private ArrayList<SequenceDiagram> undoMemory;

    /**
     * Sequence diagram window initialization
     */
    @FXML
    public void initialize() {
        // initialize grid panes on pane
        gpActivations = new GridPane();
        gpInit(spActivations, gpActivations);
        gpMessages = new GridPane();
        gpInit(spMessages, gpMessages);
        gpObjects = new GridPane();
        gpInit(spObjects, gpObjects);
        // declare empty connector lists
        uiObjectConnectors = new ArrayList<>();
        uiActivationConnectors = new ArrayList<>();
        uiMessageConnectors = new ArrayList<>();
        undoMemory = new ArrayList<>();
        // define new sequence diagram
        sequenceDiagram = new SequenceDiagram();
    }

    private void gpInit(ScrollPane scrollPane, GridPane gridPane) {
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        scrollPane.setContent(gridPane);
    }

    public void setConnector(UISDConnector uisdConnector) {
        this.uisdConnector = uisdConnector;
        loadSD(uisdConnector.getSequenceDiagram());
    }

    public void loadSD(SequenceDiagram sd) {
        this.sequenceDiagram = sd;
        this.uisdConnector.setSequenceDiagram(sd);
        ClassDiagram currentCD = CDController.getController().saveCD();
        // reset sequence diagram
        clearPane();
        clearGPs();
        int inc = 0;    // X position error
        ArrayList<Line> timeLines= new ArrayList<>();
        // load objects
        for (SDObject o: sd.getObjects()) {
            Text objectName = new Text(o.getObjName() + ":\n" + o.getClassName());
            // color object name red in case of inconsistency with class diagram
            if (!SDObject.checkClassName(currentCD, o.getClassName())) {
                objectName.setFill(Color.RED);
            }

            objectName.setTextAlignment(TextAlignment.CENTER);
            Rectangle recObj = new Rectangle(110, 40, Color.WHITESMOKE);
            recObj.setStroke(Color.BLACK);
            StackPane stackPaneObj = new StackPane(recObj, objectName);

            double classYPos = o.getTimePos() == -1 ? 40 : timeLineStartY + (o.getTimePos() * timeLineOneUnit) - 40;
            // set object position on pane
            AnchorPane.setTopAnchor(stackPaneObj, classYPos);
            AnchorPane.setLeftAnchor(stackPaneObj, inc + 60.0);

            Line timeLine = new Line(inc + 115, classYPos + 40, inc + 115, 683);
            timeLine.getStrokeDashArray().addAll(25d, 10d);
            timeLines.add(timeLine);

            root.getChildren().addAll(timeLine, stackPaneObj);
            // create object entry and add him on the grid pane
            Label lObject = new Label(o.getObjName() + ":" + o.getClassName());
            Button bEditObject = new Button("Edit");
            bEditObject.setOnAction(editObject);
            Button bDeleteObject = new Button("Delete");
            bDeleteObject.setOnAction(deleteObject);

            for (Node child : gpObjects.getChildren()) {
                GridPane.setRowIndex(child, GridPane.getRowIndex(child) + 1);
            }
            gpObjects.addRow(0, lObject, bEditObject, bDeleteObject);
            uiObjectConnectors.add(new UIObjectConnector(o, lObject, bEditObject, bDeleteObject));

            // load object activations
            for (SDActivation a: o.getActivations()) {
                int timeBegin = a.getTimeBegin();
                int timeEnd = a.getTimeEnd();
                Rectangle recActivation = new Rectangle(10, (timeEnd - timeBegin) / 100.0 * (timeLineEndY - timeLineStartY), Color.WHITESMOKE);
                recActivation.setStroke(Color.BLACK);
                AnchorPane.setTopAnchor(recActivation, timeLineStartY + (timeBegin * timeLineOneUnit));
                AnchorPane.setLeftAnchor(recActivation, inc + 110.0);
                root.getChildren().add(recActivation);

                // create activation entry and add it on the grid pane
                Label lActivation = new Label(o.getObjName() + "(" + timeBegin + ":" + timeEnd + ")");
                Button bDeleteActivation = new Button("Delete");
                bDeleteActivation.setOnAction(deleteActivation);

                for (Node child : gpActivations.getChildren()) {
                    GridPane.setRowIndex(child, GridPane.getRowIndex(child) + 1);
                }
                gpActivations.addRow(0, lActivation, bDeleteActivation);
                uiActivationConnectors.add(new UIActivationConnector(a, o, lActivation, bDeleteActivation));
            }
            // add next object X error
            inc += 135;
        }
        // load messages
        for (SDMessage m: sd.getMessages()) {
            int classIndexFrom = m.getFrom(sd);
            int classIndexTo = m.getTo(sd);
            int error = classIndexFrom < classIndexTo ? 5 : -5;

            double fromX = 115 + error + classIndexFrom * 135;
            // error correction on toX for object creation
            error += m.getType() == MessageType.CREATE_OBJ ? (classIndexFrom < classIndexTo ? 50 : -50) : 0;
            double toX = 115 - error + classIndexTo * 135;
            double Y = timeLineStartY + (m.getTimePos() * timeLineOneUnit);
            // message name label positioning
            Label lNameMessage = new Label(m.getName());
            // color message red in case of inconsistency
            if (!SDMessage.checkConsistency(currentCD, uiObjectConnectors.get(classIndexTo).getObject(), m.getName())) {
                lNameMessage.setTextFill(Color.RED);
            }
            lNameMessage.setAlignment(Pos.CENTER);
            AnchorPane.setLeftAnchor(lNameMessage, fromX - (classIndexFrom < classIndexTo ? 0 : 72));
            AnchorPane.setTopAnchor(lNameMessage, Y - 17);
            // message arrow setup
            Line lineMessage = new Line(fromX, Y, toX, Y);
            if (m.getType() == MessageType.CREATE_OBJ || m.getType() == MessageType.RETURN) {
                lineMessage.getStrokeDashArray().addAll(10d, 10d);
            }
            Shape arrowHead = getArrowHead(fromX, toX, Y);

            root.getChildren().addAll(lineMessage, arrowHead, lNameMessage);
            // if message is destroy, create cross object on the object lifeline
            if (m.getType() == MessageType.DESTROY_OBJ) {
                Line cross1 = new Line(105 + classIndexTo * 135, Y + 30, 125 + classIndexTo * 135, Y + 50);
                Line cross2 = new Line(125 + classIndexTo * 135, Y + 30, 105 + classIndexTo * 135, Y + 50);
                // correct existing timeline if destroy message is higher than other destroy messages
                boolean destroyNotExist = true;
                for (SDMessage mDestroy: sd.getMessages()) {
                    if (mDestroy.getType() == MessageType.DESTROY_OBJ && mDestroy.getTo(sequenceDiagram) == classIndexTo) {
                        if (mDestroy.getTimePos() < m.getTimePos()) {
                            destroyNotExist = false;
                            break;
                        }
                    }
                }
                if (destroyNotExist) {
                    timeLines.get(classIndexTo).setEndY(Y + 40);
                }

                root.getChildren().addAll(cross1, cross2);
            }

            // create activation entry and add it on the grid pane
            Label lMessage = new Label(m.getName());
            Button bDeleteMessage = new Button("Delete");
            bDeleteMessage.setOnAction(deleteMessage);
            for (Node child : gpMessages.getChildren()) {
                GridPane.setRowIndex(child, GridPane.getRowIndex(child) + 1);
            }
            gpMessages.addRow(0, lMessage, bDeleteMessage);
            uiMessageConnectors.add(new UIMessageConnector(m, lMessage, bDeleteMessage));
        }
    }

    @FXML
    void clearAction() {
        undoSave();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remove everything?");
        alert.setHeaderText(null);
        alert.setContentText("All objects, activations and messages will be removed. Proceed?");

        alert.showAndWait();
        if (alert.getResult() != ButtonType.OK) {
            return;
        }
        clearPane();
        clearGPs();
        sequenceDiagram.getObjects().clear();
        sequenceDiagram.getMessages().clear();
    }

    private void clearPane() {
        ArrayList<Node> nodesToDelete = new ArrayList<>();
        for (Node child: root.getChildren()) {
            if (child.getId() == null) {
                nodesToDelete.add(child);
            }
        }
        root.getChildren().removeAll(nodesToDelete);
    }

    private void clearGPs() {
        for (UIObjectConnector cObj: uiObjectConnectors) {
            gpObjects.getChildren().removeAll(cObj.getlObject(), cObj.getbEditObject(), cObj.getbDeleteObject());
        }
        uiObjectConnectors.clear();
        for (UIActivationConnector cAct: uiActivationConnectors) {
            gpActivations.getChildren().removeAll(cAct.getlActivation(), cAct.getbDeleteActivation());
        }
        uiActivationConnectors.clear();
        for (UIMessageConnector cMsg: uiMessageConnectors) {
            gpMessages.getChildren().removeAll(cMsg.getlMessage(), cMsg.getbDeleteMessage());
        }
        uiMessageConnectors.clear();
    }

    private Shape getArrowHead(double fromX, double toX, double Y) {
        double L1 = 15;     // arrow head wings length
        double L2 = Math.sqrt((toX - fromX) * (toX - fromX));
        // calculate arrow head coordinates
        double arrowX = toX + L1 * ((fromX - toX) * (Math.sqrt(3)/2)) / L2;
        double arrowY1 = Y + L1 * (-(fromX - toX) * (Math.sqrt(1)/2)) / L2;
        double arrowY2 = Y + L1 * ((fromX - toX) * (Math.sqrt(1)/2)) / L2;

        return new Polyline(arrowX, arrowY1, toX, Y, arrowX, arrowY2);
    }

    @FXML
    void addObject() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("SequenceDiagramFXML/objectForm.fxml"));
            Stage stage = new Stage();
            Parent root = fxmlLoader.load();
            stage.setTitle("Object form");
            stage.setResizable(false);

            ((ObjectFormController) fxmlLoader.getController()).setSDController(this);

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void putObject(String objectName, String className, Integer timePos) {
        // throw warning if there are no place for the new object in diagram
        if (sequenceDiagram.getObjects().size() >= 6) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Diagram is full");
            alert.setHeaderText(null);
            alert.setContentText("There are no space for the new object");

            alert.showAndWait();
            return;
        }
        undoSave();
        // add new object and reload the diagram
        sequenceDiagram.getObjects().add(new SDObject(objectName, className, timePos));
        loadSD(sequenceDiagram);
    }

    @FXML
    void addActivation() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("SequenceDiagramFXML/activationForm.fxml"));
            Stage stage = new Stage();
            Parent root = fxmlLoader.load();
            stage.setTitle("Activation form");
            stage.setResizable(false);

            ActivationFormController controller = fxmlLoader.getController();
            controller.setSDController(this);
            controller.setObjects(uiObjectConnectors);

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void putActivation(SDObject object, SDActivation activation) {
        undoSave();
        // add new activation and reload the diagram
        object.getActivations().add(activation);
        loadSD(sequenceDiagram);
    }

    @FXML
    void addMessage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("SequenceDiagramFXML/messageForm.fxml"));
            Stage stage = new Stage();
            Parent root = fxmlLoader.load();
            stage.setTitle("Message form");
            stage.setResizable(false);

            MessageFormController controller = fxmlLoader.getController();
            controller.setSDController(this);
            controller.setObjects(uiObjectConnectors);

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void putMessage(String msgName, int fromIdx, int toIdx, MessageType msgType, int timePos) {
        undoSave();
        // add new message and reload the diagram
        sequenceDiagram.getMessages().add(new SDMessage(msgName, fromIdx, toIdx, sequenceDiagram, msgType, timePos));
        loadSD(sequenceDiagram);
    }

    public void undoSave () {
        // clone existing diagram
        SequenceDiagram sdToSave = new SequenceDiagram();
        ArrayList<SDObject> sdObjects = new ArrayList<>();
        ArrayList<SDMessage> sdMessages = new ArrayList<>();
        for (SDObject o: sequenceDiagram.getObjects()) {
            SDObject objToSave = new SDObject(o.getObjName(), o.getClassName(), o.getTimePos());
            ArrayList<SDActivation> sdActivations = new ArrayList<>();
            for (SDActivation a: o.getActivations()) {
                sdActivations.add(new SDActivation(a.getTimeBegin(), a.getTimeEnd()));
            }
            objToSave.setActivations(sdActivations);
            sdObjects.add(objToSave);
        }
        sdToSave.setObjects(sdObjects);
        for (SDMessage m: sequenceDiagram.getMessages()) {
            sdMessages.add(new SDMessage(m.getName(), m.getFrom(sequenceDiagram), m.getTo(sequenceDiagram), sdToSave,
                    m.getType(), m.getTimePos()));
        }
        sdToSave.setMessages(sdMessages);
        undoMemory.add(sdToSave);
    }

    @FXML
    void undoAction() {
        if (undoMemory.size() != 0) {
            loadSD(undoMemory.remove(undoMemory.size() - 1));
        }
    }

    public EventHandler<ActionEvent> editObject = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            for (UIObjectConnector c: uiObjectConnectors) {
                if (event.getSource() == c.getbEditObject()) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("SequenceDiagramFXML/objectForm.fxml"));
                        Stage stage = new Stage();
                        Parent root = fxmlLoader.load();
                        stage.setTitle("Object form");
                        stage.setResizable(false);

                        ObjectFormController formController = fxmlLoader.getController();
                        formController.setSDController(SDController.this);
                        formController.setEdit(c);

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

    public EventHandler<ActionEvent> deleteObject = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            for (UIObjectConnector cObj: uiObjectConnectors) {
                if (event.getSource() == cObj.getbDeleteObject()) {
                    undoSave();
                    // reload sequence diagram
                    sequenceDiagram.getObjects().remove(cObj.getObject());

                    // delete any bounded with object messages
                    ArrayList<SDMessage> messagesToDelete = new ArrayList<>();
                    for (SDMessage m: sequenceDiagram.getMessages()) {
                        if (m.getFrom(sequenceDiagram) == -1 || m.getTo(sequenceDiagram) == -1) {
                            messagesToDelete.add(m);
                        }
                    }
                    sequenceDiagram.getMessages().removeAll(messagesToDelete);

                    loadSD(sequenceDiagram);
                    break;
                }
            }
        }
    };

    public EventHandler<ActionEvent> deleteActivation = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            for (UIActivationConnector cAct: uiActivationConnectors) {
                if (event.getSource() == cAct.getbDeleteActivation()) {
                    for (SDObject o: sequenceDiagram.getObjects()) {
                        if (o == cAct.getsdObject()) {
                            undoSave();
                            // reload sequence diagram
                            o.getActivations().remove(cAct.getsdActivation());
                            loadSD(sequenceDiagram);
                            break;
                        }
                    }
                    break;
                }
            }
        }
    };

    public EventHandler<ActionEvent> deleteMessage = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            for (UIMessageConnector cMsg: uiMessageConnectors) {
                if (event.getSource() == cMsg.getbDeleteMessage()) {
                    undoSave();
                    // reload sequence diagram
                    sequenceDiagram.getMessages().remove(cMsg.getsdMessage());
                    loadSD(sequenceDiagram);
                    break;
                }
            }
        }
    };
}
