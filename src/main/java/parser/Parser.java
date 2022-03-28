package parser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import classDiagram.*;
import org.json.*;

public class Parser {
    public static ClassDiagram decodeJSON(String path) {
        // File to String
        String diagString = "";
        try {
            diagString = Files.readString(Paths.get(path));
            System.out.println(diagString);

        } catch (IOException e) {
            e.printStackTrace();
        }

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
                        Visibility.valueOfLabel(fieldsJSON.getJSONObject(j).getString("name"))
                );
                newClass.addField(field);
            }

            for (int j = 0; j < methodsJSON.length(); j++) {
                CDField method = new CDField(
                        fieldsJSON.getJSONObject(j).getString("name"),
                        Visibility.valueOfLabel(fieldsJSON.getJSONObject(j).getString("name"))
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

    public static void encodeJSON(String path, ClassDiagram cd) {
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

        }


        try {
            FileWriter file = new FileWriter("data/testOutput.json");
            file.write(obj.toString());
            file.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
