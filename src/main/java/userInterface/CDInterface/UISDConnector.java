package userInterface.CDInterface;

import sequenceDiagram.SequenceDiagram;

import javafx.scene.control.Label;
import javafx.scene.control.Button;

/**
 * Connection between all sequence diagrams attributes
 * @author Aleksandr Verevkin (xverev00)
 * @since 2022-04-15
 */
public class UISDConnector {
    private SequenceDiagram sequenceDiagram;
    private final Label lName;
    private final Button bEditSD, bDeleteSD;

    /**
     * UISDConnector constructor
     * @param sd connector Sequence Diagram
     * @param lName Sequence Diagram identifier Label
     * @param bEdit Sequence Diagram edit Button
     * @param bDelete Sequence Diagram delete Button
     */
    public UISDConnector(SequenceDiagram sd, Label lName, Button bEdit, Button bDelete) {
        this.sequenceDiagram = sd;
        this.lName = lName;
        this.bEditSD = bEdit;
        this.bDeleteSD = bDelete;
    }

    /**
     * Sequence Diagram getter
     * @return Sequence Diagram
     */
    public SequenceDiagram getSequenceDiagram() {
        return sequenceDiagram;
    }

    /**
     * Sequence Diagram setter
     * @param sequenceDiagram Sequence Diagram to set
     */
    public void setSequenceDiagram(SequenceDiagram sequenceDiagram) {
        this.sequenceDiagram = sequenceDiagram;
    }

    /**
     * Sequence Diagram identifier Label getter
     * @return Sequence Diagram identifier Label
     */
    public Label getlName() {
        return lName;
    }

    /**
     * Sequence Diagram edit Button getter
     * @return Sequence Diagram edit Button
     */
    public Button getbEditSD() {
        return bEditSD;
    }

    /**
     * Sequence Diagram delete Button getter
     * @return Sequence Diagram delete Button
     */
    public Button getbDeleteSD() {
        return bDeleteSD;
    }
}
