package sequenceDiagram;

import classDiagram.ClassDiagram;
import classDiagram.NodeType;

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
    private boolean inconsistentFromLoad;

    public SDMessage(String name, SDObject from, SDObject to, MessageType type, int timePos) {
        this.name = name;
        this.from = from;
        this.to = to;
        this.type = type;
        this.timePos = timePos;
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
}
