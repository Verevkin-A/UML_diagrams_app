package userInterface.SDInterface;

import classDiagram.NodeType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import sequenceDiagram.*;

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

            for (SDActivation a: o.getActivations()) {
                double timeBegin = a.getTimeBegin();
                double timeEnd = a.getTimeEnd();
                Rectangle recActivation = new Rectangle(10, (timeEnd - timeBegin) / 100 * (timeLineEndY - timeLineStartY), Color.WHITESMOKE);
                recActivation.setStroke(Color.BLACK);
                AnchorPane.setTopAnchor(recActivation, timeLineStartY + (timeBegin * timeLineOneUnit));
                AnchorPane.setLeftAnchor(recActivation, inc + 110.0);
                root.getChildren().add(recActivation);
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
        System.out.println("Save");
    }

    @FXML
    void undoAction(ActionEvent event) {

    }
}
