package classDiagram;

import java.util.ArrayList;

/**
 * @author Marek Dohnal (xdohna48)
 * @since 2022-03-26
 */
public class ClassDiagram {
    private ArrayList<CDClass> classes;
    private ArrayList<CDNode> nodes;


    public ClassDiagram() {
        this.classes = new ArrayList<>();
        this.nodes = new ArrayList<>();
    }

    //TODO: Implement methods
    public boolean addClass(CDClass newClass) {
        return this.classes.add(newClass);
    }

    public boolean addNode(CDNode newNode) {
        return this.nodes.add(newNode);
    }

    public CDClass getCDClass(int index) {
        return this.classes.get(index);
    }

    public CDNode getCDNode(int index) {
        return this.nodes.get(index);
    }

    public int classesLen() {
        return this.classes.size();
    }

    public int nodesLen() {
        return this.nodes.size();
    }


    public boolean removeClass(int index) {
        if (index < this.classes.size()) {
            for (CDClass cdClass : this.classes) {
                if (this.classes.get(index).getParent() == cdClass) {
                    this.classes.get(index).removeParent();
                }
            }
            for (CDNode cdNode : this.nodes) {
                if (cdNode.getFrom() == this.classes.get(index) ||
                    cdNode.getTo() == this.classes.get(index)) {
                    this.nodes.remove(cdNode);
                }
            }
            this.classes.remove(index);
        }
        return false;
    }

    public boolean removeNode(int index) {
        if (index < this.nodes.size()) {
            this.nodes.remove(index);
        }
        return false;
    }

}
