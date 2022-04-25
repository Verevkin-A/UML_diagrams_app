package sequenceDiagram;

import classDiagram.ClassDiagram;

import java.util.ArrayList;

/**
 * A class representing an object in a sequence diagram
 * @author Marek Dohnal
 * @since 25/04/2022
 */
public class SDObject {
    private String objName;
    private String className;
    private ArrayList<SDActivation> activations;
    /// When the classes of objects are inconsistent directly after being loaded
    /// from a file, they need to be differentiated (in GUI they need to change colour for example)
    /// TRUE if className is inconsistent, FALSE otherwise.
    private boolean inconsistentFromLoad;
    /// timePos is -1 when the object was not created by a message.
    private int timePos;

    /**
     * Creates a new object at the top, above the timeline.
     * No consistency checks are performed.
     * @param objName the name of the object.
     * @param className the name of the class
     */
    public SDObject(String objName, String className) {
        this.objName = objName;
        this.className = className;
        activations = new ArrayList<>();
        timePos = -1;
        this.inconsistentFromLoad = false;
    }

    /**
     * Creates a new object as specified by a create object message.
     *
     * @param objName the name of the object
     * @param className the name of the class
     * @param timePos the at which the object is created
     */
    public SDObject(String objName, String className, int timePos) {
        this.objName = objName;
        this.className = className;
        activations = new ArrayList<>();
        this.timePos = timePos;
        this.inconsistentFromLoad = false;
    }

    /**
     * Performs a consistency check on a user entered class name.
     * @param className the class name a user enters
     * @param cd a class diagram containing a CDClass which should correspond to className
     * @return true if cd contains a CDClass with a name, that equals className, false otherwise.
     */
    public boolean checkClassName(String className, ClassDiagram cd) {
        for (int i = 0; i < cd.classesLen(); i++) {
            if (cd.getCDClass(i).getName().equals(className)) {
                return true;
            }
        }
        return false;
    }

    public void setInconsistentFromLoad(boolean inconsistentFromLoad) {
        this.inconsistentFromLoad = inconsistentFromLoad;
    }

    public void setActivations(ArrayList<SDActivation> activations) {
        this.activations = activations;
    }

    public void addActivation(SDActivation activation) {
        this.activations.add(activation);
    }

    public void addActivation(int idx, SDActivation activation) {
        this.activations.add(idx, activation);
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    /**
     * When a className is set by the user, the inconsistency from load is set to false.
     * @param className the name of the class to be set
     */
    public void setClassName(String className) {
        this.className = className;
        this.inconsistentFromLoad = false;
    }

    public ArrayList<SDActivation> getActivations() {
        return activations;
    }

    public void setTimePos(int timePos) {
        this.timePos = timePos;
    }

    public boolean isInconsistentFromLoad() {
        return inconsistentFromLoad;
    }

    public String getObjName() {
        return objName;
    }

    public String getClassName() {
        return className;
    }

    public int getTimePos() {
        return timePos;
    }
}
