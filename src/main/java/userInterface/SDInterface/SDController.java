package userInterface.SDInterface;

import classDiagram.NodeType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import sequenceDiagram.*;
import userInterface.CDInterface.CDController;
import userInterface.CDInterface.UIClassConnector;
import userInterface.CDInterface.UINodeConnector;
import userInterface.CDInterface.UISDConnector;

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
            // TODO onaction
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
                // TODO onaction
                for (Node child : gpActivations.getChildren()) {
                    GridPane.setRowIndex(child, GridPane.getRowIndex(child) + 1);
                }
                gpActivations.addRow(0, lActivation, bDeleteActivation);
                uiActivationConnectors.add(new UIActivationConnector(a, lActivation, bDeleteActivation));
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
            // TODO onaction
            for (Node child : gpMessages.getChildren()) {
                GridPane.setRowIndex(child, GridPane.getRowIndex(child) + 1);
            }
            gpMessages.addRow(0, lMessage, bDeleteMessage);
            uiMessageConnectors.add(new UIMessageConnector(m, lMessage, bDeleteMessage));
        }
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
    void addActivation(ActionEvent event) {

    }

    @FXML
    void addMessage(ActionEvent event) {

    }

    @FXML
    void addObject(ActionEvent event) {

    }

    @FXML
    void saveAction(ActionEvent event) {
        // save sequence diagram into memory
        CDController.getController().saveSD(sequenceDiagram);
    }

    @FXML
    void undoAction(ActionEvent event) {

    }
}
