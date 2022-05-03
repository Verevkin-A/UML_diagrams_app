package userInterface.CDInterface;

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
    private final UIClassConnector from, to;
    private final Line node;
    private final Shape arrowHead;
    private final Label fCard, tCard, nodeNameLabel;
    private final Button btnDelete;
    private final AnchorType fAnchor, tAnchor;
    private final NodeType nodeType;

    /**
     * UIConnector constructor
     * @param from FROM class connector
     * @param to TO class connector
     * @param node Line of the node
     * @param arrowHead arrow head of the node
     * @param fCard FROM class cardinality
     * @param tCard TO class cardinality
     * @param nodeNameLabel node identification Label
     * @param btnDelete node delete Button
     * @param fAnchor node FROM anchor type
     * @param tAnchor node TP anchor type
     * @param nodeType type of the node
     */
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

    /**
     * FROM class connector getter
     * @return FROM class connector
     */
    public UIClassConnector getFrom() {
        return from;
    }

    /**
     * TO class connector getter
     * @return TO class connector
     */
    public UIClassConnector getTo() {
        return to;
    }

    /**
     * node Line getter
     * @return node line
     */
    public Line getNode() {
        return node;
    }

    /**
     * node arrow head getter
     * @return node arrow head
     */
    public Shape getArrowHead() {
        return arrowHead;
    }

    /**
     * node FROM cardinality LABEL getter
     * @return node FROM cardinality LABEL
     */
    public Label getfCard() {
        return fCard;
    }

    /**
     * node TO cardinality LABEL getter
     * @return node TO cardinality LABEL
     */
    public Label gettCard() {
        return tCard;
    }

    /**
     * node identifier LABEL getter
     * @return node identifier LABEL
     */
    public Label getNodeNameLabel() {
        return nodeNameLabel;
    }

    /**
     * node delete Button getter
     * @return node delete Button
     */
    public Button getBtnDelete() {
        return btnDelete;
    }

    /**
     * node FROM anchor getter
     * @return node FROM anchor
     */
    public AnchorType getfAnchor() {
        return fAnchor;
    }

    /**
     * node TO anchor getter
     * @return node TO anchor
     */
    public AnchorType gettAnchor() {
        return tAnchor;
    }

    /**
     * node type getter
     * @return node type
     */
    public NodeType getNodeType() {
        return nodeType;
    }
}
