package classDiagram;

/**
 * A type of an anchor.
 * @author Marek Dohnal (xdohna48)
 * @since 2022-03-26
 */
public enum AnchorType {
    UP("UP"),
    DOWN("DOWN"),
    LEFT("LEFT"),
    RIGHT("RIGHT");

    private String symb;

    AnchorType(String symb) {
        this.symb = symb;
    }

    public String getSymb() {
        return symb;
    }

    /**
     * Returns the AnchorType specified by it's String descriptor
     * @param symb the String descriptor
     * @return the AnchorType
     */
    public static AnchorType valueOfLabel(String symb) {
        for (AnchorType a : values()) {
            if (a.symb.equals(symb)) {
                return a;
            }
        }
        return null;
    }
}