package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseTest {

    Participant a = new Participant("Ana");
    Participant b = new Participant("Are");
    Participant c = new Participant("Mere");

    List<Participant> list;

    Expense exp1;
    Expense exp2;
    Expense exp3;

    @BeforeEach
    void first() {
        list = new ArrayList<>();
        list.add(a);
        list.add(b);
        list.add(c);
        exp1 = new Expense("party", a, 100, list, "2/25/2024");
        exp2 = new Expense("party", a, 100, list, "2/25/2024");
        exp3 = new Expense("partyy", b, 56, list);
    }

    @Test
    void getTitle() {
        assertEquals("party", exp1.getTitle());
    }

    @Test
    void setTitle() {
        exp1.setTitle("Party");
        assertEquals("Party", exp1.getTitle());
    }

    @Test
    void getPayingParticipant() {
        assertEquals(a, exp1.getPayingParticipant());
    }

    @Test
    void setPayingParticipant() {
        exp1.setPayingParticipant(b);
        assertEquals(b, exp1.getPayingParticipant());
    }

    @Test
    void getAmount() {
        assertEquals(100.0d, exp1.getAmount());
    }

    @Test
    void setAmount() {
        exp1.setAmount(150.0d);
        assertEquals(150.0d, exp1.getAmount());
    }

    @Test
    void getParticipants() {
        assertEquals(list, exp1.getParticipants());
    }

    @Test
    void setParticipants() {
        List<Participant> list2 = new ArrayList<>();
        list2.add(a);

        exp1.setParticipants(list2);
        assertEquals(list2, exp1.getParticipants());
    }

    @Test
    void getDateTime() {
        assertEquals("2/25/2024", exp1.getDateTime());
    }

    @Test
    void setDateTime() {
        exp1.setDateTime("2/26/2024");
        assertEquals("2/26/2024", exp1.getDateTime());
    }

    @Test
    void getActivity() {
        assertEquals("party payed by Ana", exp1.getActivity());
    }

    @Test
    void testEquals() {
        assertEquals(exp1, exp2);
        assertNotEquals(exp1, exp3);
    }

    @Test
    void addParticipant() {
        Participant pepe = new Participant("Pepe");
        assertFalse(exp1.getParticipants().contains(pepe));
        exp1.addParticipant(pepe);
        assertTrue(exp1.getParticipants().contains(pepe));
    }

    @Test
    void delParticipant() {
        assertTrue(exp1.getParticipants().contains(a));
        exp1.delParticipant(a);
        assertFalse(exp1.getParticipants().contains(a));
    }
}