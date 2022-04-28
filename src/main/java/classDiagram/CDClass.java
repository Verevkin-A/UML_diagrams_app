package classDiagram;

import sequenceDiagram.SDObject;
import sequenceDiagram.SequenceDiagram;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * A class of the class diagram
 * @author Marek Dohnal (xdohna48)
 * @since 2022-03-26
 */
public class CDClass {
    private String name;
    private int parent;
    private ArrayList<CDField> fields;
    private ArrayList<CDField> methods;
    private boolean isInterface;
    private Point position;

    /**
     * Constructs an empty class with a name = "defaultName",
     * the class is not an interface, and has no parent.
     * Class has position (0,0), width = 10, height = 10.
     */
    public CDClass() {
        this.name = "defaultName";
        this.isInterface = false;
        this.fields = new ArrayList<>();
        this.methods = new ArrayList<>();
        this.parent = -1;
        this.position = new Point(0,0);
    }

    /**
     * Constructs a filled class, performs a check against sequence diagrams.
     * @param name the name of the class
     * @param parent the index od the parent class (superclass) in a class diagram
     * @param fields the fields, which the class contains
     * @param methods the class methods
     * @param isInterface true if the class in an interface, false otherwise
     * @param x the x coordinate of the upper-left corner
     * @param y the y coordinate of the upper-left corner
     */
    public CDClass(String name, int parent,
                   ArrayList<CDField> fields, ArrayList<CDField> methods, boolean isInterface,
                   int x, int y) {
        this.name = name;
        this.parent = parent;
        this.isInterface = isInterface;
        this.fields = fields;
        this.methods = methods;
        this.position = new Point(x, y);
    }

    /**
     * Sets the name of the class, updates the name in sequence diagrams as well.
     * @param name the name of the class
     */
    public void setName(ClassDiagram cd, String name) {
        for (SequenceDiagram sd : cd.getSequenceDiagrams()) {
            for (SDObject obj : sd.getObjects()) {
                if (obj.getClassName().equals(this.name)) {
                    obj.setClassName(name);
                }
            }
        }
        this.name = name;

    }

    /**
     * Checks whether changing a class to a different name causes an inconsistency in the sequence diagram.
     * @param cd The class diagram containing a sequence diagram against which to check inconsistencies.
     * @param name The name of the class you want to set.
     * @return TRUE if an inconsistency is not caused, FALSE otherwise.
     */
    private boolean checkName(ClassDiagram cd, String name) {
        for (SequenceDiagram sd : cd.getSequenceDiagrams()) {
            for (SDObject obj : sd.getObjects()) {
                if (obj.getClassName().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Sets the index of a parent class in a class diagram
     * @param parentIdx the index of the parent
     */
    public void setParent(int parentIdx) {
        this.parent = parentIdx;
    }

    /**
     * Sets the parent class to an index specified by a class in a class diagram.
     * @param cd the class diagram which contains the parent
     * @param parent the parent class
     */
    public void setParent(ClassDiagram cd, CDClass parent) {
        for (int i = 0; i < cd.classesLen(); i++) {
            if (parent == cd.getCDClass(i)) {
                this.parent = i;
                return;
            }
        }
        this.parent = -1;

    }

    /**
     * Removes the parent from the class (sets the index to -1).
     */
    public void removeParent() {
        this.parent = -1;
    }

    /**
     * Sets the fields of the class
     * @param fields the fields of the class
     */
    public void setFields(CDField[] fields) {
        this.fields = new ArrayList<>(Arrays.asList(fields));
    }

    /**
     * Sets the methods of the class
     * @param methods the methods of the class
     */
    public void setMethods(CDField[] methods) {
        this.methods = new ArrayList<>(Arrays.asList(methods));
    }

    /**
     * Adds a single field to the class
     * @param field the field to be added
     * @return true if field was added, false otherwise
     */
    public boolean addField(CDField field) {
        return this.fields.add(field);
    }

    /**
     * Adds a single method to the class
     * @param method the method to be added
     * @return true if method was added, false otherwise
     */
    public boolean addMethod(CDField method) {
        return this.methods.add(method);
    }

    /**
     * Sets the isInterface property.
     * @param isInterface true if class is an interface, false otherwise
     */
    public void setInterface(boolean isInterface) {
        this.isInterface = isInterface;
    }

    /**
     * Check if setting a class to the interface specified by the parameter would cause an inconsistency
     * in a sequential diagram.
     * Should be called before setting the interface.
     * @param cd The class diagram against which to check inconsistencies.
     * @param isInterface The interface value we want to use in setInterface or the CDClass constructor.
     * @return TRUE if the change is consistent, FALSE otherwise
     */
    public boolean checkInterface(ClassDiagram cd, boolean isInterface) {
        for (SequenceDiagram sd : cd.getSequenceDiagrams()) {
            for (SDObject obj : sd.getObjects()) {
                if (obj.getClassName().equals(this.name)) {
                    return !isInterface;
                }
            }
        }
        return true;
    }

    /**
     * Sets the position to x and y coordinates.
     * @param x the x coordinate of the upper-left corner
     * @param y the y coordinate of the upper-left corner
     */
    public void setPosition(int x, int y) {
        this.position = new Point(x, y);
    }

    /**
     *
     * @return the name of the class
     */
    public String getName() {
        return this.name;
    }

    /**
     *
     * @return the parent of the class
     */
    public int getParent() {
        return this.parent;
    }

    /**
     * Returns the parent of the class as an index
     * of the parent in the class diagram.
     * @param cd the class diagram containing the parent
     * @return index specifying the parent, or -1 if parent couldn't be found
     */
    public CDClass getParentAsCDClass(ClassDiagram cd) {
        if (this.parent != -1) {
            return cd.getCDClass(this.parent);
        }
        return null;
    }

    /**
     * @return the methods overriden in the subclass
     */
    public ArrayList<CDField> getOverridenMethods(ClassDiagram cd) {
        ArrayList<CDField> overridenMethods = new ArrayList<>();
        if (this.parent != -1) {
            CDClass parentClass = cd.getCDClass(this.parent);
            for (CDField method : this.methods) {
                if (parentClass.methods.contains(method) &&
                        method.getVisibility() != Visibility.PRIVATE) {
                    overridenMethods.add(method);
                }
            }
            return overridenMethods;
        }
        return null;
    }

    /**
     *
     * @return the fields of the class
     */
    public ArrayList<CDField> getFields() {
        return this.fields;
    }

    /**
     *
     * @return the methods of the class
     */
    public ArrayList<CDField> getMethods() {
        return this.methods;
    }

    public Point getPosition() {
        return this.position;
    }

    public int getXposition() {
        return this.position.x;
    }

    public int getYposition() {
        return this.position.y;
    }

    public boolean getInterface() {
        return this.isInterface;
    }
}
