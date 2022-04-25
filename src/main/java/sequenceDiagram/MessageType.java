package sequenceDiagram;

/**
 * A type of a message.
 * @author Marek Dohnal (xdohna48)
 * @since 25/04/2022
 */
public enum MessageType {
    NORMAL("NORMAL"),
    CALLBACK("CALLBACK"),
    CREATE_OBJ("CREATE_OBJ"),
    DESTROY_OBJ("DESTROY_OBJ");

    private String symb;

    MessageType(String symb) {
        this.symb = symb;
    }

    public String getSymb() {
        return symb;
    }

    /**
     * Returns the MessageType specified by it's String descriptor
     * @param symb the String descriptor
     * @return the MessageType
     */
    public static MessageType valueOfLabel(String symb) {
        for (MessageType m : values()) {
            if (m.symb.equals(symb)) {
                return m;
            }
        }
        return null;
    }
}
