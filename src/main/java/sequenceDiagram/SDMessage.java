package sequenceDiagram;

/**
 * Represents a message in a sequence diagram.
 * @author Marek Dohnal
 * @since 25/04/2022
 */
public class SDMessage {
    private String name;
    private SDObject from;
    private SDObject to;
    private MessageType type;
    private int timePos;

}
