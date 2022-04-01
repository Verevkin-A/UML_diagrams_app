package classDiagram;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * @author Marek Dohnal (xdohna48)
 * @since 2022-03-26
 */
public class CDClass {
    private String name;
    private CDClass parent;
    private ArrayList<CDField> fields;
    private ArrayList<CDField> methods;
    private boolean isInterface;

    public CDClass() {
        this.name = "defaultName";
        this.isInterface = false;
        this.fields = new ArrayList<CDField>();
        this.methods = new ArrayList<CDField>();
        this.parent = null;
    }

    public CDClass(String name, CDClass parent,
                   ArrayList<CDField> fields, ArrayList<CDField> methods, boolean isInterface) {
        this.name = name;
        this.parent = parent;
        this.isInterface = isInterface;
        this.fields = fields;
        this.methods = methods;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParent(CDClass parent) {
        this.parent = parent;
    }

    public void setParent(ClassDiagram cd, int index) {
        try {
            this.parent = cd.getCDClass(index);
        } catch (IndexOutOfBoundsException e) {
            this.parent = null;
        }
    }

    public void removeParent() {
        this.parent = null;
    }

    public void setFields(ArrayList<CDField> fields) {
        this.fields = fields;
    }

    public void setMethods(ArrayList<CDField> methods) {
        this.methods = methods;
    }

    public boolean addField(CDField field) {
        return this.fields.add(field);
    }

    public boolean addMethod(CDField method) {
        return this.methods.add(method);
    }

    public void setInterface(boolean isInterface) {
        this.isInterface = isInterface;
    }

    public String getName() {
        return this.name;
    }

    public CDClass getParent() {
        return this.parent;
    }

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

    public ArrayList<CDField> getFields() {
        return this.fields;
    }
    public ArrayList<CDField> getMethods() {
        return this.methods;
    }
}
