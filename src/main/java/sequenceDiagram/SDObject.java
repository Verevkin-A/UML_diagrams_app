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
    /**
     * When the classes of objects are inconsistent directly after being loaded
     * from a file, they need to be differentiated (in GUI they need to change colour for example)
     * TRUE if className is inconsistent, FALSE otherwise.
     */
    private boolean isInconsistentOnLoad;
    /**
     * timePos is -1 when the object was not created by a message.
     * if object is at timePos position it can be activated at position timePos + 1, no sooner than that.
     */
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
        this.isInconsistentOnLoad = false;
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
        this.isInconsistentOnLoad = false;
    }

    /**
     * Should only be called by the PARSER!
     * Performs a consistency check on a user entered class name, also checks
     * if a user wants to instantiate an interface, this method should be used in GUI
     * @param cd a class diagram containing a CDClass which should correspond to className
     * @return true if cd contains a CDClass with a name, that equals className AND is NOT an interface,
     * false otherwise.
     */
    public boolean checkClsNameAndInt(ClassDiagram cd) {
        for (int i = 0; i < cd.classesLen(); i++) {
            // Interface cannot be instantiated
            if (cd.getCDClass(i).getName().equals(this.className) && !cd.getCDClass(i).getInterface()) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method should be used in parser only. Sets the inconsistency flag on load.
     * @param cd A class diagram against which to check consistency.
     *
     */
    public void setInconsistentOnLoad(ClassDiagram cd) {
        this.isInconsistentOnLoad = !checkClsNameAndInt(cd);
    }

    /**
     * This method should also be used only in the parser.
     * @param isInconsistentOnLoad TRUE if the class is inconsistent on load, FALSE otherwise.
     */
    public void setInconsistentOnLoad(boolean isInconsistentOnLoad) {
        this.isInconsistentOnLoad = isInconsistentOnLoad;
    }

    public void setActivations(ArrayList<SDActivation> activations) {
        this.activations = activations;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    /**
     *
     * @param className the name of the class to be set
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * Checks if setting an object to a class name specified by the parameter causes an inconsistency.
     * @param cd The class diagram against which to check the inconsistency
     * @param className The class name we wish to set the SDObject to.
     */
    public boolean checkClassName(ClassDiagram cd, String className) {
        for (int i = 0; i < cd.classesLen(); i++) {
            if (cd.getCDClass(i).getName().equals(className)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<SDActivation> getActivations() {
        return activations;
    }

    /**
     * Checks whether the activation we wish to add to the object is within the objects lifeline.
     * For example, we cannot add an activation from time 0 to time 50, when the object's lifeline
     * starts at timePos = 30.
     * @param activation The activation we want to check
     * @return True if the activation is within the bounds of the object's lifeline, false otherwise.
     */
    public boolean checkActivation(SDActivation activation) {
        return activation.getTimeBegin() >= this.timePos && activation.getTimeEnd() <= 100;
    }

    public void setTimePos(int timePos) {
        this.timePos = timePos;
    }

    public boolean isInconsistentOnLoad() {
        return isInconsistentOnLoad;
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
