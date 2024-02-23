package server;

import org.junit.jupiter.api.Test;
import org.w3c.dom.events.Event;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class AdminTest {

    @Test
    void getSocket() {
        Admin admin = new Admin();
        Socket socket = new Socket();
        admin.setSocket(socket);
        assertEquals(socket, admin.getSocket());
    }

    @Test
    void setSocket() {
        Admin admin = new Admin();
        Socket socket = new Socket();
        admin.setSocket(socket);
        assertEquals(socket, admin.getSocket());
    }

    @Test
    void getPort() {
        Admin admin = new Admin();
        admin.setPort(4000);
        assertEquals(4000, admin.getPort());
    }

    @Test
    void setPort() {
        Admin admin = new Admin();
        admin.setPort(4000);
        assertEquals(4000, admin.getPort());
    }

    @Test
    void getHost() {
        Admin admin = new Admin();
        admin.setHost("19:18");
        assertEquals("19:18", admin.getHost());
    }

    @Test
    void setHost() {
        Admin admin = new Admin();
        admin.setHost("19:18");
        assertEquals("19:18", admin.getHost());
    }

    @Test
    void login() {
        //TODO
    }

    @Test
    void getEvents() {
        Admin admin = new Admin();
        admin.login("password", "host");
        List<Event> list = admin.getEvents();
        assertNotNull(list);
    }

    @Test
    void orderByCDate() {
        Admin admin = new Admin();
        admin.login("password", "host");
        List<Event> list = admin.orderByCDate();
        assertNotNull(list);
    }

    @Test
    void orderByLastActivity() {
        Admin admin = new Admin();
        admin.login("password", "host");
        List<Event> list = admin.orderByLastActivity();
        assertNotNull(list);
    }

    @Test
    void orderByTitle() {
        Admin admin = new Admin();
        admin.login("password", "host");
        List<Event> list = admin.orderByTitle();
        assertNotNull(list);
    }

    @Test
    void deleteEvent() {
        Admin admin = new Admin();
        admin.login("password", "host");
        List<Event> list = admin.getEvents();
        if(list.size() > 0){
            Event event = list.get(0);
            admin.deleteEvent(event);
            list = admin.getEvents();
            assertTrue(!list.contains(event));
        }
    }

    @Test
    void exportEvents() {
        String filepath = "";
        Admin admin = new Admin();
        admin.login("password", "host");
        List<Event> list = admin.getEvents();
        String json = ""; // turn events in list into json
        String output = "";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filepath));
            output = String.valueOf(reader.read());
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(json, output);
    }

    @Test
    void importEvents() {
        Admin admin = new Admin();
        admin.login("password", "host");
        Event event = new Event();
        List<Event> toadd = List.of(event);
        String filepath = "";
        //create file with the json of this event at filepath
        admin.importEvents(filepath);
        List<Event> list = admin.getEvents();
        assertTrue(!list.contains(toadd));
    }
}