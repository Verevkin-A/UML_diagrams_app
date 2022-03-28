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
        this.classes = new ArrayList<CDClass>();
        this.nodes = new ArrayList<CDNode>();
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
        return false;
    }

    public boolean removeNode(int index) {
        return false;
    }

}
