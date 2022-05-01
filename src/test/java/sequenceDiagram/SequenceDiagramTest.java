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

        testObj.setClassName("testClass");
        testClass.setName(cd, "randomNamme");
        Assertions.assertEquals(testClass.getName(), testObj.getClassName());

        Assertions.assertFalse(testClass.checkInterface(cd, true));

        Assertions.assertTrue(testClass.checkInterface(cd, false));
    }

    @Test
    public void checkInconsistentMsgOnLoad() {
        SDMessage testMsg = cd.getSequenceDiagrams().get(0).getMessages().get(0);

        Assertions.assertTrue(testMsg.isInconsistentOnLoad());
    }

    @Test
    public void checkCausedInconsistencyOnMsg() {
        CDClass testClassTo = cd.getCDClass(1);

        SDObject testObjTo = cd.getSequenceDiagrams().get(0).getObjects().get(1);
        SDMessage testMsg = cd.getSequenceDiagrams().get(0).getMessages().get(0);

        Assertions.assertFalse(testMsg.checkConsistency(cd, testObjTo, testMsg.getName()));

        testObjTo.setClassName("testClass3");
        Assertions.assertTrue(cd.checkDeleteClass(testClassTo));

        Assertions.assertTrue(testMsg.checkConsistency(cd, testObjTo, "myTestMethod()"));
        Assertions.assertTrue(testMsg.checkConsistency(cd, testObjTo, "myTestMethod2()"));
        Assertions.assertTrue(testMsg.checkConsistency(cd, testObjTo, "myTestMethod5()"));
        Assertions.assertFalse(testMsg.checkConsistency(cd, testObjTo, "myTestMethod3()"));


        testObjTo.setClassName("nonsens1");
        Assertions.assertFalse(cd.checkDeleteClass(testClassTo));

        Assertions.assertFalse(testMsg.checkConsistency(cd, testObjTo, testMsg.getName()));
        Assertions.assertFalse(testMsg.checkConsistency(cd, testObjTo, "myTestMethod()"));
        Assertions.assertFalse(testMsg.checkConsistency(cd, testObjTo, "myTestMethod2()"));
        Assertions.assertFalse(testMsg.checkConsistency(cd, testObjTo, "myTestMethod5()"));
        Assertions.assertFalse(testMsg.checkConsistency(cd, testObjTo, "myTestMethod3()"));

        testObjTo.setClassName("testClass");
        Assertions.assertFalse(cd.checkDeleteClass(testClassTo));
        Assertions.assertTrue(testMsg.checkConsistency(cd, testObjTo, "myTestMethod()"));
        Assertions.assertFalse(testMsg.checkConsistency(cd, testObjTo, "myTestMethod2()"));


    }
}
