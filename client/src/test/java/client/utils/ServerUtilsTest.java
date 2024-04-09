package client.utils;

import commons.Event;
import commons.Expense;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ServerUtilsTest {

    public ServerUtils server = new ServerUtils();
    public Event event;
    public Participant participant;
    public Participant participant2;
    public Expense expense;

    @BeforeEach
    void ini() {
        event = new Event();
        event.setTitle("Test");
        event.createInviteCode();

        participant = new Participant();
        participant.setName("Ana");
        participant2 = new Participant();
        participant2.setName("Bob");

        expense = new Expense();
        expense.setTitle("Food");
        expense.setAmount(10);
        expense.setCurrency("EUR");
        expense.setPayingParticipant(participant);

        List<Participant> participants = new ArrayList<>();
        participants.add(participant);
        participants.add(participant2);
        event.setParticipants(participants);
        expense.setParticipants(participants);

        //event = server.addEvent(event);
    }

    @Test
    void setURL() {
        ServerUtils.setURL("http://localhost:8080/");
        //assertEquals("http://localhost:8081/", server.server);
    }

    @Test
    void getPaymentInstructions() {
    }

    @Test
    void getTags() {
    }

    @Test
    void addTag() {
    }

    @Test
    void updateTag() {
    }

    @Test
    void deleteTag() {
    }

    @Test
    void addDebt() {
    }

    @Test
    void deleteDebt() {
    }

    @Test
    void getEvent() {
        event = server.addEvent(event);
        Event res = server.getEvent(event.getId());
        assertEquals(event, res);
    }

    @Test
    void addEvent() {
        Event event = new Event();
        event.setTitle("Test");
        event.createInviteCode();
        Event res = server.addEvent(event);
        assertEquals(event.getInviteCode(), res.getInviteCode());
        assertEquals(event.getTitle(), res.getTitle());
    }

    @Test
    void getEventByCode() {
//        Event event = new Event();
//        event.setTitle("Test");
//        event.createInviteCode();
//        event = server.addEvent(event);
//        Event res = server.getEventByCode(event.getInviteCode());
//        assertEquals(event, res);
    }

    @Test
    void persistParticipant() {
    }

    @Test
    void deleteParticipant() {
    }

    @Test
    void addParticipant() {
        Participant p = new Participant();
        p.setName("Gogu");
        Participant res = server.addParticipant(p).readEntity(Participant.class);
        assertEquals(p.getName(), res.getName());

        event = server.addEvent(event);
        event.addParticipant(res);
        event = server.persistEvent(event);
        assertTrue(event.getParticipants().contains(res));
    }

    @Test
    void persistEvent() {
        event = server.addEvent(event);
        event.setTitle("Idk");
        Event res = server.persistEvent(event);
        assertEquals(event, res);
    }

    @Test
    void registerEventUpdate() {
    }

    @Test
    void sendInvites() {
    }

    @Test
    void sendDefault() {
    }

    @Test
    void sendRemainder() {
    }

    @Test
    void deleteExpense() {
    }

    @Test
    void testDeleteExpense() {
    }

    @Test
    void addExpense() {
        event = server.addEvent(event);
        event.addExpense(expense);
        Event res = server.persistEvent(event);
        assertEquals(event, res);
    }

    @Test
    void testAddExpense() {
    }

    @Test
    void getExpense() {
    }

    @Test
    void updateExpense() {
    }

    @Test
    void persistExpense() {
    }

    @Test
    void convertRate() {
    }

    @Test
    void showAlert() {
    }

    @Test
    void testShowAlert() {
    }

    @Test
    void stop() {
    }
}