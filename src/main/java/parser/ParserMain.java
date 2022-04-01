package parser;

import classDiagram.ClassDiagram;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * A temporary class to demonstrate Parser functionality.
 * @author Marek Dohnal (xdohna48)
 * @since 2022-03-26
 */
public class ParserMain {
    public static void main(String[] args) {
        ClassDiagram cd = new ClassDiagram();
        try {
            String filepath = "data/format.json";
            String diagString = Files.readString(Paths.get(filepath));
            cd = Parser.decodeJSON(diagString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileWriter file = new FileWriter("data/output.json");
            String output = Parser.encodeJSON(cd);
            file.write(output);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
