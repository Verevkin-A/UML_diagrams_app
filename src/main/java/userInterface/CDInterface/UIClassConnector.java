package userInterface.CDInterface;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

/**
 * Connection between all single class objects on the pane
 * @author Aleksandr Verevkin (xverev00)
 * @since 2022-04-02
 */
public class UIClassConnector {
    private final TitledPane tpClass;
    private final VBox vbFields;
    private final Double axisX, axisY;
    private final Boolean interface_;
    private final TableView<FormField> tableView;
    private final Label classNameLabel;
    private final Button btnEdit, btnDelete;

    /**
     * Class connection constructor
     *
     * @param tpClass class titled pane on the pane
     * @param axisX class titled pane X axis
     * @param axisY class titled pane Y axis
     * @param interface_ is class an interface
     * @param tableView class table view with fields and methods
     * @param classNameLabel class label object on grid pane
     * @param btnEdit class edit button object on grid pane
     * @param btnDelete class delete button object on grid pane
     */
    public UIClassConnector(TitledPane tpClass, VBox vbFields, Double axisX, Double axisY, Boolean interface_, TableView<FormField> tableView,
                            Label classNameLabel, Button btnEdit, Button btnDelete) {

        this.tpClass = tpClass;
        this.vbFields = vbFields;
        this.axisX = axisX;
        this.axisY = axisY;
        this.tableView = tableView;
        this.interface_ = interface_;
        this.classNameLabel = classNameLabel;
        this.btnEdit = btnEdit;
        this.btnDelete = btnDelete;
    }

    /**
     * Class titled pane getter
     *
     * @return class titled pane connected with pane
     */
    public TitledPane getTpClass() {
        return tpClass;
    }

    public VBox getVbFields() {
        return vbFields;
    }

    /**
     * Titled pane X axis getter
     *
     * @return class titled pane X axis
     */
    public Double getAxisX() {
        return axisX;
    }

    /**
     * Titled pane Y axis getter
     *
     * @return class titled pane Y axis
     */
    public Double getAxisY() {
        return axisY;
    }

    /**
     * Is class an interface getter
     *
     * @return true if class is interface, false otherwise
     */
    public Boolean getInterface_() {
        return interface_;
    }

    /**
     * Table view with class fields/methods getter
     *
     * @return class tableview
     */
    public TableView<FormField> getTableView() {
        return tableView;
    }

    /**
     * Class label object on grid pane getter
     *
     * @return class name label on grid pane
     */
    public Label getClassNameLabel() {
        return classNameLabel;
    }

    /**
     * Class edit button object on grid pane getter
     *
     * @return class edit button on grid pane
     */
    public Button getBtnEdit() {
        return btnEdit;
    }

    /**
     * Class delete button object on grid pane getter
     *
     * @return class delete button on grid pane
     */
    public Button getBtnDelete() {
        return btnDelete;
    }

    @Override
    public String toString() {
        return classNameLabel.getText();
    }
}
