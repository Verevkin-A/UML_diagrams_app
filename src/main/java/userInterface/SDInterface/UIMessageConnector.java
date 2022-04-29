package userInterface.SDInterface;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import sequenceDiagram.SDMessage;

public class UIMessageConnector {
    private SDMessage sdMessage;
    private Label lMessage;
    private Button bDeleteMessage;

    public UIMessageConnector(SDMessage sdMessage, Label lMessage, Button bDeleteMessage) {
        this.sdMessage = sdMessage;
        this.lMessage = lMessage;
        this.bDeleteMessage = bDeleteMessage;
    }

    public SDMessage getsdMessage() {
        return sdMessage;
    }

    public Label getlMessage() {
        return lMessage;
    }

    public Button getbDeleteMessage() {
        return bDeleteMessage;
    }
}
