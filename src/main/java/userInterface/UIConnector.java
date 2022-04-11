package userInterface;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;

public class UIConnector {
    private TitledPane tpClass;
    private Double axisX;
    private Double axisY;
    private Boolean interface_;
    private TableView<FormField> tableView;
    private Label classNameLabel;
    private Button btnEdit;
    private Button btnDelete;

    public UIConnector(TitledPane tpClass, Double axisX, Double axisY, Boolean interface_, TableView<FormField> tableView,
                       Label classNameLabel, Button btnEdit, Button btnDelete) {

        this.tpClass = tpClass;
        this.axisX = axisX;
        this.axisY = axisY;
        this.tableView = tableView;
        this.interface_ = interface_;
        this.classNameLabel = classNameLabel;
        this.btnEdit = btnEdit;
        this.btnDelete = btnDelete;
    }

    public TitledPane getTpClass() {
        return tpClass;
    }

    public Double getAxisX() {
        return axisX;
    }

    public Double getAxisY() {
        return axisY;
    }

    public Boolean getInterface_() {
        return interface_;
    }

    public TableView<FormField> getTableView() {
        return tableView;
    }

    public Label getClassNameLabel() {
        return classNameLabel;
    }

    public Button getBtnEdit() {
        return btnEdit;
    }

    public Button getBtnDelete() {
        return btnDelete;
    }
}
