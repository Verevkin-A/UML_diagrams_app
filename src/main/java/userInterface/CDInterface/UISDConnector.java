package userInterface.CDInterface;

import sequenceDiagram.SequenceDiagram;

import javafx.scene.control.Label;
import javafx.scene.control.Button;

public class UISDConnector {
    private final SequenceDiagram sequenceDiagram;
    private final Label lName;
    private final Button bEditSD, bDeleteSD;

    public UISDConnector(SequenceDiagram sd, Label lName, Button bEdit, Button bDelete) {
        this.sequenceDiagram = sd;
        this.lName = lName;
        this.bEditSD = bEdit;
        this.bDeleteSD = bDelete;
    }

    public SequenceDiagram getSequenceDiagram() {
        return sequenceDiagram;
    }

    public Label getlName() {
        return lName;
    }

    public Button getbEditSD() {
        return bEditSD;
    }

    public Button getbDeleteSD() {
        return bDeleteSD;
    }
}
