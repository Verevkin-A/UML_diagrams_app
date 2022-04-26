package parser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import classDiagram.*;
import org.json.*;
import sequenceDiagram.*;

/**
 * Encodes and decodes a JSON file with a class diagram and a sequence diagram.
 * @author Marek Dohnal (xdohna48)
 * @since 2022-03-26
 */
public class Parser {
    /**
     * Decodes a JSON string containing the class diagram
     * into a ClassDiagram object.
     * @param diagString The string containing a JSON class diagram
     * @return a filled ClassDiagram object
     */
    public static ClassDiagram decodeJSONclassDiag(String diagString) {
        ClassDiagram classDiagram = new ClassDiagram();

        JSONObject obj = new JSONObject(diagString);
        JSONObject cdJSON = obj.getJSONObject("classDiagram");
        JSONArray classesJSON = cdJSON.getJSONArray("classes");

        for (int i = 0; i < classesJSON.length(); i++) {
            JSONObject classJSON = classesJSON.getJSONObject(i);
            CDClass newClass = new CDClass();
            JSONArray fieldsJSON = classJSON.getJSONArray("fields");
            JSONArray methodsJSON = classJSON.getJSONArray("methods");

            for (int j = 0; j < fieldsJSON.length(); j++) {
                JSONObject fieldJSON = fieldsJSON.getJSONObject(j);
                CDField field = new CDField(
                        fieldJSON.getString("name"),
                        Visibility.valueOfLabel(fieldJSON.getString("visibility"))
                );
                newClass.addField(field);
            }

            for (int j = 0; j < methodsJSON.length(); j++) {
                JSONObject methodJSON = methodsJSON.getJSONObject(j);
                CDField method = new CDField(
                        methodJSON.getString("name"),
                        Visibility.valueOfLabel(methodJSON.getString("visibility"))
                );
                newClass.addMethod(method);
            }


            newClass.setName(classJSON.getString("name"));
            newClass.setParent(classJSON.getInt("parent"));
            newClass.setInterface(classJSON.getBoolean("isInterface"));
            newClass.setPosition(classJSON.getInt("xPos"), classJSON.getInt("yPos"));
            newClass.setWidth(classJSON.getInt("width"));
            newClass.setHeight(classJSON.getInt("height"));

            classDiagram.addClass(newClass);
        }

        JSONArray nodesJSON = cdJSON.getJSONArray("nodes");

        for (int i = 0; i < nodesJSON.length(); i++) {
            JSONObject node = nodesJSON.getJSONObject(i);
            CDNode newNode = new CDNode(
                    classDiagram.getCDClass(node.getInt("from")),
                    AnchorType.valueOfLabel(node.getString("fAnchor")),
                    classDiagram.getCDClass(node.getInt("to")),
                    AnchorType.valueOfLabel(node.getString("tAnchor")),
                    node.getString("fCard"),
                    node.getString("tCard"),
                    NodeType.valueOfLabel(node.getInt("type"))
            );
            classDiagram.addNode(newNode);
        }

        return classDiagram;
    }

    /**
     * Decodes a sequence diagram from a JSON file.
     * @param diagString The input JSON file
     * @param cd The class diagram against which inconsistencies are checked
     * @return An ArrayList of sequence diagrams
     */
    public static ArrayList<SequenceDiagram> decodeJSONseqDiag(String diagString, ClassDiagram cd) {
        ArrayList<SequenceDiagram> sequenceDiagrams = new ArrayList<>();

        JSONObject obj = new JSONObject(diagString);
        JSONArray seqDiagsJSON = obj.getJSONArray("sequenceDiagrams");

        // Diagrams
        for (int i = 0; i < seqDiagsJSON.length(); i++) {
            JSONObject seqDiagJSON = seqDiagsJSON.getJSONObject(i);
            SequenceDiagram seqDiag = new SequenceDiagram();

            // Objects
            JSONArray objectsJSON = seqDiagJSON.getJSONArray("objects");
            for (int j = 0; j < objectsJSON.length(); j++) {
                JSONObject objectJSON = objectsJSON.getJSONObject(j);
                SDObject object = new SDObject(
                        objectJSON.getString("objName"),
                        objectJSON.getString("className"),
                        objectJSON.getInt("timePos")
                );

                // Activations
                JSONArray activationsJSON = objectJSON.getJSONArray("activations");
                ArrayList<SDActivation> activations = new ArrayList<>();
                for (int k = 0; k < activationsJSON.length(); k++) {
                    JSONObject activationJSON = activationsJSON.getJSONObject(k);
                    SDActivation activation = new SDActivation(
                            activationJSON.getInt("timeBegin"),
                            activationJSON.getInt("timeEnd")
                    );
                    activations.add(activation);
                }
                object.setActivations(activations);

                // Class name inconsistency
                object.setInconsistentClassOnLoad(cd);

                // Add object
                seqDiag.getObjects().add(object);
            }

            // Messages
            JSONArray msgsJSON = seqDiagJSON.getJSONArray("messages");
            for (int j = 0; j < msgsJSON.length(); j++) {
                JSONObject msgJSON = msgsJSON.getJSONObject(i);
                SDMessage msg = new SDMessage(
                        msgJSON.getString("name"),
                        msgJSON.getInt("from"),
                        msgJSON.getInt("to"),
                        seqDiag,
                        MessageType.valueOfLabel(msgJSON.getString("type")),
                        msgJSON.getInt("timePos")
                );

                // Inconsistency with nodes.
                msg.setInconsistentFromLoad(cd);
                seqDiag.getMessages().add(msg);
            }
            sequenceDiagrams.add(seqDiag);
        }

        return sequenceDiagrams;
    }

    /**
     * Encodes a ClassDiagram object into a JSONObject.
     * @param cd The ClassDiagram to be encoded
     * @return The encoded JSONObject
     */
    private static JSONObject encodeJSONclassDiag(ClassDiagram cd) {
        JSONObject classDiagram = new JSONObject();
        JSONArray classes = new JSONArray();
        JSONArray nodes = new JSONArray();
        classDiagram.put("classes", classes);
        classDiagram.put("nodes", nodes);

        for (int i = 0; i < cd.classesLen(); i++) {
            CDClass cdClass = cd.getCDClass(i);
            JSONObject classJSON = new JSONObject();
            classJSON.put("name", cdClass.getName());
            classJSON.put("parent", cdClass.getParent());
            classJSON.put("isInterface", cdClass.getInterface());
            classJSON.put("xPos", cdClass.getXposition());
            classJSON.put("yPos", cdClass.getYposition());
            classJSON.put("width", cdClass.getWidth());
            classJSON.put("height", cdClass.getHeight());

            JSONArray fields = new JSONArray();
            classJSON.put("fields", fields);

            ArrayList<CDField> cdFields = cdClass.getFields();

            for (CDField cdField : cdFields) {
                JSONObject field = new JSONObject();
                field.put("name", cdField.getName());
                field.put("visibility", cdField.getVisibilityAsString());
                fields.put(field);
            }

            JSONArray methods = new JSONArray();
            classJSON.put("methods", methods);

            ArrayList<CDField> cdMethods = cdClass.getMethods();

            for (CDField cdMethod : cdMethods) {
                JSONObject method = new JSONObject();
                method.put("name", cdMethod.getName());
                method.put("visibility", cdMethod.getVisibilityAsString());
                methods.put(method);
            }
            classes.put(classJSON);
        }
        for (int i = 0; i < cd.nodesLen(); i++) {
            JSONObject node = new JSONObject();
            node.put("from", cd.getCDNode(i).getFromAsInt(cd));
            node.put("fAnchor", cd.getCDNode(i).getfAnchor().getSymb());
            node.put("to", cd.getCDNode(i).getToAsInt(cd));
            node.put("tAnchor", cd.getCDNode(i).gettAnchor().getSymb());
            node.put("fCard", cd.getCDNode(i).getfCard());
            node.put("tCard", cd.getCDNode(i).gettCard());
            node.put("type", cd.getCDNode(i).getType());
            nodes.put(node);
        }
        return classDiagram;
    }

    /**
     * Encodes an ArrayList of sequence diagrams into a JSONArray
     * @param seqDiags an ArrayList of sequence diagrams to be encoded
     * @return encoded JSONArray
     */
    private static JSONArray encodeJSONseqDiags(ArrayList<SequenceDiagram> seqDiags) {
        JSONArray seqDiagsJSON = new JSONArray();
        JSONObject seqDiagJSON = new JSONObject();
        for (SequenceDiagram sd : seqDiags) {
            JSONArray objsJSON = new JSONArray();
            for (SDObject obj : sd.getObjects()) {
                JSONObject objJSON = new JSONObject();
                JSONArray actsJSON = new JSONArray();
                for (SDActivation act : obj.getActivations()) {
                    JSONObject actJSON = new JSONObject();
                    actJSON.put("timeBegin", act.getTimeBegin());
                    actJSON.put("timeEnd", act.getTimeEnd());

                    actsJSON.put(actJSON);
                }
                objJSON.put("objName", obj.getObjName());
                objJSON.put("className", obj.getClassName());
                objJSON.put("activations", actsJSON);
                objJSON.put("timePos", obj.getTimePos());

                objsJSON.put(objJSON);
            }
            seqDiagJSON.put("objects", objsJSON);

            // Messages
            JSONArray msgsJSON = new JSONArray();
            for (SDMessage msg : sd.getMessages()) {
                JSONObject msgJSON = new JSONObject();
                msgJSON.put("name", msg.getName());
                msgJSON.put("from", msg.getFrom(sd));
                msgJSON.put("to", msg.getTo(sd));
                msgJSON.put("type", msg.getTypeAsString());
                msgJSON.put("timePos", msg.getTimePos());

                msgsJSON.put(msgJSON);
            }
            seqDiagJSON.put("messages", msgsJSON);
            seqDiagsJSON.put(seqDiagJSON);
        }
        return seqDiagsJSON;
    }

    /**
     * Encodes a class diagram and its sequence diagrams as JSON.
     * @param cd The class diagram to be encoded
     * @param sds An ArrayList of sequence diagrams to be encoded.
     * @return A String representing a JSONObject
     */
    public static String encodeJSON(ClassDiagram cd, ArrayList<SequenceDiagram> sds) {
        JSONObject obj = new JSONObject();
        obj.put("classDiagram", encodeJSONclassDiag(cd));
        obj.put("sequenceDiagrams", encodeJSONseqDiags(sds));

        return obj.toString(4);
    }

}
