package userInterface.SDInterface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class ActivationFormController {

    private SDController sdController;

    @FXML
    private ComboBox<UIObjectConnector> cbObjects;

    @FXML
    private TextField tdEndTime, tfBeginTime;

    public void setSDController(SDController sdController) {
        this.sdController = sdController;
    }

    public void setObjects(ArrayList<UIObjectConnector> uiObjectConnectors) {
        ObservableList<UIObjectConnector> objects = FXCollections.observableList(uiObjectConnectors);
        cbObjects.setItems(objects);
    }

    @FXML
    void doneAction(ActionEvent event) {

    }
}
