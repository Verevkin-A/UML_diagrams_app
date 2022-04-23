package userInterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Class form window controller
 * @author Aleksandr Verevkin (xverev00)
 * @since 2022-04-02
 */
public class ClassFormController {

    @FXML
    private ToggleGroup Visibility, Type;

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

    /**
     * Form window initialization
     */
    @FXML
    public void initialize() {
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcType.setCellValueFactory(new PropertyValueFactory<>("type"));
        tcVisibility.setCellValueFactory(new PropertyValueFactory<>("visibility"));
    }

    /**
     * Form button events handler
     *
     * @param event pressed button
     */
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

    /**
     * New field button handler
     */
    void createField() {
        FormField formField = new FormField(tfFieldName.getText(), getType(), getVisibility());
        tvFields.getItems().add(formField);
    }

    /**
     * Delete field button handler
     */
    void deleteField() {
        try {
            tvFields.getItems().removeAll(tvFields.getSelectionModel().getSelectedItem());
        } catch (Exception e) {
            System.out.println("Nothing is selected");
        }
    }

    /**
     * Update field button handler
     */
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

    /**
     * Table view dynamic row selection
     *
     * @param event selected row
     */
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

    /**
     * Current visibility radio button selection getter
     *
     * @return selected visibility toggle text
     */
    private String getVisibility() {
        return ((RadioButton) Visibility.getSelectedToggle()).getText();
    }

    /**
     * Current type radio button selection getter
     *
     * @return selected type toggle text
     */
    private String getType() {
        return ((RadioButton) Type.getSelectedToggle()).getText();
    }
}
