package userInterface.CDInterface;

import classDiagram.NodeType;
import classDiagram.AnchorType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Node form window CDController
 * @author Aleksandr Verevkin (xverev00)
 * @since 2022-04-15
 */
public class NodeFormController {

    @FXML
    private ToggleGroup AnchorFrom, AnchorTo, nodeType;

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

    /**
     * Node form done(save node) action
     */
    @FXML
    void doneAction() {
        // check if every required field is selected
        // if not show warning message
        if (cbFromClass.getValue() == null || cbToClass.getValue() == null) {
            showWarning("Unselected class", "One or both classes are not selected");
            return;
        }
        if (Objects.equals(((RadioButton) nodeType.getSelectedToggle()).getText(), "Generalization") &&
                cbFromClass.getValue() == cbToClass.getValue()) {
            showWarning("Unary generalization", "Unary generalization is not supported");
            return;
        }
        CDController controller = CDController.getController();
        controller.undoSave();

        controller.putNode(cbFromClass.getValue(), cbToClass.getValue(),
                getFromAnchorType(AnchorFrom.getSelectedToggle()),
                getToAnchorType(AnchorTo.getSelectedToggle()),
                tfFromCardinality.getText(), tfToCardinality.getText(),
                getNodeType(nodeType.getSelectedToggle()));
        ((Stage) btnDone.getScene().getWindow()).close();
    }

    /**
     * Get FROM anchor from selected toggle
     * @param rbAnchor selected toggle
     * @return corresponding anchor type
     */
    private AnchorType getFromAnchorType(Toggle rbAnchor) {
        if (rbAnchor == rbFromUp) {
            return AnchorType.UP;
        } else if (rbAnchor == rbFromDown) {
            return AnchorType.DOWN;
        } else if (rbAnchor == rbFromLeft) {
            return AnchorType.LEFT;
        } else if (rbAnchor == rbFromRight) {
            return AnchorType.RIGHT;
        }
        return null;
    }

    /**
     * Get TO anchor from selected toggle
     * @param rbAnchor selected toggle
     * @return corresponding anchor type
     */
    private AnchorType getToAnchorType(Toggle rbAnchor) {
        if (rbAnchor == rbToUp) {
            return AnchorType.UP;
        } else if (rbAnchor == rbToDown) {
            return AnchorType.DOWN;
        } else if (rbAnchor == rbToLeft) {
            return AnchorType.LEFT;
        } else if (rbAnchor == rbToRight) {
            return AnchorType.RIGHT;
        }
        return null;
    }

    /**
     * Return node type according to selected toggle
     * @param rbType selected toggle with node type
     * @return NodeType
     */
    private NodeType getNodeType(Toggle rbType) {
        if (rbType == rbAggregation) {
            return NodeType.AGGREGATION;
        } else if (rbType == rbAssociation) {
            return NodeType.ASSOCIATION;
        } else if (rbType == rbGeneralization) {
            return NodeType.GENERALIZATION;
        } else if (rbType == rbComposition) {
            return NodeType.COMPOSITION;
        }
        return null;
    }

    /**
     * Show warning message
     * @param title window title
     * @param content warning message content
     */
    private void showWarning(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
