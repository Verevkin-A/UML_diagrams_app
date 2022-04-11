package classDiagram;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parser.Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
            String filepath = "data/format.json";
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
}
