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
    private boolean markedInconsistent;

    public SDMessage(String name, int fromIdx, int toIdx, SequenceDiagram sd, MessageType type, int timePos) {
        this.name = name;
        setFrom(fromIdx, sd);
        setTo(toIdx, sd);
        this.type = type;
        this.timePos = timePos;
    }

    public void setFrom(int fromIdx, SequenceDiagram sd) {
        this.from = sd.getObjects().get(fromIdx);
    }

    public void setTo(int toIdx, SequenceDiagram sd) {
        this.to = sd.getObjects().get(toIdx);
    }

    public void setMarkedInconsistent(ClassDiagram cd) {
        markedInconsistent = !checkFromAndTo(cd);
    }

    public void setMarkedInconsistent(boolean markedInconsistent) {
        this.markedInconsistent = markedInconsistent;
    }

    public boolean checkFromAndTo(ClassDiagram cd) {
        for (int i = 0; i < cd.nodesLen(); i++) {
            if ((this.from.getClassName().equals(cd.getCDNode(i).getFrom().getName()) ||
                this.from.getClassName().equals(cd.getCDNode(i).getTo().getName()))
                    &&
                (this.to.getClassName().equals(cd.getCDNode(i).getTo().getName()) ||
                this.to.getClassName().equals(cd.getCDNode(i).getTo().getName()))) {
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
}
