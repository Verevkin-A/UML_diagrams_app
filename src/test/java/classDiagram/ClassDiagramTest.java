package classDiagram;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parser.Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * A test suite for the ClassDiagram
 * @author Marek Dohnal
 * @since 2022-04-11
 */
public class ClassDiagramTest {
    ClassDiagram cd;
    String diagString;
    @BeforeEach
    void setUp() {
        cd = new ClassDiagram();
        try {
            String filepath = "data/test/seqDiagTest/seqDiagTestIn.json";
            diagString = Files.readString(Paths.get(filepath));
            cd = Parser.decodeJSON(diagString);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testRemoveClass() {
        Assertions.assertThrows(IndexOutOfBoundsException.class,() -> cd.removeClass(-1));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> cd.removeClass(3));

        CDClass classRmvdParent = cd.getCDClass(2);
        Assertions.assertEquals(0, classRmvdParent.getParent());
        Assertions.assertEquals(0, classRmvdParent.getParent());
        Assertions.assertEquals(3, cd.nodesLen());
        cd.removeClass(0);

        Assertions.assertEquals(-1, classRmvdParent.getParent());
        Assertions.assertEquals(1, cd.nodesLen());
    }

    @Test
    public void testSuperMethods() {
        CDClass testClass = cd.getCDClass(1);

        ArrayList<CDField> superMethods = testClass.getSuperclassMethods(cd);

        Assertions.assertEquals(superMethods.get(0).getName(), "myTestMethod2");
        Assertions.assertEquals(superMethods.get(1).getName(), "myTestMethod");
        Assertions.assertEquals(superMethods.get(2).getName(), "myTestMethod5");

        Assertions.assertEquals(superMethods.size(), 3);
    }

    @Test
    public void testRemoveMethod() {
        ClassDiagram cd2 = new ClassDiagram();
        try {
            String filepath = "data/test/GUITest/methodOverriding.json";
            diagString = Files.readString(Paths.get(filepath));
            cd2 = Parser.decodeJSON(diagString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        CDClass testClass = cd2.getCDClass(2);
        Assertions.assertTrue(testClass.checkDeleteMethod(cd2, "myMethod"));


    }

    @Test
    public void testOverridenMethods() {
        CDClass testClass = cd.getCDClass(1);

        ArrayList<CDField> overridenMethods = testClass.getOverridenMethods(cd);

        Assertions.assertEquals(overridenMethods.get(0).getName(), "myTestMethod");
        Assertions.assertEquals(overridenMethods.get(1).getName(), "myTestMethod2");

        Assertions.assertEquals(overridenMethods.size(), 2);
    }
}
