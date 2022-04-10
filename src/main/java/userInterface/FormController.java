package userInterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;

public class FormController {

    @FXML
    private ToggleGroup Visibility;

    @FXML
    private ToggleGroup Type;

    @FXML
    private Button bCreate, bDelete, bDone, bUpdate;

    @FXML
    private RadioButton rbPackage, rbPrivate, rbProtected, rbPublic, rbField, rbMethod;

    @FXML
    private ToggleButton tbInterface;

    @FXML
    private TableColumn<FormField, String> tcName, tcType, tcVisibility;

    @FXML
    private TextField tfClassName, tfFieldName;

    @FXML
    private TableView<FormField> tvFields;

    @FXML
    void ClassAction(ActionEvent event) {
        if (event.getSource() == bCreate) {
            tcName.setCellValueFactory(new PropertyValueFactory<FormField, String>("name"));
            tcType.setCellValueFactory(new PropertyValueFactory<FormField, String>("type"));
            tcVisibility.setCellValueFactory(new PropertyValueFactory<FormField, String>("visibility"));
            FormField formField = new FormField(tfFieldName.getText(), getType(), this.getVisibility());
            tvFields.getItems().add(formField);
        } else if (event.getSource() == bDelete) {
            tvFields.getItems().removeAll(tvFields.getSelectionModel().getSelectedItem());
        } else if (event.getSource() == bUpdate) {

        } else if (event.getSource() == bDone) {

        }
    }

    private String getVisibility() {
        return ((RadioButton) this.Visibility.getSelectedToggle()).getText();
    }

    private String getType() {
        return ((RadioButton) this.Type.getSelectedToggle()).getText();
    }

}

