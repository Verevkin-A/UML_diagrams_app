package classDiagram;

/**
 * A type of a node.
 * @author Marek Dohnal (xdohna48)
 * @since 2022-03-26
 */
public enum NodeType {
    AGGREGATION(0),
    ASSOCIATION(1),
    GENERALIZATION(2),
    COMPOSITION(3);

    private int numVal;

    NodeType(int numVal) {
        this.numVal = numVal;
    }

    /**
     *
     * @return the integer value of the NodeType object
     */
    public int getNumVal() {
        return numVal;
    }

    /**
     * Returns a NodeType object specified by its integer value.
     * @param numVal the value of the NodeType
     * @return the NodeType specified
     */
    public static NodeType valueOfLabel(int numVal) {
        for (NodeType t : values()) {
            if (t.numVal == numVal) {
                return t;
            }
        }
        return null;
    }
}
