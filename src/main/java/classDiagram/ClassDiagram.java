package classDiagram;

import sequenceDiagram.SDObject;
import sequenceDiagram.SequenceDiagram;

import java.util.ArrayList;

/**
 * The class diagram which contains classes and nodes.
 * @author Marek Dohnal (xdohna48)
 * @since 2022-03-26
 */
public class ClassDiagram {
    private ArrayList<CDClass> classes;
    private ArrayList<CDNode> nodes;
    private ArrayList<SequenceDiagram> sequenceDiagrams;

    /**
     * Constructs an empty class diagram.
     */
    public ClassDiagram() {
        this.classes = new ArrayList<>();
        this.nodes = new ArrayList<>();
        this.sequenceDiagrams = new ArrayList<>();
    }

    /**
     * Adds a class to the class diagram.
     * @param newClass the class to be added
     * @return true if class was added, false otherwise
     */
    public boolean addClass(CDClass newClass) {
        return this.classes.add(newClass);
    }

    public void addClass(CDClass newClass, int index) {
        this.classes.add(index, newClass);
    }
    /**
     * Adds a node to the class diagram
     * @param newNode the node to be added
     * @return true if node was added, false otherwise
     */
    public boolean addNode(CDNode newNode) {
        return this.nodes.add(newNode);
    }

    public void addNode(CDNode newNode, int index) {
        this.nodes.add(index, newNode);
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
     * the parent of the latter class is set to -1.
     * Also removes any nodes coming from or to the class.
     * @param index Index of the class to be removed
     */
    public void removeClass(int index) {
        CDClass removedClass = this.classes.get(index);
        for (CDClass cdClass : this.classes) {
            if (index == cdClass.getParent()) {
                cdClass.removeParent();
            }
        }

        CDNode removedNode = null;
        for (int i = 0; i < this.nodes.size(); i++) {
            if (this.nodes.get(i).getFrom() == removedClass ||
                    this.nodes.get(i).getTo() == removedClass) {
                this.nodes.remove(i);
                i = 0;
            }
        }
        this.nodes.removeIf(e ->
                (e.getFrom() == removedClass || e.getTo() == removedClass));
        this.classes.remove(index);
    }

    public boolean removeNode(int index) {
        if (index < this.nodes.size()) {
            this.nodes.remove(index);
            return true;
        }
        return false;
    }

    /**
     * Checks if deleting a class would cause an inconsistency in the sequence diagrams.
     * @param deletedClass The class we want to delete.
     * @return TRUE if an inconsistency would be caused, false otherwise.
     */
    public boolean checkDeleteClass(CDClass deletedClass) {
        System.out.println("|" + this.getSequenceDiagrams() + "|");
        for (SequenceDiagram sd : this.getSequenceDiagrams()) {
            System.out.println("===================");
            for (SDObject obj : sd.getObjects()) {
                System.out.println(obj.getClassName());
                if (obj.getClassName().equals(deletedClass.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setSequenceDiagrams(ArrayList<SequenceDiagram> sequenceDiagrams) {
        this.sequenceDiagrams = sequenceDiagrams;
    }

    public ArrayList<SequenceDiagram> getSequenceDiagrams() {
        return sequenceDiagrams;
    }
}
