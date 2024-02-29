package commons;

import commons.Tag;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class TagTest {
    commons.Tag t1, t2, t3;
    @BeforeEach
    void testBasis() {
        t1 = new Tag("food", "red");
        t2 = new commons.Tag("drinks", "blue");
        t3 = new commons.Tag("drinks", "blue");
    }

    @Test
    void testConstructor() {
        assertNotNull(t1);
    }

    @Test
    void testGetType() {
        assertEquals("food", t1.getType());
    }

    @Test
    void testGetColor() {
        assertEquals("red", t1.getColor());
    }

    @Test
    void testSetType() {
        t1.setType("decoration");
        assertEquals("decoration", t1.getType());
    }

    @Test
    void testSetColor() {
        t1.setColor("purple");
        assertEquals("purple", t1.getColor());
    }

    @Test
    void testEquals() {
        assertEquals(t2, t3);
        assertNotEquals(t1, t2);
    }

    @Test
    void testHashCode() {
        assertEquals(t2.hashCode(), t3.hashCode());
    }
}
