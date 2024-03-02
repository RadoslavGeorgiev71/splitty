package commons;

import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ParticipantTest {

    Participant p1, p2, p3;
    @BeforeEach
    void testBasis() {
        p1 = new Participant("Bob");
        p2 = new Participant(5, "Alice");
        p3 = new Participant(5, "Alice");
    }

    @Test
    void testConstructorWIthName() {
        assertNotNull(p1);
    }

    @Test
    void textConstructor() {
        assertNotNull(p2);
    }

    @Test
    void testGetId() {
        assertEquals(5, p2.getId());
    }

    @Test
    void testGetName() {
        assertEquals("Alice", p2.getName());
    }

    @Test
    void testSetId() {
        p1.setId(8);
        assertEquals(8, p1.getId());
    }

    @Test
    void setName() {
        p1.setName("Greg");
        assertEquals("Greg", p1.getName());
    }

    @Test
    void testEquals() {
        assertEquals(p2, p3);
        assertNotEquals(p1, p2);
    }

    @Test
    void testHashCode() {
        assertEquals(p2.hashCode(), p3.hashCode());
    }
}
