package classDiagram;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
public class ElementTest {
    @Test
    public void testElName() {
        Element el = new Element("test");
        Assertions.assertEquals("test", el.getName());
    }
}
