package userInterface.SDInterface;

import classDiagram.ClassDiagram;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sequenceDiagram.MessageType;
import sequenceDiagram.SDMessage;
import userInterface.CDInterface.CDController;

import java.util.ArrayList;

public class MessageFormController {

    private SDController sdController;

    @FXML
    private ComboBox<UIObjectConnector> cbFrom, cbTo;

    @FXML
    private RadioButton rbCall, rbCreate, rbDestroy, rbReturn;

    @FXML
    private ToggleGroup tgMessageType;

    @FXML
    private TextField tfMessageName, tfTimePosition;

    public void setSDController(SDController sdController) {
        this.sdController = sdController;
    }

    public void setObjects(ArrayList<UIObjectConnector> uiObjectConnectors) {
        ObservableList<UIObjectConnector> classes = FXCollections.observableList(uiObjectConnectors);
        cbFrom.setItems(classes);
        cbTo.setItems(classes);
    }

    @FXML
    void doneAction() {
        if (cbFrom.getValue() == null || cbTo.getValue() == null) {
            showWarning("Unselected object", "One or both objects are not selected");
            return;
        }

        String timePosStr = tfTimePosition.getText();
        int timePosInt;
        try {
            timePosInt = Integer.parseInt(timePosStr);      // TODO check destroyed lifeline?
            if (timePosInt < cbFrom.getValue().getObject().getTimePos() ||
                    timePosInt < cbTo.getValue().getObject().getTimePos() ||
                    timePosInt < 0 || timePosInt > 100) {
                throw new java.lang.NumberFormatException("Invalid time integer");
            }
        } catch (NumberFormatException e) {
            showWarning("Invalid time", "Time position must be integer from 0 to 100 and can only be placed within both objects lifeline");
            return;
        }

        String msgName = tfMessageName.getText();
        ClassDiagram currentCD = CDController.getController().saveCD();
        if (!SDMessage.checkConsistency(currentCD, cbTo.getValue().getObject(), msgName)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Inconsistency");
            alert.setHeaderText(null);
            alert.setContentText("There are no method named '" + msgName + "' in selected class. Still proceed?");

            alert.showAndWait();
            if (alert.getResult() != ButtonType.OK){
                return;
            }
        }

        sdController.putMessage(msgName, cbFrom.getItems().indexOf(cbFrom.getValue()), cbTo.getItems().indexOf(cbTo.getValue()),
                getMessageType(tgMessageType.getSelectedToggle()), timePosInt);
        ((Stage) tfMessageName.getScene().getWindow()).close();
    }

    private MessageType getMessageType(Toggle rbType) {
        if (rbType == rbCall) {
            return MessageType.CALL;
        } else if (rbType == rbReturn) {
            return MessageType.RETURN;
        } else if (rbType == rbCreate) {
            return MessageType.CREATE_OBJ;
        } else if (rbType == rbDestroy) {
            return MessageType.DESTROY_OBJ;
        }
        return null;
    }

    private void showWarning(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
