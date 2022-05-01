package userInterface.CDInterface;

import classDiagram.NodeType;
import classDiagram.AnchorType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Objects;

public class NodeFormController {

    @FXML
    private ToggleGroup AnchorFrom, AnchorTo, NodeType;

    @FXML
    private ComboBox<UIClassConnector> cbFromClass, cbToClass;

    @FXML
    private RadioButton rbAggregation, rbAssociation, rbComposition, rbGeneralization;

    @FXML
    private RadioButton rbFromDown, rbFromLeft, rbFromRight, rbFromUp;

    @FXML
    private RadioButton rbToDown, rbToLeft, rbToRight, rbToUp;

    @FXML
    private TextField tfFromCardinality, tfToCardinality;

    @FXML
    private Button btnDone;

    /**
     * Form window initialization
     */
    @FXML
    public void initialize() {
        ObservableList<UIClassConnector> classes = FXCollections.observableList(CDController.getController().uiClassConnectors);
        cbFromClass.setItems(classes);
        cbToClass.setItems(classes);
    }

    @FXML
    void doneAction() {
        // check if every required field is selected
        // if not show warning message
        if (cbFromClass.getValue() == null || cbToClass.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Unselected class");
            alert.setHeaderText(null);
            alert.setContentText("One or both classes are not selected");

            alert.showAndWait();
            return;
        }
        if (!Objects.equals(((RadioButton) NodeType.getSelectedToggle()).getText(), "Generalization") &&
                (Objects.equals(tfFromCardinality.getText(), "") || Objects.equals(tfToCardinality.getText(), ""))) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Unselected cardinality");
            alert.setHeaderText(null);
            alert.setContentText("One or both cardinality fields are empty, but required for the selected type of node");

            alert.showAndWait();
            return;
        }
        if (Objects.equals(((RadioButton) NodeType.getSelectedToggle()).getText(), "Generalization") &&
                cbFromClass.getValue() == cbToClass.getValue()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Unary generalization");
            alert.setHeaderText(null);
            alert.setContentText("Unary generalization is not supported");

            alert.showAndWait();
            return;
        }
        CDController.getController().putNode(cbFromClass.getValue(), cbToClass.getValue(),
                getFromAnchorType((RadioButton) AnchorFrom.getSelectedToggle()),
                getToAnchorType((RadioButton) AnchorTo.getSelectedToggle()),
                tfFromCardinality.getText(), tfToCardinality.getText(),
                getNodeType((RadioButton) NodeType.getSelectedToggle()));
        ((Stage) btnDone.getScene().getWindow()).close();
    }

    private AnchorType getFromAnchorType(RadioButton rbAnchor) {
        if (rbAnchor == rbFromUp) {
            return classDiagram.AnchorType.valueOfLabel("UP");
        } else if (rbAnchor == rbFromDown) {
            return classDiagram.AnchorType.valueOfLabel("DOWN");
        } else if (rbAnchor == rbFromLeft) {
            return classDiagram.AnchorType.valueOfLabel("LEFT");
        } else if (rbAnchor == rbFromRight) {
            return classDiagram.AnchorType.valueOfLabel("RIGHT");
        }
        return null;
    }

    private AnchorType getToAnchorType(RadioButton rbAnchor) {
        if (rbAnchor == rbToUp) {
            return classDiagram.AnchorType.valueOfLabel("UP");
        } else if (rbAnchor == rbToDown) {
            return classDiagram.AnchorType.valueOfLabel("DOWN");
        } else if (rbAnchor == rbToLeft) {
            return classDiagram.AnchorType.valueOfLabel("LEFT");
        } else if (rbAnchor == rbToRight) {
            return classDiagram.AnchorType.valueOfLabel("RIGHT");
        }
        return null;
    }

    private NodeType getNodeType(RadioButton rbType) {
        if (rbType == rbAggregation) {
            return classDiagram.NodeType.valueOfLabel(0);
        } else if (rbType == rbAssociation) {
            return classDiagram.NodeType.valueOfLabel(1);
        } else if (rbType == rbGeneralization) {
            return classDiagram.NodeType.valueOfLabel(2);
        } else if (rbType == rbComposition) {
            return classDiagram.NodeType.valueOfLabel(3);
        }
        return null;
    }
}
