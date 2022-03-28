package classDiagram;

/**
 * @author Marek Dohnal (xdohna48)
 * @since 2022-03-26
 */
public class CDNode {
    private CDClass from;
    private CDClass to;
    private String fCard;
    private String tCard;
    private NodeType type;

    public CDNode(CDClass from, CDClass to, String fCard,
                  String tCard, NodeType type) {
        this.from = from;
        this.to = to;
        this.fCard = fCard;
        this.tCard = tCard;
        this.type = type;
    }
}
