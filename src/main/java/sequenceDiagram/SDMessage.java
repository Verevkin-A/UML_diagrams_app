package sequenceDiagram;

import classDiagram.CDField;
import classDiagram.ClassDiagram;
import classDiagram.NodeType;

import java.util.ListIterator;

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
    /**
     * Is set to TRUE if a message is inconsistent with the class diagram on load.
     * The GUI should mark the message accordingly (change colour for example) if
     * the message is inconsistent.
     */
    private boolean isInconsistentOnLoad;

    public SDMessage(String name, int fromIdx, int toIdx, SequenceDiagram sd, MessageType type, int timePos) {
        this.name = name;
        setFrom(fromIdx, sd);
        setTo(toIdx, sd);
        this.type = type;
        this.timePos = timePos;
        isInconsistentOnLoad = false;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFrom(int fromIdx, SequenceDiagram sd) {
        this.from = sd.getObjects().get(fromIdx);
    }

    public void setTo(int toIdx, SequenceDiagram sd) {
        this.to = sd.getObjects().get(toIdx);
    }

    /**
     * Should only be used by the parser.
     * @param cd The class diagram against which to check inconsistencies
     */
    public void setInconsistentOnLoad(ClassDiagram cd) {
        isInconsistentOnLoad = !checkConsistency(cd, this.to, this.name);
    }

    /**
     * Should only be used by the parser.
     * @param isInconsistentOnLoad TRUE if msg is incosistent, false otherwise
     */
    public void setInconsistentOnLoad(boolean isInconsistentOnLoad) {
        this.isInconsistentOnLoad = isInconsistentOnLoad;
    }

    /**
     * Checks the end object of a message, as well as the message name, for consistency.
     *
     * @param cd The class diagram against which to check.
     * @return True if the message is consistent, false otherwise.
     */
    public static boolean checkConsistency(ClassDiagram cd, SDObject to, String name) {
        for (int i = 0; i < cd.classesLen(); i++) {
            if (cd.getCDClass(i).getName().equals(to.getClassName())) {
                for (CDField method : cd.getCDClass(i).getMethods()) {
                    if (method.getName().equals(name)) {
                        return true;
                    }
                }
                for (CDField method : cd.getCDClass(i).getSuperclassMethods(cd)) {
                    if (method.getName().equals(name)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean checkTimePos() {
        return timePos < 100 && timePos > 0;
    }

    public int getTimePos() {
        return timePos;
    }

    public SDObject getFrom() {
        return from;
    }

    public SDObject getTo() {
        return to;
    }

    public int getFrom(SequenceDiagram sd) {
        return sd.getObjects().indexOf(this.from);
    }

    public int getTo(SequenceDiagram sd) {
        return sd.getObjects().indexOf(this.to);
    }

    public MessageType getType() {
        return type;
    }

    public String getTypeAsString() {
        return type.getSymb();
    }

    public String getName() {
        return name;
    }

    public boolean isInconsistentOnLoad() {
        return isInconsistentOnLoad;
    }
}
