package userInterface.SDInterface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sequenceDiagram.SDActivation;
import sequenceDiagram.SDObject;

import java.util.ArrayList;

/**
 * Activation form window controller
 * @author Aleksandr Verevkin (xverev00)
 * @since 2022-04-15
 */
public class ActivationFormController {

    private SDController sdController;

    @FXML
    private ComboBox<UIObjectConnector> cbObjects;

    @FXML
    private TextField tfBeginTime, tdEndTime;

    /**
     * SDController setter
     * @param sdController SDController to set
     */
    public void setSDController(SDController sdController) {
        this.sdController = sdController;
    }

    /**
     * Objects to chose from
     * @param uiObjectConnectors connectors with existing objects
     */
    public void setObjects(ArrayList<UIObjectConnector> uiObjectConnectors) {
        ObservableList<UIObjectConnector> objects = FXCollections.observableList(uiObjectConnectors);
        cbObjects.setItems(objects);
    }

    /**
     * Done button action handler
     */
    @FXML
    void doneAction() {
        // check if object is selected
        if (cbObjects.getValue() == null) {
            showWarning("Unselected object", "Object for the new activation is unselected");
            return;
        }

        String beginTimeString = tfBeginTime.getText();
        String endTimeString = tdEndTime.getText();
        int beginTimeInt, endTimeInt;
        try {
            beginTimeInt = Integer.parseInt(beginTimeString);
            endTimeInt = Integer.parseInt(endTimeString);
            if (beginTimeInt >= endTimeInt || beginTimeInt < 0 || beginTimeInt > 100 || endTimeInt > 100) {
                throw new java.lang.NumberFormatException("Invalid time integer");
            }
        } catch (NumberFormatException e) {
            showWarning("Invalid time", "Time can only be integer from 0 to 100.\nEnd time must be greater than begin time");
            return;
        }

        SDObject object = cbObjects.getValue().getObject();
        SDActivation newActivation = new SDActivation(beginTimeInt, endTimeInt);
        // check if activation time is valid
        if (!object.checkActivation(newActivation)) {
            showWarning("Invalid time", "Activation not within the lifeline of the object");
            return;
        }
        for (SDActivation a: object.getActivations()) {
            int beginTimeExist = a.getTimeBegin();
            int endTimeExist = a.getTimeEnd();
            if (object.getTimePos() > beginTimeInt ||
                    (beginTimeExist > beginTimeInt && beginTimeExist < endTimeInt) ||
                    (endTimeExist > beginTimeInt && endTimeExist < endTimeInt) ||
                    (beginTimeInt > beginTimeExist && beginTimeInt < endTimeExist) ||
                    (endTimeInt > beginTimeExist && endTimeInt < endTimeExist)) {
                showWarning("Invalid time", "There are existing activation within the selected timeline");
                return;
            }
        }
        // add activation on the pane
        sdController.putActivation(object, newActivation);
        ((Stage) tfBeginTime.getScene().getWindow()).close();
    }

    /**
     * Show warning message
     * @param title window title
     * @param content warning message content
     */
    private void showWarning(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
