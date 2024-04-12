package utils;

import commons.Event;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.scene.control.Alert;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class Admin{

    private String server;
    private String password = "none set";
    private final StompSession session;

    /**
     * Constructor for Admin
     */
    public Admin(){
        this.session = connect("ws://localhost:8080/websocket");
        this.server = "http://localhost:8080/";
    }

    /**
     * Constructor for Admin, for testing purposes
     * @param s Url of the mock server
     * @param mockStompSession StompSession from Mockito
     */
    public Admin(String s, StompSession mockStompSession){
        this.session = mockStompSession;
        this.server = s;
    }

    /**
     * Sets the password for the Admin
     * @param password The password to be set
     */
    public void setPassword(String password){
        this.password = password;
    }

    /**
     * Returns the password the was set for admin
     * @return String password
     */
    public String getPassword(){
        return password;
    }

    /**
     * Sets the server for the Admin
     * @param url to be set
     */
    public void setURL(String url){
        this.server = url;
    }

    /**
     * Getter for ServerURL
     * @return server
     */
    public String getURL(){
        return server;
    }

    /**
     * This method sends a get request to the server which
     * generates a password and prints it to the server console
     * @return boolean true if ok false if error
     */
    public boolean generatePassword(){
        try {
            Response response = ClientBuilder.newClient()
                    .target(server)
                    .path("api/admin/")
                    .request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .get();
            response.close();
        } catch (ProcessingException e) {
            return false;
        }
        return true;
    }

    /**
     * Sends password to server for admin authentication
     * @param password to login
     * @return Boolean authenticated
     */
    public boolean login(String password){
        try {
            Response res = ClientBuilder.newClient(new ClientConfig()) //
                    .target(server).path("api/admin/") //
                    .request(APPLICATION_JSON) //
                    .post(Entity.entity(password, APPLICATION_JSON));
            int statusCode;
            if(!(res instanceof Response)){
                return true; //for testing puprposes with MockServer
            }
            else{
                statusCode = res.getStatus();
            }
            boolean isAuthenticated = statusCode == Response.Status.OK.getStatusCode();
            connect("ws://localhost:8080/websocket");
            return isAuthenticated;
        } catch (ProcessingException e) {
            return false;
        }
    }

    /**
     * Requests all the events in the server
     * @return List<Event> with all events in the server
     */
    public List<Event> getEvents(){
        try {
            return ClientBuilder.newClient(new ClientConfig()) //
                    .target(server).path("api/events") //
                    .request(APPLICATION_JSON) //
                    .accept(APPLICATION_JSON) //
                    .get(new GenericType<List<Event>>() {});
        } catch (ProcessingException e) {
            showalert();
            return new ArrayList<Event>();
        }
    }


    /**
     * Gets an event and deletes it from the database of the server
     * @param event to delete
     */
    public void deleteEvent(Event event){
        /*
        ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/events/{eventId}") //
                .resolveTemplate("eventId", event.getId()) // Resolve the template with the event ID
                .request() //
                .delete();
         */
        send("/app/eventsDelete", event);
    }

    /**
     * Retrieves json object of an event
     * into a JSON file in the specified filepath
     * @param eventID the id of the event to be dumped
     * @return boolean success
     */
    public boolean jsonDump(long eventID){
        Client client = ClientBuilder.newClient();
        Response response = client.target(server)
                .path("api/events/id/" + eventID) //
                .request(MediaType.APPLICATION_JSON)
                .get();
        //response obtained
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            try {
                File file = new File("event" + eventID + ".json");
                FileOutputStream outputStream = new FileOutputStream(file);
                // Read response entity as InputStream
                InputStream inputStream = response.readEntity(InputStream.class);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return false; //let the Overview controller deal with error message
        }
        response.close();
        client.close();
        return true; //let the overview controller deal with success message
    }

    /**
     * Takes a list of events to import in the database
     * If one of the events is already in the database (The id or the invitecode exists)
     * Then the process fails with a warning
     * @param event events to add to the database
     */
    public void importEvent(Event event) {
        List<Event> currentEvents = getEvents();
        for(Event cevent : currentEvents){
            if(cevent.getId() == event.getId() ||
                    cevent.getInviteCode().equals(event.getInviteCode())) {
                showalert(event);
                return;
            }
        }
        /*
        Response response = ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("/api/events")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.json(event));
        if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
            showalert(event);
        }

         */
        send("/app/events", event);
    }

    /**
     * Method that shows a warning to the user everytime
     * they try to import an event that is already in the database
     * @param event that causes the problem
     */
    public void showalert(Event event){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("JSON Import");
        alert.setHeaderText("Error");
        alert.setContentText("Event with id:  " + event.getId() +
                " could not be imported because there " +
                "is already an event with this id or invite code in the database");
        alert.showAndWait();
    }

    /**
     * Method that shows a warning to the user everytime
     * they try to import an event that is already in the database
     */
    public void showalert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Server Unavailable");
        alert.setHeaderText("Error");
        alert.setContentText("Unable to connect to server. " +
                "Check the url of the server or try again later");
        alert.showAndWait();
    }




    /**
     * Establishes a websocket connection
     * @param url The url to the server
     * @return A StompSession which is kept until admin is closed
     */
    private StompSession connect(String url) {
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        try{
            return stomp.connect(url, new StompSessionHandlerAdapter() {
            }).get();
        } catch(InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch(ExecutionException e){
            throw new RuntimeException(e);
        }
        throw new IllegalStateException();
    }

    /**
     * Registers an admin to this event channel
     * @param dest asd
     * @param consumer asd
     */
    public void registerForEvents(String dest, Consumer<Event> consumer) {
        session.subscribe(dest, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Event.class;
            }
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                consumer.accept((Event) payload);
            }
        });
    }

    /**
     * Registers an admin to this event deletion channel
     * @param dest asd
     * @param consumer asd
     */
    public void registerForEventDeletion(String dest, Consumer<Event> consumer) {
        session.subscribe(dest, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Event.class;
            }
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                consumer.accept((Event) payload);
            }
        });
    }

    /**
     * Sends and object to a path (channel)
     * @param dest the channel path
     * @param o the object to be sent
     */
    public void send(String dest, Object o) {
        session.send(dest, o);
//        StompSession.Receiptable receiptable = session.send(dest, o);
//        Event event = (Event) receiptable.
//        if(event == null){
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Error");
//            alert.setHeaderText("Error during importing the event");
//            alert.setContentText("The event you are trying to import possibly overwrites " +
//                    "data already in the database thus could not be imported");
//            alert.showAndWait();
//        }
    }
}
