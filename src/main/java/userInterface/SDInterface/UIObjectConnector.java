package userInterface.SDInterface;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import sequenceDiagram.SDObject;

/**
 * Object GUI attributes connector
 * @author Aleksandr Verevkin (xverev00)
 * @since 2022-04-15
 */
public class UIObjectConnector {
    private final SDObject object;
    private final Label lObject;
    private final Button bEditObject, bDeleteObject;

    /**
     * UIObjectConnector constructor
     * @param object Sequence Diagram object
     * @param lObject object identifier Label
     * @param bEditObject object edit Button
     * @param bDeleteObject object delete Button
     */
    public UIObjectConnector(SDObject object, Label lObject, Button bEditObject, Button bDeleteObject) {
        this.object = object;
        this.lObject = lObject;
        this.bEditObject = bEditObject;
        this.bDeleteObject = bDeleteObject;
    }

    /**
     * Sequence Diagram object getter
     * @return Sequence Diagram object
     */
    public SDObject getObject() {
        return object;
    }

    /**
     * object identifier Label getter
     * @return object identifier Label
     */
    public Label getlObject() {
        return lObject;
    }

    /**
     * object edit Button getter
     * @return object edit Button
     */
    public Button getbEditObject() {
        return bEditObject;
    }

    /**
     * object delete Button getter
     * @return object delete Button
     */
    public Button getbDeleteObject() {
        return bDeleteObject;
    }

    @Override
    public String toString() {
        return lObject.getText();
    }
}
