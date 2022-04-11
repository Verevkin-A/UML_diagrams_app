package userInterface;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;

public class UIConnector {
    private Label classNameLabel;
    private TitledPane tpClass;
    private Button btnEdit;
    private Button btnDelete;

    public UIConnector(Label classNameLabel, TitledPane tpClass, Button btnEdit, Button btnDelete) {
        this.classNameLabel = classNameLabel;
        this.tpClass = tpClass;
        this.btnEdit = btnEdit;
        this.btnDelete = btnDelete;
    }

    public Label getClassNameLabel() {
        return classNameLabel;
    }

    public TitledPane getTpClass() {
        return tpClass;
    }

    public Button getBtnEdit() {
        return btnEdit;
    }

    public Button getBtnDelete() {
        return btnDelete;
    }
}
