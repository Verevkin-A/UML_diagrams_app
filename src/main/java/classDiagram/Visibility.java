package classDiagram;

/**
 * The visibility of a field.
 * @author Marek Dohnal (xdohna48)
 * @since 2022-03-26
 */
public enum Visibility {
    PUBLIC("+"),
    PRIVATE("-"),
    PROTECTED("#"),
    PKG_PRIVATE("~");

    private String symb;

    Visibility(String symb) {
        this.symb = symb;
    }

    public String getName() {
        switch (symb) {
            case "+":
                return "Public";
            case "-":
                return "Private";
            case "#":
                return "Protected";
            case "~":
                return "Package";
        }
        return null;
    }

    public String getSymb() {
        return symb;
    }

    /**
     * Returns the Visibility specified by it's String descriptor
     * @param symb the String descriptor
     * @return the Visibility
     */
    public static Visibility valueOfLabel(String symb) {
        for (Visibility v : values()) {
            if (v.symb.equals(symb)) {
                return v;
            }
        }
        return null;
    }
}
