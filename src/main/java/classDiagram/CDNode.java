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

    public int getFromAsInt(ClassDiagram cd) {
        for (int i = 0; i < cd.classesLen(); i++) {
            if (cd.getCDClass(i) == this.from) {
                return i;
            }
        }
        return -1;
    }

    public int getToAsInt(ClassDiagram cd) {
        for (int i = 0; i < cd.classesLen(); i++) {
            if (cd.getCDClass(i) == this.to) {
                return i;
            }
        }
        return -1;
    }

    public String getfCard() {
        return this.fCard;
    }

    public String gettCard() {
        return this.tCard;
    }

    public int getType() {
        return this.type.getNumVal();
    }
}
