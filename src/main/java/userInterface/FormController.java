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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


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
    void FormAction(ActionEvent event) {
        if (event.getSource() == bCreate) {
            this.createField();
        } else if (event.getSource() == bDelete) {
            this.deleteField();
        } else if (event.getSource() == bUpdate) {
            this.updateField();
        } else if (event.getSource() == bDone) {
            Controller.getController().putClass(tfClassName.getText(), tbInterface.isSelected(), tvFields);
            ((Stage) bDone.getScene().getWindow()).close();
        }
    }

    void createField() {
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcType.setCellValueFactory(new PropertyValueFactory<>("type"));
        tcVisibility.setCellValueFactory(new PropertyValueFactory<>("visibility"));
        FormField formField = new FormField(tfFieldName.getText(), getType(), getVisibility());
        tvFields.getItems().add(formField);
    }

    void deleteField() {
        try {
            tvFields.getItems().removeAll(tvFields.getSelectionModel().getSelectedItem());
        } catch (Exception e) {
            System.out.println("Nothing is selected");
        }
    }

    void updateField() {
        try {
            FormField formField = tvFields.getSelectionModel().getSelectedItem();
            formField.setName(tfFieldName.getText());
            formField.setType(getType());
            formField.setVisibility(getVisibility());
            tvFields.refresh();
        } catch (Exception e) {
            System.out.println("Nothing is selected");
        }
    }

    @FXML
    void tvSelectRow(MouseEvent event) {
        FormField formField = tvFields.getSelectionModel().getSelectedItem();
        tfFieldName.setText(formField.getName());
        switch (formField.getType()) {
            case "Field":
                rbField.fire();
                break;
            case "Method":
                rbMethod.fire();
        }
        switch (formField.getVisibility()) {
            case "+":
                rbPublic.fire();
                break;
            case "-":
                rbPrivate.fire();
                break;
            case "#":
                rbProtected.fire();
                break;
            case "~":
                rbPackage.fire();
                break;
        }
    }

    private String getVisibility() {
        return ((RadioButton) Visibility.getSelectedToggle()).getText();
    }

    private String getType() {
        return ((RadioButton) Type.getSelectedToggle()).getText();
    }

}

