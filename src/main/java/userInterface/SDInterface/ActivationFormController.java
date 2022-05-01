package userInterface.SDInterface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sequenceDiagram.SDActivation;
import sequenceDiagram.SDObject;

import java.util.ArrayList;

public class ActivationFormController {

    private SDController sdController;

    @FXML
    private ComboBox<UIObjectConnector> cbObjects;

    @FXML
    private TextField tfBeginTime, tdEndTime;

    public void setSDController(SDController sdController) {
        this.sdController = sdController;
    }

    public void setObjects(ArrayList<UIObjectConnector> uiObjectConnectors) {
        ObservableList<UIObjectConnector> objects = FXCollections.observableList(uiObjectConnectors);
        cbObjects.setItems(objects);
    }

    @FXML
    void doneAction(ActionEvent event) {
        // check if object is selected
        if (cbObjects.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Unselected object");
            alert.setHeaderText(null);
            alert.setContentText("Object for the new activation is unselected");

            alert.showAndWait();
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
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid time");
            alert.setHeaderText(null);
            alert.setContentText("Time can only be integer from 0 to 100.\nEnd time must be greater than begin time");

            alert.showAndWait();
            return;
        }

        SDObject object = cbObjects.getValue().getObject();
        // check if activation time is valid
        System.out.println("Object time: " + object.getTimePos());
        for (SDActivation a: object.getActivations()) {
            int beginTimeExist = a.getTimeBegin();
            int endTimeExist = a.getTimeEnd();
            System.out.println("Activation start: " + beginTimeExist);
            System.out.println("Activation end: " + a.getTimeEnd());
            if (object.getTimePos() > beginTimeInt ||
                    (beginTimeExist > beginTimeInt && beginTimeExist < endTimeInt) ||
                    (endTimeExist > beginTimeInt && endTimeExist < endTimeInt) ||
                    (beginTimeInt > beginTimeExist && beginTimeInt < endTimeExist) ||
                    (endTimeInt > beginTimeExist && endTimeInt < endTimeExist)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Invalid time");
                alert.setHeaderText(null);
                alert.setContentText("Selected time interval for the selected object either already have activation \n" +
                        "or not within the lifeline of the object.");

                alert.showAndWait();
                return;
            }
        }
        // add activation on the pane
        sdController.putActivation(object, new SDActivation(beginTimeInt, endTimeInt));
        ((Stage) tfBeginTime.getScene().getWindow()).close();
    }
}
