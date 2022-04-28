package parser;
import classDiagram.*;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import sequenceDiagram.SequenceDiagram;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * A test suite for the Parser class.
 * Demonstrates functionality of the application required for the Task 3.
 * @author Marek Dohnal
 * @since 2022-04-11
 */
public class ParserTest {
    /**
     * Tests whether a class was added correctly.
     */
    @Test
    public void addClass() {
        ClassDiagram cd = new ClassDiagram();
        String inString = "";
        String exptString = "";
        try {
            String inPath = "data/test/addClassIn.json";
            inString = Files.readString(Paths.get(inPath));
            cd = Parser.decodeJSON(inString);
            String exptPath = "data/test/addClassExpt.json";
            exptString = Files.readString(Paths.get(exptPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        CDClass addedClass = new CDClass();
        addedClass.setName(cd, "addedClass");
        addedClass.setParent(2);
        addedClass.addField(new CDField("addedAttr", Visibility.PRIVATE));
        addedClass.addMethod(new CDField("addedMeth()", Visibility.PUBLIC));
        addedClass.setInterface(false);
        addedClass.setPosition(20,20);

        cd.addClass(addedClass);

        JSONAssert.assertEquals(exptString, Parser.encodeJSON(cd), JSONCompareMode.STRICT);
    }

    /**
     * Tests whether a node was added correctly.
     */
    @Test
    public void addNode() {
        ClassDiagram cd = new ClassDiagram();
        String inString = "";
        String exptString = "";
        try {
            String inPath = "data/test/addNodeIn.json";
            inString = Files.readString(Paths.get(inPath));
            cd = Parser.decodeJSON(inString);
            String exptPath = "data/test/addNodeExpt.json";
            exptString = Files.readString(Paths.get(exptPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        CDNode addedNode = new CDNode(
                cd.getCDClass(2),
                AnchorType.LEFT,
                cd.getCDClass(2),
                AnchorType.UP,
                "1..*",
                "0",
                NodeType.ASSOCIATION
        );

        cd.addNode(addedNode);

        JSONAssert.assertEquals(exptString, Parser.encodeJSON(cd), JSONCompareMode.STRICT);
    }

    /**
     * Tests whether a class was removed correctly.
     */
    @Test
    public void removeClass() {
        ClassDiagram cd = new ClassDiagram();
        String inString = "";
        String exptString = "";
        try {
            String inPath = "data/test/removeClassIn.json";
            inString = Files.readString(Paths.get(inPath));
            cd = Parser.decodeJSON(inString);
            String exptPath = "data/test/removeClassExpt.json";
            exptString = Files.readString(Paths.get(exptPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        cd.removeClass(0);

        JSONAssert.assertEquals(exptString, Parser.encodeJSON(cd), JSONCompareMode.STRICT);

    }

    /**
     * Tests whether a node was removed correctly.
     */
    @Test
    public void removeNode() {
        ClassDiagram cd = new ClassDiagram();
        String inString = "";
        String exptString = "";
        try {
            String inPath = "data/test/removeNodeIn.json";
            inString = Files.readString(Paths.get(inPath));
            cd = Parser.decodeJSON(inString);
            String exptPath = "data/test/removeNodeExpt.json";
            exptString = Files.readString(Paths.get(exptPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        cd.removeNode(2);

        JSONAssert.assertEquals(exptString, Parser.encodeJSON(cd), JSONCompareMode.STRICT);

    }

    @Test
    public void emptyDiagram() {
        ClassDiagram cd = new ClassDiagram();
        String inString = "";
        String exptString = "";
        try {
            String inPath = "data/test/emptyDiagramIn.json";
            inString = Files.readString(Paths.get(inPath));
            cd = Parser.decodeJSON(inString);
            String exptPath = "data/test/emptyDiagramExpt.json";
            exptString = Files.readString(Paths.get(exptPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONAssert.assertEquals(exptString, Parser.encodeJSON(cd), JSONCompareMode.STRICT);

    }
}