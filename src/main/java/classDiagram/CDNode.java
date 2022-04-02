package classDiagram;

/**
 * A node of a class diagram
 * @author Marek Dohnal (xdohna48)
 * @since 2022-03-26
 */
public class CDNode {
    private CDClass from;
    private CDClass to;
    private String fCard;
    private String tCard;
    private NodeType type;

    /**
     * Constructs a filled node
     * @param from The class from which the node originates
     * @param to The class at which the node ends
     * @param fCard The cardinality at the originating class
     * @param tCard The cardinality at the terminating class
     * @param type The type of the node
     */
    public CDNode(CDClass from, CDClass to, String fCard,
                  String tCard, NodeType type) {
        this.from = from;
        this.to = to;
        this.fCard = fCard;
        this.tCard = tCard;
        this.type = type;
    }

    /**
     *
     * @return the class from which the node originates
     */
    public CDClass getFrom() {
        return this.from;
    }

    /**
     *
     * @return the class at which the node ends
     */
    public CDClass getTo() {
        return this.to;
    }

    /**
     * Returns the originating class as an index in a class diagram,
     * which contains such class
     * @param cd the class diagram containing the class
     * @return the index of the class, or -1 if the class was not found
     */
    public int getFromAsInt(ClassDiagram cd) {
        for (int i = 0; i < cd.classesLen(); i++) {
            if (cd.getCDClass(i) == this.from) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns the terminating class as an index in a class diagram,
     * which contains such class
     * @param cd the class diagram containing the class
     * @return the index of the class, or -1 if the class was not found
     */
    public int getToAsInt(ClassDiagram cd) {
        for (int i = 0; i < cd.classesLen(); i++) {
            if (cd.getCDClass(i) == this.to) {
                return i;
            }
        }
        return -1;
    }

    /**
     *
     * @return the cardinality at the originating class
     */
    public String getfCard() {
        return this.fCard;
    }

    /**
     *
     * @return the cardinality at the terminating class
     */
    public String gettCard() {
        return this.tCard;
    }

    /**
     *
     * @return the type of the node as an integer
     */
    public int getType() {
        return this.type.getNumVal();
    }
}
