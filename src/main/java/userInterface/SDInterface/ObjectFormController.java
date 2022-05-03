package userInterface.SDInterface;

import classDiagram.ClassDiagram;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sequenceDiagram.SDActivation;
import sequenceDiagram.SDMessage;
import sequenceDiagram.SDObject;
import userInterface.CDInterface.CDController;

import java.util.Objects;

public class ObjectFormController {

    private SDController sdController;
    private SDObject editingObject = null;

    @FXML
    private TextField tfClassName, tfObjectName, tfTimePosition;

    public void setSDController(SDController sdController) {
        this.sdController = sdController;
    }

    public void setEdit(UIObjectConnector cObject) {
        SDObject object = cObject.getObject();
        tfClassName.setText(object.getClassName());
        tfObjectName.setText(object.getObjName());
        tfTimePosition.setText(Integer.toString(object.getTimePos()));
        editingObject = object;
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
            showWarning("Invalid time", "Time position can only be integer from 0 to 100.\n-1 or empty for above the timeline");
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

        if (editingObject != null) {
            // check if new time position overlaps with existing activations
            for (SDActivation a: editingObject.getActivations()) {
                if (a.getTimeBegin() < timePositionInt) {
                    showWarning("Invalid time", "New time position overlaps with existing activation. Remove activation or change time position");
                    return;
                }
            }
            // check if new time position overlaps with existing messages
            for (SDMessage m: sdController.sequenceDiagram.getMessages()) {
                if ((m.getTo() == editingObject || m.getFrom() == editingObject) &&
                        (m.getTimePos() < timePositionInt)) {
                    showWarning("Invalid time", "New time position overlaps with existing message. Remove message or change time position");
                    return;
                }
            }

            sdController.undoSave();
            // edit existing object
            editingObject.setObjName(objectName);
            editingObject.setClassName(className);
            editingObject.setTimePos(timePositionInt);

            sdController.loadSD(sdController.sequenceDiagram);
        } else {
            // add new object
            sdController.putObject(objectName, className, timePositionInt);
        }
        ((Stage) tfClassName.getScene().getWindow()).close();
    }

    private void showWarning(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
