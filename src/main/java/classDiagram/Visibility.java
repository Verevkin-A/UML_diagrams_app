package classDiagram;

public enum Visibility {
    PUBLIC("+"),
    PRIVATE("-"),
    PROTECTED("#"),
    PKG_PRIVATE("~");

    private String symb;

    Visibility(String symb) {
        this.symb = symb;
    }

    public String getSymb() {
        return symb;
    }

    public static Visibility valueOfLabel(String symb) {
        for (Visibility v : values()) {
            if (v.symb.equals(symb)) {
                return v;
            }
        }
        return null;
    }
}
