package classDiagram;

import java.util.ArrayList;

/**
 * The class diagram which contains classes and nodes.
 * @author Marek Dohnal (xdohna48)
 * @since 2022-03-26
 */
public class ClassDiagram {
    private ArrayList<CDClass> classes;
    private ArrayList<CDNode> nodes;

    /**
     * Constructs an empty class diagram.
     */
    public ClassDiagram() {
        this.classes = new ArrayList<>();
        this.nodes = new ArrayList<>();
    }

    /**
     * Adds a class to the class diagram.
     * @param newClass the class to be added
     * @return true if class was added, false otherwise
     */
    public boolean addClass(CDClass newClass) {
        return this.classes.add(newClass);
    }

    /**
     * Adds a node to the class diagram
     * @param newNode the node to be added
     * @return true if node was added, false otherwise
     */
    public boolean addNode(CDNode newNode) {
        return this.nodes.add(newNode);
    }

    /**
     * Returns a class of the class diagram on the specified index
     * @param index position of the class in the class diagram
     * @return the seeked CDClass
     */
    public CDClass getCDClass(int index) {
        return this.classes.get(index);
    }

    /**
     * Returns a node of the class diagram on the specified index
     * @param index position of the node in the class diagram
     * @return the seeked CDNode
     */
    public CDNode getCDNode(int index) {
        return this.nodes.get(index);
    }

    /**
     *
     * @return number of classes contained in the class diagram
     */
    public int classesLen() {
        return this.classes.size();
    }

    /**
     *
     * @return number of nodes in the class diagram
     */
    public int nodesLen() {
        return this.nodes.size();
    }


    /**
     * Removes a class from the class diagram.
     * If the class was a parent to another class,
     * the parent of the latter class is set to null.
     * Also removes any nodes coming from or to the class.
     * @param index Index of the class to be removed
     * @return true if removal was successful, false otherwise
     */
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
