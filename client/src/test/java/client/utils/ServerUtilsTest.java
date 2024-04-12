package client.utils;

import commons.*;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
//import org.mockserver.integration.ClientAndServer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ServerUtilsTest {

    @Mock
    WebTarget mockWebTarget;

    @Mock
    Invocation.Builder mockBuilder;

    @Mock
    Response mockResponse;
    //private ClientAndServer mockServer;
    public ServerUtils server;
    public ConfigClient configClient;
    public Event event;
    public Participant participant;
    public Participant participant2;
    public Expense expense;

    @BeforeEach
    void ini() {
        MockitoAnnotations.initMocks(this);
        //mockServer = ClientAndServer.startClientAndServer(8080);
        server = new ServerUtils();
        ServerUtils.setURL("http://localhost:8081/");

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
        expense.setDateTime("2004-08-12");
        expense.setDebts(new ArrayList<>());
        expense.setTag(new Tag("","#FFFFFF"));

        List<Participant> participants = new ArrayList<>();
        participants.add(participant);
        participants.add(participant2);
        event.setParticipants(participants);
        expense.setParticipants(participants);

        //event = server.addEvent(event);
    }

    @Test
    void setURL() {
        ServerUtils.setURL("http://localhost:8081/");
        //assertEquals("http://localhost:8081/", server);
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
        //event = server.addEvent(event);
        participant = event.getParticipants().get(0);
        participant2 = event.getParticipants().get(1);
        Debt debt = new Debt(participant, participant2, 10);

//        Debt res = server.addDebt(debt);
//        assertEquals(debt.getAmount(), res.getAmount());
//        assertEquals(debt.getPersonOwing(), res.getPersonOwing());
//        assertEquals(debt.getPersonPaying(), res.getPersonPaying());
    }

    @Test
    void deleteDebt() {
//        event = server.addEvent(event);
        participant = event.getParticipants().get(0);
        participant2 = event.getParticipants().get(1);
        Debt debt = new Debt(participant, participant2, 10);

//        Debt res = server.addDebt(debt);
//        assertEquals(debt.getAmount(), res.getAmount());
//        Response response = server.deleteDebt(res);
//        assertEquals(200, response.getStatus());
    }

    @Test
    void getEvent() {
//        event = server.addEvent(event);
//        Event res = server.getEvent(event.getId());
//        assertEquals(event, res);
    }

    @Test
    void addEvent() {
        Event event = new Event();
        event.setTitle("Test");
        event.createInviteCode();
        //Event res = server.addEvent(event);
//        assertEquals(event.getInviteCode(), res.getInviteCode());
//        assertEquals(event.getTitle(), res.getTitle());
    }

    @Test
    void getEventByCode() {
        Event event = new Event();
        event.setTitle("Test");
        event.createInviteCode();
//        event = server.addEvent(event);
//        Event res = server.getEventByCode(event.getInviteCode());
//        assertEquals(event, res);
    }

    @Test
    void persistParticipant() {
//        event = server.addEvent(event);
        Participant participant = event.getParticipants().get(0);

        participant.setName("Cornel");
//        Participant res = server.persistParticipant(participant);
//        event = server.persistEvent(event);
//        assertEquals(res, participant);
//        assertTrue(event.getParticipants().contains(participant));
//        assertEquals(2, event.getParticipants().size());
    }

    @Test
    void deleteParticipant() {
//        event = server.addEvent(event);
        participant = event.getParticipants().get(0);
        participant2 = event.getParticipants().get(1);
//        event = server.getEvent(event.getId());
//        assertEquals(2, event.getParticipants().size());

        event.removeParticipant(participant);
//        event = server.persistEvent(event);
//        assertEquals(1, event.getParticipants().size());
//        assertFalse(event.getParticipants().contains(participant));
//
//        Response res = server.deleteParticipant(participant2);
//        assertEquals(200, res.getStatus());
//        assertEquals(0, event.getParticipants());
    }

    @Test
    void addParticipant() {
        Participant p = new Participant();
        p.setName("Gogu");
//        Response response = server.addParticipant(p);
//        Participant res = response.readEntity(Participant.class);
//        assertEquals(p.getName(), res.getName());

//        event = server.addEvent(event);
//        event.addParticipant(res);
//        event = server.persistEvent(event);
//        assertTrue(event.getParticipants().contains(res));
//        assertTrue(200 <= response.getStatus() && 300 > response.getStatus());
    }

    @Test
    void persistEvent() {
        //event = server.addEvent(event);
        event.setTitle("Idk");
//        Event res = server.persistEvent(event);
//        assertEquals(event, res);
    }

    @Test
    void registerEventUpdate() {
    }

    @Test
    void sendInvites() {
//        event = server.addEvent(event);
        List<String> emails = new ArrayList<>();
        emails.add("maria.c.burlacu@gmail.com");
        ConfigClient configClient = new ConfigClient();
        configClient.setEmail("mariaciprianaburlacu@gmail.com");
//        assertTrue(server.sendInvites(emails, event, "Maria"));
    }

    @Test
    void sendDefault() {
//        event = server.addEvent(event);
        ConfigClient configClient = new ConfigClient();
        configClient.setName("Maria");
        configClient.setEmail("maria.c.burlacu@gmail.com");
//        assertTrue(server.sendDefault());
    }

    @Test
    void sendRemainder() {
//        event = server.addEvent(event);
        ConfigClient configClient = new ConfigClient();
        configClient.setName("Maria");
        configClient.setEmail("mariaciprianaburlacu@gmail.com");
        Participant participant1 = new Participant("Maria");
        participant1.setEmail("maria.c.burlacu@gmail.com");
//        assertTrue(server.sendRemainder(participant1, 10,
//                participant1.getEmail(), event.getTitle()));
    }

    @Test
    void deleteExpense() {
    }

    @Test
    void testDeleteExpense() {
    }

    @Test
    void addExpense() {
//        event = server.addEvent(event);
        expense.setParticipants(event.getParticipants());
        event.addExpense(expense);
//        event = server.persistEvent(event);
//        assertTrue(event.getParticipants().contains(expense));
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
    void convertRate() throws IOException {
        //server.convertRate("2023-08-12", "EUR", "USD");
        String date = "2023-08-12";
        String from = "EUR";
        String to = "USD";
        String path = "client/src/main/resources/rates/"+ date +"/"
                + from + "/" + to + ".txt";
        Path filePath = Paths.get(path).toAbsolutePath();

        try {
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                System.out.println(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
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