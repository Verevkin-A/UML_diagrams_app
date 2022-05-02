package userInterface.SDInterface;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import sequenceDiagram.SDActivation;
import sequenceDiagram.SDObject;

public class UIActivationConnector {
    private SDActivation sdActivation;
    private SDObject sdObject;
    private Label lActivation;
    private Button bDeleteActivation;

    public UIActivationConnector(SDActivation sdActivation, SDObject sdObject, Label lActivation, Button bDeleteActivation) {
        this.sdActivation = sdActivation;
        this.sdObject = sdObject;
        this.lActivation = lActivation;
        this.bDeleteActivation = bDeleteActivation;
    }

    public SDActivation getsdActivation() {
        return sdActivation;
    }

    public SDObject getsdObject() {
        return sdObject;
    }

    public Label getlActivation() {
        return lActivation;
    }

    public Button getbDeleteActivation() {
        return bDeleteActivation;
    }
}
