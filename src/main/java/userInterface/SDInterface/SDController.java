package userInterface.SDInterface;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import sequenceDiagram.*;
import userInterface.App;

import java.io.IOException;
import java.util.ArrayList;

public class SDController {

    @FXML
    private AnchorPane root;

    @FXML
    private ScrollPane spActivations, spMessages, spObjects;

    private GridPane gpActivations, gpMessages, gpObjects;

    private SequenceDiagram sequenceDiagram;

    public double timeLineStartY = 93.0;
    public double timeLineEndY = 683.0;
    public double timeLineOneUnit = (timeLineEndY - timeLineStartY) / 100;

    public ArrayList<UIObjectConnector> uiObjectConnectors;
    public ArrayList<UIActivationConnector> uiActivationConnectors;
    public ArrayList<UIMessageConnector> uiMessageConnectors;

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
        // define new sequence diagram
        sequenceDiagram = new SequenceDiagram();
    }

    private void gpInit(ScrollPane scrollPane, GridPane gridPane) {
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        scrollPane.setContent(gridPane);
    }

    public void loadSD(SequenceDiagram sd) {
        this.sequenceDiagram = sd;
        // reset sequence diagram
        clearPane();
        clearGPs();
        int inc = 0;    // X position error
        // load objects
        for (SDObject o: sd.getObjects()) {
            Text objectName = new Text(o.getObjName() + ":\n" + o.getClassName());
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

            root.getChildren().addAll(timeLine, stackPaneObj);
            // create object entry and add him on the grid pane
            Label lObject = new Label(o.getObjName() + ":" + o.getClassName());
            Button bEditObject = new Button("Edit");
            Button bDeleteObject = new Button("Delete");
            bDeleteObject.setOnAction(deleteObject);

            for (Node child : gpObjects.getChildren()) {
                GridPane.setRowIndex(child, GridPane.getRowIndex(child) + 1);
            }
            gpObjects.addRow(0, lObject, bEditObject, bDeleteObject);
            uiObjectConnectors.add(new UIObjectConnector(o, lObject, bEditObject, bDeleteObject));

            // load object activations
            for (SDActivation a: o.getActivations()) {
                double timeBegin = a.getTimeBegin();
                double timeEnd = a.getTimeEnd();
                Rectangle recActivation = new Rectangle(10, (timeEnd - timeBegin) / 100 * (timeLineEndY - timeLineStartY), Color.WHITESMOKE);
                recActivation.setStroke(Color.BLACK);
                AnchorPane.setTopAnchor(recActivation, timeLineStartY + (timeBegin * timeLineOneUnit));
                AnchorPane.setLeftAnchor(recActivation, inc + 110.0);
                root.getChildren().add(recActivation);

                // create activation entry and add it on the grid pane
                Label lActivation = new Label("Todo");
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
            error += m.getType() == MessageType.CREATE_OBJ ? 50 : 0;
            double toX = 115 - error + classIndexTo * 135;
            double Y = timeLineStartY + (m.getTimePos() * timeLineOneUnit);

            Line lineMessage = new Line(fromX, Y, toX, Y);
            if (m.getType() == MessageType.CREATE_OBJ || m.getType() == MessageType.RETURN) {
                lineMessage.getStrokeDashArray().addAll(10d, 10d);
            }
            Shape arrowHead = getArrowHead(fromX, toX, Y);

            root.getChildren().addAll(lineMessage, arrowHead);

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
    void addObject(ActionEvent event) {
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
            alert.setContentText("There are no space for the new object.\n");

            alert.showAndWait();
            return;
        }
        // add new object and reload diagram
        sequenceDiagram.getObjects().add(new SDObject(objectName, className, timePos));
        loadSD(sequenceDiagram);
    }

    @FXML
    void addActivation(ActionEvent event) {
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

    public void putActivation() {
        // TODO
    }

    @FXML
    void addMessage(ActionEvent event) {
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

    public void putMessage() {
        // TODO
    }

    @FXML
    void undoAction(ActionEvent event) {
        // TODO
    }

    public EventHandler<ActionEvent> editObject = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            for (UIObjectConnector c: uiObjectConnectors) {
                if (event.getSource() == c.getbEditObject()) {
                    // TODO
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
                    // reload sequence diagram
                    sequenceDiagram.getMessages().remove(cMsg.getsdMessage());
                    loadSD(sequenceDiagram);
                    break;
                }
            }
        }
    };
}
