package userInterface.SDInterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class MessageFormController {

    @FXML
    private ComboBox<?> cbFrom, cbTo;

    @FXML
    private RadioButton rbCall, rbCreate, rbDestroy, rbReturn;

    @FXML
    private ToggleGroup tgMessageType;

    @FXML
    private TextField tfMessageName, tfTimePosition;

    @FXML
    void doneAction(ActionEvent event) {

    }
}
