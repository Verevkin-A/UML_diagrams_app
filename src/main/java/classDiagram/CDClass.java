package classDiagram;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * A class of the class diagram
 * @author Marek Dohnal (xdohna48)
 * @since 2022-03-26
 */
public class CDClass {
    private String name;
    private CDClass parent;
    private ArrayList<CDField> fields;
    private ArrayList<CDField> methods;
    private boolean isInterface;

    /**
     * Constructs an empty class with a name = "defaultName",
     * the class is not an interface, and has no parent.
     */
    public CDClass() {
        this.name = "defaultName";
        this.isInterface = false;
        this.fields = new ArrayList<CDField>();
        this.methods = new ArrayList<CDField>();
        this.parent = null;
    }

    /**
     * Constructs a filled class
     * @param name the name of the class
     * @param parent the parent class (superclass)
     * @param fields the fields, which the class contains
     * @param methods the class methods
     * @param isInterface true if the class in an interface, false otherwise
     */
    public CDClass(String name, CDClass parent,
                   ArrayList<CDField> fields, ArrayList<CDField> methods, boolean isInterface) {
        this.name = name;
        this.parent = parent;
        this.isInterface = isInterface;
        this.fields = fields;
        this.methods = methods;
    }

    /**
     * Sets the name of the class
     * @param name the name of the class
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the parent class
     * @param parent the parent class
     */
    public void setParent(CDClass parent) {
        this.parent = parent;
    }

    /**
     * Sets the parent class to a class specified by an index of
     * a class in a class diagram.
     * @param cd the class diagram which contains the parent
     * @param index the index of te parent
     */
    public void setParent(ClassDiagram cd, int index) {
        try {
            this.parent = cd.getCDClass(index);
        } catch (IndexOutOfBoundsException e) {
            this.parent = null;
        }
    }

    /**
     * Removes the parent from the class.
     */
    public void removeParent() {
        this.parent = null;
    }

    /**
     * Sets the fields of the class
     * @param fields the fields of the class
     */
    public void setFields(ArrayList<CDField> fields) {
        this.fields = fields;
    }

    /**
     * Sets the methods of the class
     * @param methods the methods of the class
     */
    public void setMethods(ArrayList<CDField> methods) {
        this.methods = methods;
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
     * Sets the isInterface property
     * @param isInterface true if class is an interface, false otherwise
     */
    public void setInterface(boolean isInterface) {
        this.isInterface = isInterface;
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
    public CDClass getParent() {
        return this.parent;
    }

    /**
     * Returns the parent of the class as an index
     * of the parent in the class diagram.
     * @param cd the class diagram containing the parent
     * @return index specifying the parent, or -1 if parent couldn't be found
     */
    public int getParentAsInt(ClassDiagram cd) {
        if (this.parent != null) {
            for (int i = 0; i < cd.classesLen(); i++) {
                if (cd.getCDClass(i) == this.parent) {
                    return i;
                }
            }
            return -1;
        }
        return -1;
    }

    /**
     * @return the methods overriden in the subclass
     */
    public ArrayList<CDField> getOverridenMethods() {
        ArrayList<CDField> overridenMethods = new ArrayList<>();
        if (this.parent != null) {
            for (CDField method : this.methods) {
                if (this.parent.methods.contains(method) &&
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
}
