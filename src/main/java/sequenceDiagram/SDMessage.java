package sequenceDiagram;

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
        isInconsistentOnLoad = !checkFromAndTo(cd, this.from, this.to);
    }

    /**
     * Should only be used by the parser.
     * @param isInconsistentOnLoad TRUE if msg is incosistent, false otherwise
     */
    public void setInconsistentOnLoad(boolean isInconsistentOnLoad) {
        this.isInconsistentOnLoad = isInconsistentOnLoad;
    }

    /**
     * Checks the start and end objects of a message.
     * If the objects are instances of classes with no association, aggregation or composition,
     * they are not consistent.
     * @param cd The class diagram against which to check.
     * @return True if the message is consistent, false otherwise.
     */
    public boolean checkFromAndTo(ClassDiagram cd, SDObject from, SDObject to) {
        for (int i = 0; i < cd.nodesLen(); i++) {
            if ((this.from.getClassName().equals(cd.getCDNode(i).getFrom().getName()) &&
                this.to.getClassName().equals(cd.getCDNode(i).getTo().getName()))
                    ||
                (this.from.getClassName().equals(cd.getCDNode(i).getTo().getName()) &&
                this.to.getClassName().equals(cd.getCDNode(i).getFrom().getName()))) {
                if (cd.getCDNode(i).getType() != NodeType.GENERALIZATION.getNumVal()) {
                    return true;
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
