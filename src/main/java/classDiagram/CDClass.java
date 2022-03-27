package classDiagram;

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
}
