package userInterface;

import classDiagram.NodeType;
import classDiagram.AnchorType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class NodeFormController {

    @FXML
    private ToggleGroup AnchorFrom, AnchorTo, NodeType;

    @FXML
    private ComboBox<UIConnector> cbFromClass, cbToClass;

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
        ObservableList<UIConnector> classes = FXCollections.observableList(Controller.getController().uiConnectors);
        cbFromClass.setItems(classes);
        cbToClass.setItems(classes);
    }

    @FXML
    void doneAction(ActionEvent event) {
        Controller.getController().putNode(cbFromClass.getValue(), cbToClass.getValue(),
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

