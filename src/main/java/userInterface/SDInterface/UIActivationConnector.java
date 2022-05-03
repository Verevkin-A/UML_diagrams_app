package userInterface.SDInterface;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import sequenceDiagram.SDActivation;
import sequenceDiagram.SDObject;

/**
 * Activation GUI attributes connector
 * @author Aleksandr Verevkin (xverev00)
 * @since 2022-04-15
 */
public class UIActivationConnector {
    private final SDActivation sdActivation;
    private final SDObject sdObject;
    private final Label lActivation;
    private final Button bDeleteActivation;

    /**
     * UIActivationConnector constructor
     * @param sdActivation Sequence Diagram activation
     * @param sdObject activation object
     * @param lActivation activation identifier Label
     * @param bDeleteActivation delete activation Button
     */
    public UIActivationConnector(SDActivation sdActivation, SDObject sdObject, Label lActivation, Button bDeleteActivation) {
        this.sdActivation = sdActivation;
        this.sdObject = sdObject;
        this.lActivation = lActivation;
        this.bDeleteActivation = bDeleteActivation;
    }

    /**
     * Sequence Diagram activation getter
     * @return Sequence Diagram activation
     */
    public SDActivation getsdActivation() {
        return sdActivation;
    }

    /**
     * activation object getter
     * @return activation object
     */
    public SDObject getsdObject() {
        return sdObject;
    }

    /**
     * activation identifier Label getter
     * @return activation identifier Label
     */
    public Label getlActivation() {
        return lActivation;
    }

    /**
     * activation delete Button getter
     * @return activation delete Button
     */
    public Button getbDeleteActivation() {
        return bDeleteActivation;
    }
}
