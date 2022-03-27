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
        return false;
    }

    public boolean addNode(CDNode newNode) {
        return false;
    }

    public boolean getCDClass() {
        return false;
    }

    public boolean getCDNode() {
        return false;
    }

    public boolean removeClass(int index) {
        return false;
    }

    public boolean removeNode(int index) {
        return false;
    }
}
