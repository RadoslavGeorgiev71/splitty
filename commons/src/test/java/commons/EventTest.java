package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EventTest {

    Event event1, event2, event3;
    Participant p1, p2, p3;
    Expense e1, e2, e3;
    List<Participant> participants1;
    List<Participant> participants2;
    List<Expense> expenses1;
    List<Expense> expenses2;

    @BeforeEach
    void first() {
        p1 = new Participant("Obama");
        p2 = new Participant("Joe");
        p3 = new Participant("Donald");

        e1 = new Expense("drinks", p1, 100, participants1, "2/25/2024");
        e2 = new Expense("snacks", p1, 100, participants1, "2/25/2024");
        e3 = new Expense("fuel", p3, 56, participants1);

        participants1 = new ArrayList<>();
        participants1.add(p1);
        participants1.add(p2);
        participants1.add(p3);

        expenses1 = new ArrayList<>();
        expenses1.add(e1);
        expenses1.add(e2);
        expenses1.add(e3);

        event1 = new Event("BBQ", "inviteCode1", p1, participants1, expenses1);
        event2 = new Event("Paintball", "inviteCode1", p2, participants1, expenses2);
        event3 = new Event("Swimming", "inviteCode2", p2, participants2, expenses1);
    }

    @Test
    void getTitle() {
        assertEquals("BBQ", event1.getTitle());
    }

    @Test
    void setTitle() {
        event1.setTitle("Lasergaming");
        assertEquals("Lasergaming", event1.getTitle());
    }

    @Test
    void getInviteCode() {
        assertEquals("inviteCode1", event1.getInviteCode());
    }

    @Test
    void setInviteCode() {
        event1.setInviteCode("inviteCode2");
        assertEquals("inviteCode2", event1.getInviteCode());
    }

    @Test
    void getCreator() {
        assertEquals(p1, event1.getCreator());
    }

    @Test
    void setCreator() {
        event1.setCreator(p2);
        assertEquals(p2, event1.getCreator());
    }

    @Test
    void getParticipants() {
        assertEquals(participants1, event1.getParticipants());
    }

    @Test
    void setParticipants() {
        event1.setParticipants(participants2);
        assertEquals(participants2, event1.getParticipants());
    }

    @Test
    void getExpenses() {
        assertEquals(expenses1, event1.getExpenses());
    }

    @Test
    void setExpenses() {
        event1.setExpenses(expenses2);
        assertEquals(expenses2, event1.getExpenses());
    }

    @Test
    void getId() {
        assertEquals(0, event1.getId());
    }

    @Test
    void setId() {
        System.out.println(event1.getId());
        event1.setId(1);
        assertEquals(1, event1.getId());
    }

    @Test
    void checkEquals() {
        assertNotSame(event1, event2);
    }

}
