package userInterface.SDInterface;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import sequenceDiagram.SDActivation;

public class UIActivationConnector {
    private SDActivation sdActivation;
    private Label lActivation;
    private Button bDeleteActivation;

    public UIActivationConnector(SDActivation sdActivation, Label lActivation, Button bDeleteActivation) {
        this.sdActivation = sdActivation;
        this.lActivation = lActivation;
        this.bDeleteActivation = bDeleteActivation;
    }

    public SDActivation getsdActivation() {
        return sdActivation;
    }

    public Label getlActivation() {
        return lActivation;
    }

    public Button getbDeleteActivation() {
        return bDeleteActivation;
    }
}
