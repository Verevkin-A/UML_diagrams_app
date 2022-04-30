package userInterface.SDInterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import userInterface.CDInterface.CDController;
import userInterface.CDInterface.UIClassConnector;

import java.util.Objects;

public class ObjectFormController {

    private SDController sdController;

    @FXML
    private TextField tfClassName, tfObjectName, tfTimePosition;

    public void setSDController(SDController sdController) {
        this.sdController = sdController;
    }

    @FXML
    void doneAction(ActionEvent event) {
        String objectName = tfObjectName.getText();
        String className = tfClassName.getText();
        String timePositionString = Objects.equals(tfTimePosition.getText(), "") ? "-1" : tfTimePosition.getText();
        int timePositionInt;
        try {
            timePositionInt = Integer.parseInt(timePositionString);
            if (timePositionInt < -1 || timePositionInt > 100) {
                throw new java.lang.NumberFormatException("Invalid timeline position");
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid time");
            alert.setHeaderText(null);
            alert.setContentText("Time position can only be integer from 0 to 100.\n-1 or empty for above the timeline");

            alert.showAndWait();
            return;
        }

        // check for consistency with class diagram
        boolean inconsistency = true;
        for (UIClassConnector c: CDController.getController().uiClassConnectors) {
            if (Objects.equals(c.getClassNameLabel().getText(), className)) {
                inconsistency = false;
                break;
            }
        }
        // throw conformation window in case of inconsistency
        if (inconsistency) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Inconsistency");
            alert.setHeaderText(null);
            alert.setContentText("There are no class named '" + className + "' in class diagram. Still proceed?");

            alert.showAndWait();
            if (alert.getResult() != ButtonType.OK){
                return;
            }
        }
        // add new object
        sdController.putObject(objectName, className, timePositionInt);
        ((Stage) tfClassName.getScene().getWindow()).close();
    }
}
