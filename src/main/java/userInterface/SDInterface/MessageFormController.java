package userInterface.SDInterface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

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


        ((Stage) tfMessageName.getScene().getWindow()).close();
    }
}
