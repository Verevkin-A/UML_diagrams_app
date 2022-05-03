package userInterface.SDInterface;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import sequenceDiagram.SDMessage;

/**
 * Message GUI attributes connector
 * @author Aleksandr Verevkin (xverev00)
 * @since 2022-04-15
 */
public class UIMessageConnector {
    private final SDMessage sdMessage;
    private final Label lMessage;
    private final Button bDeleteMessage;

    /**
     * UIMessageConnector constructor
     * @param sdMessage Sequence Diagram message
     * @param lMessage message identifier Label
     * @param bDeleteMessage delete message Button
     */
    public UIMessageConnector(SDMessage sdMessage, Label lMessage, Button bDeleteMessage) {
        this.sdMessage = sdMessage;
        this.lMessage = lMessage;
        this.bDeleteMessage = bDeleteMessage;
    }

    /**
     * Sequence Diagram message getter
     * @return Sequence Diagram message
     */
    public SDMessage getsdMessage() {
        return sdMessage;
    }

    /**
     * message identifier Label getter
     * @return message identifier Label
     */
    public Label getlMessage() {
        return lMessage;
    }

    /**
     * message delete Button getter
     * @return message delete Button
     */
    public Button getbDeleteMessage() {
        return bDeleteMessage;
    }
}
