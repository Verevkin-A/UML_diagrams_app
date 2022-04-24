package userInterface;

import classDiagram.AnchorType;
import classDiagram.NodeType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

/**
 * Connection between all single nodes objects on the pane
 * @author Aleksandr Verevkin (xverev00)
 * @since 2022-04-12
 */
public class UINodeConnector {
    private UIClassConnector from, to;
    private Line node;
    private Shape arrowHead;
    private Label fCard, tCard, nodeNameLabel;
    private Button btnDelete;
    private AnchorType fAnchor, tAnchor;
    private NodeType nodeType;

    public UINodeConnector(UIClassConnector from, UIClassConnector to, Line node, Shape arrowHead,
                           Label fCard, Label tCard, Label nodeNameLabel, Button btnDelete,
                           AnchorType fAnchor, AnchorType tAnchor, NodeType nodeType) {
        this.from = from;
        this.to = to;
        this.node = node;
        this.arrowHead = arrowHead;
        this.fCard = fCard;
        this.tCard = tCard;
        this.nodeNameLabel = nodeNameLabel;
        this.btnDelete = btnDelete;
        this.fAnchor = fAnchor;
        this.tAnchor = tAnchor;
        this.nodeType = nodeType;
    }

    public UIClassConnector getFrom() {
        return from;
    }

    public UIClassConnector getTo() {
        return to;
    }

    public Line getNode() {
        return node;
    }

    public Shape getArrowHead() {
        return arrowHead;
    }

    public Label getfCard() {
        return fCard;
    }

    public Label gettCard() {
        return tCard;
    }

    public Label getNodeNameLabel() {
        return nodeNameLabel;
    }

    public Button getBtnDelete() {
        return btnDelete;
    }

    public AnchorType getfAnchor() {
        return fAnchor;
    }

    public AnchorType gettAnchor() {
        return tAnchor;
    }

    public NodeType getNodeType() {
        return nodeType;
    }
}
