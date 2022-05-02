package userInterface.SDInterface;

import classDiagram.ClassDiagram;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sequenceDiagram.SDObject;
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
    void doneAction() {
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

        String objectName = tfObjectName.getText();
        String className = tfClassName.getText();
        // check for consistency with class diagram
        ClassDiagram currentCD = CDController.getController().saveCD();
        if (!SDObject.checkClassName(currentCD, className)) {
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
