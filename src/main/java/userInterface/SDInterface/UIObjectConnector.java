package userInterface.SDInterface;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import sequenceDiagram.SDObject;

public class UIObjectConnector {
    private SDObject object;
    private Label lObject;
    private Button bEditObject, bDeleteObject;

    public UIObjectConnector(SDObject object, Label lObject, Button bEditObject, Button bDeleteObject) {
        this.object = object;
        this.lObject = lObject;
        this.bEditObject = bEditObject;
        this.bDeleteObject = bDeleteObject;
    }

    public SDObject getObject() {
        return object;
    }

    public Label getlObject() {
        return lObject;
    }

    public Button getbEditObject() {
        return bEditObject;
    }

    public Button getbDeleteObject() {
        return bDeleteObject;
    }
}
