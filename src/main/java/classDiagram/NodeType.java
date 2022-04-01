package classDiagram;

public enum NodeType {
    AGGREGATION(0),
    ASSOCIATION(1),
    GENERALIZATION(2),
    COMPOSITION(3);

    private int numVal;

    NodeType(int numVal) {
        this.numVal = numVal;
    }

    public int getNumVal() {
        return numVal;
    }

    public static NodeType valueOfLabel(int numVal) {
        for (NodeType t : values()) {
            if (t.numVal == numVal) {
                return t;
            }
        }
        return null;
    }
}
