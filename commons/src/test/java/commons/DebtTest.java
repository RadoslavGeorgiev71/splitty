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
    Debt d3 = new Debt(321, p2, p2, 511);

    @Test
    void testBasicConstructor() {
        Debt d0 = new Debt();
        assertNotNull(d0);
    }

    @Test
    void testConstructor(){
        assertNotNull(d1);
    }

    @Test
    void testGetId() {
        assertEquals(123, d1.getId());
    }

    @Test
    void testGetPersonPaying() {
        assertEquals(p1, d1.getPersonPaying());
    }

    @Test
    void testGetPersonOwed() {
        assertEquals(p2, d1.getPersonOwed());
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
    void testSetPersonPaying() {
        d1.setPersonPaying(p2);
        assertEquals(p2, d1.getPersonPaying());
    }

    @Test
    void testPersonOwed() {
        d1.setPersonOwed(p1);
        assertEquals(p1, d1.getPersonOwed());
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

    @Test
    void getPersonPaying() {
        Participant p = d1.getPersonPaying();
        assertEquals(p1, p);
    }

    @Test
    void getPersonOwed() {
        Participant p = d1.getPersonOwed();
        assertEquals(p2, p);
    }

    @Test
    void setPersonPaying() {
        Debt d = new Debt(123, p1, p2, 500);
        Participant p = d.getPersonPaying();
        d.setPersonPaying(p2);
        assertNotEquals(p, d.getPersonPaying());
        assertEquals(p2, d.getPersonPaying());
    }

    @Test
    void setPersonOwed() {
        Debt d = new Debt(123, p1, p2, 500);
        Participant p = d.getPersonOwed();
        d.setPersonOwed(p1);
        assertNotEquals(p, d.getPersonOwed());
        assertEquals(p1, d.getPersonOwed());
    }

    @Test
    void testEqualsEqual() {
        assertTrue(d1.equals(d2));
    }

    @Test
    void testEqualsSameObject() {
        assertTrue(d1.equals(d1));
    }

    @Test
    void testEqualsDifferent() {
        assertFalse(d1.equals(d3));
    }

    @Test
    void testEqualsNull() {
        assertFalse(d1.equals(null));
    }

    @Test
    void testHashCodeSame() {
        assertEquals(d1.hashCode(), d2.hashCode());
    }

    @Test
    void testHashCodeDifferent() {
        assertNotEquals(d1.hashCode(), d3.hashCode());
    }
}