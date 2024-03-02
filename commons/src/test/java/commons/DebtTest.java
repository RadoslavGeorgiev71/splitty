package commons;

import commons.Debt;
import commons.Participant;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DebtTest {
    Participant p1 = new Participant(1, "a");
    Participant p2 = new Participant(2, "b");
    Debt d1 = new Debt(123, p1, p2, 500);
    Debt d2 = new Debt(123, p1, p2, 500);

    @Test
    void testConstructor(){
        assertNotNull(d1);
    }

    @Test
    void testGetId() {
        assertEquals(123, d1.getId());
    }

    @Test
    void testGetAmount() {
        assertEquals(500, d1.getAmount());
    }

    @Test
    void testIsPaid() {
        assertFalse(d1.isPaid());
    }

    @Test
    void testGetPaidDateTimeNULL() {
        assertEquals(d1.getPaidDateTime(), LocalDateTime.MIN);
    }

    @Test
    void testSetId() {
        d2.setId(321);
        assertEquals(d2.getId(), 321);
    }

    @Test
    void testSetAmount() {
        d2.setAmount(4000);
        assertEquals(d2.getAmount(), 4000);
    }

    @Test
    void testSetPaid() {
        d2.setPaid(true);
        assertTrue(d2.isPaid());
    }

    @Test
    void testSetPaidDateTime() {
        LocalDateTime now = LocalDateTime.now();
        d2.setPaidDateTime(now);
        assertNotNull(d2.getPaidDateTime());
    }

    @Test
    void testGetPaidDateTimeNotNull() {
        LocalDateTime now = LocalDateTime.now();
        d2.setPaidDateTime(now);
        assertEquals(now, d2.getPaidDateTime());
    }
}