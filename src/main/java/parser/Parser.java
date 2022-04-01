package parser;

import java.util.ArrayList;
import classDiagram.*;
import org.json.*;

public class Parser {
    public static ClassDiagram decodeJSON(String diagString) {
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
                CDField field = new CDField(
                        fieldsJSON.getJSONObject(j).getString("name"),
                        Visibility.valueOfLabel(fieldsJSON.getJSONObject(j).getString("visibility"))
                );
                newClass.addField(field);
            }

            for (int j = 0; j < methodsJSON.length(); j++) {
                CDField method = new CDField(
                        fieldsJSON.getJSONObject(j).getString("name"),
                        Visibility.valueOfLabel(fieldsJSON.getJSONObject(j).getString("visibility"))
                );
                newClass.addMethod(method);
            }

            newClass.setName(classJSON.getString("name"));
            newClass.setParent(classDiagram,
                    classJSON.getInt("parent"));
            newClass.setInterface(classJSON.getBoolean("isInterface"));

            classDiagram.addClass(newClass);
        }

        JSONArray nodesJSON = cdJSON.getJSONArray("nodes");

        for (int i = 0; i < nodesJSON.length(); i++) {
            JSONObject node = nodesJSON.getJSONObject(i);
            CDNode newNode = new CDNode(
                    classDiagram.getCDClass(node.getInt("from")),
                    classDiagram.getCDClass(node.getInt("to")),
                    node.getString("fCard"),
                    node.getString("tCard"),
                    NodeType.valueOfLabel(node.getInt("type"))
            );
            classDiagram.addNode(newNode);
        }

        return classDiagram;
    }

    public static String encodeJSON(ClassDiagram cd) {
        JSONObject obj = new JSONObject();
        JSONObject classDiagram = new JSONObject();
        JSONArray classes = new JSONArray();
        JSONArray nodes = new JSONArray();
        obj.put("classDiagram", classDiagram);
        classDiagram.put("classes", classes);
        classDiagram.put("nodes", nodes);

        for (int i = 0; i < cd.classesLen(); i++) {
            CDClass cdClass = cd.getCDClass(i);
            JSONObject classJSON = new JSONObject();
            classJSON.put("name", cdClass.getName());
            classJSON.put("parent", cdClass.getParentAsInt(cd));

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
            node.put("to", cd.getCDNode(i).getToAsInt(cd));
            node.put("fCard", cd.getCDNode(i).getfCard());
            node.put("tCard", cd.getCDNode(i).gettCard());
            node.put("type", cd.getCDNode(i).getType());
            nodes.put(node);
        }
        return obj.toString(4);
    }
}
