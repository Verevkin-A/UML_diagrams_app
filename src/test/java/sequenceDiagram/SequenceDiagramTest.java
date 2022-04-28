package sequenceDiagram;

import classDiagram.CDClass;
import classDiagram.CDNode;
import classDiagram.ClassDiagram;
import classDiagram.NodeType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parser.Parser;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * A tester class for sequence diagrams, and the relationship between
 * the class diagram and its sequence diagrams
 */
public class SequenceDiagramTest {
    ClassDiagram cd;
    String diagString;
    @BeforeEach
    void setUp() {
        cd = new ClassDiagram();
        try {
            String filepath = "data/format.json";
            diagString = Files.readString(Paths.get(filepath));
            cd = Parser.decodeJSON(diagString);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void checkInconsistentObjOnLoad() {
        SDObject testObj = cd.getSequenceDiagrams().get(0).getObjects().get(0);
        Assertions.assertTrue(testObj.isInconsistentOnLoad());


    }

    @Test
    public void checkCausedInconsistencyOnObj() {
        CDClass testClass = cd.getCDClass(0);
        SDObject testObj = cd.getSequenceDiagrams().get(0).getObjects().get(0);

        Assertions.assertTrue(testObj.checkClassName(cd, "testClass"));

        testClass.setName("SDo2className");
        Assertions.assertFalse(testClass.checkInterface(cd, true));

        Assertions.assertTrue(testClass.checkInterface(cd, false));

        Assertions.assertFalse(testClass.checkName(cd, "nonsense"));

        Assertions.assertTrue(testClass.checkName(cd, "SDo1className"));
    }

    @Test
    public void checkInconsistentMsgOnLoad() {
        SDMessage testMsg = cd.getSequenceDiagrams().get(0).getMessages().get(0);

        Assertions.assertTrue(testMsg.isInconsistentOnLoad());
    }

    @Test
    public void checkCausedInconsistencyOnMsg() {
        CDClass testClassFrom = cd.getCDClass(0);
        CDClass testClassTo = cd.getCDClass(1);
        CDNode testNode0 = cd.getCDNode(0);
        CDNode testNode1 = cd.getCDNode(1);

        SDObject testObjFrom = cd.getSequenceDiagrams().get(0).getObjects().get(0);
        SDObject testObjTo = cd.getSequenceDiagrams().get(0).getObjects().get(1);
        SDMessage testMsg = cd.getSequenceDiagrams().get(0).getMessages().get(0);

        Assertions.assertFalse(testMsg.checkFromAndTo(cd, testObjFrom, testObjTo));

        testObjFrom.setClassName("testClass");
        testObjTo.setClassName("testClass3");

        Assertions.assertTrue(testMsg.checkFromAndTo(cd, testObjFrom, testObjTo));
        Assertions.assertTrue(testNode0.checkType(cd, NodeType.GENERALIZATION));
        Assertions.assertTrue(testNode1.checkType(cd, NodeType.GENERALIZATION));

        testClassFrom.setName("nonsense1");
        testClassTo.setName("nonsense2");
        Assertions.assertFalse(testNode0.checkFromAndTo(cd, testClassFrom, testClassTo));
        Assertions.assertFalse(testNode0.checkFromAndTo(cd, testClassTo, testClassFrom));
        Assertions.assertFalse(testNode1.checkFromAndTo(cd, testClassFrom, testClassTo));
        Assertions.assertFalse(testNode1.checkFromAndTo(cd, testClassTo, testClassFrom));

        testClassFrom.setName("testClass");
        testClassTo.setName("testClass3");
        Assertions.assertTrue(testNode0.checkFromAndTo(cd, testClassFrom, testClassTo));
        Assertions.assertTrue(testNode0.checkFromAndTo(cd, testClassTo, testClassFrom));
        Assertions.assertTrue(testNode1.checkFromAndTo(cd, testClassFrom, testClassTo));
        Assertions.assertTrue(testNode1.checkFromAndTo(cd, testClassTo, testClassFrom));
    }
}
