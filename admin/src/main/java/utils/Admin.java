package utils;

import commons.Event;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

import java.io.*;
import java.util.*;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class Admin{

//    private static Socket socket;
//    private int port;
//    private static String host;
//    /**
//     * Default constructor
//     */
//    public utils.Admin(){
//    }
//
//    /**
//     * Constructor with port as parameter
//     * @param port to connect
//     */
//    public utils.Admin(int port){
//        this.socket = null;
//        this.port = port;
//    }
//
//    /**
//     * getter for socket
//     * @return the socket that is being used
//     */
//    public static Socket getSocket() {
//        return socket;
//    }
//
//    /**
//     * Setter for socket
//     * @param socket to use
//     */
//    public static void setSocket(Socket socket) {
//        utils.Admin.socket = socket;
//    }
//
//    /**
//     * Getter for port
//     * @return the port
//     */
//    public int getPort() {
//        return port;
//    }
//
//    /**
//     * Setter for port
//     * @param port to use
//     */
//    public void setPort(int port) {
//        this.port = port;
//    }
//
//    /**
//     * Getter for the host address
//     * @return the host address of the server
//     */
//    public static String getHost() {
//        return host;
//    }
//
//    /**
//     * Setter for host
//     * @param host to use
//     */
//    public static void setHost(String host) {
//        utils.Admin.host = host;
//    }



    private static final String SERVER = "http://localhost:8080/";
    private String password = "none set";

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
     * This method sends a get request to the server which
     * generates a password and prints it to the server console
     */
    public void generatePassword(){
        ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/admin/") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get();
    }

    /**
     * Sends password to server for admin authentication
     * @param password to login
     * @return Boolean authenticated
     */
    public boolean login(String password){
        Response res = ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/admin/") //
                .request(APPLICATION_JSON) //
                .post(Entity.entity(password, APPLICATION_JSON));
        boolean isAuthenticated = res.getStatus() == Response.Status.OK.getStatusCode();
        return isAuthenticated;
    }

    /**
     * Requests all the events in the server
     * @return List<Event> with all events in the server
     */
    public List<Event> getEvents(){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/events") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Event>>() {});
    }

    /**
     * Requests all the events in the server, ordered by date
     * @return List<Event> with all events in the server ordered by date
     */
    public List<Event> orderByCDate(){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/events?orderBy=cdate") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Event>>() {});
    }

    /**
     * Orders the events by last activity
     * @return the sorted list with events
     */
    public List<Event> orderByLastActivity(){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/events?orderBy=lastactivity") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Event>>() {});
    }

    /**
     * Orders events by title
     * @return the ordered list
     */
    public List<Event> orderByTitle(){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/events?orderBy=title") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Event>>() {});
    }

    /**
     * Gets an event and deletes it from the database of the server
     * @param event to delete
     */
    public void deleteEvent(Event event){
        ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/events/{eventId}") //
                .resolveTemplate("eventId", event.getId()) // Resolve the template with the event ID
                .request() //
                .delete();
        //should delete all related expenses and debts!!!
    }

    /**
     * Retreives all related information of an event and stores it
     * into a JSON file in the specified filepath
     * @param filepath to store the JSON
     * @param event to export
     */
    public void exportEvent(String filepath, Event event){ //TODO needs work!!!!
        ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/events?eventId={eventId}") //
                .resolveTemplate("eventId", event.getId()) // Resolve the template with the event ID
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get();
        //initialize connection
        //TODO call to server to get all events
        List<Event> list = null; //TODO receive all events
        //TODO translate to JSON -- probably call another method
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filepath));
            writer.write(String.valueOf(list));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets a filepath to a JSON file from which it reads
     * events, expenses and debts and adds them to the database of the server
     * @param filepath of the JSON file specified by the admin
     */
    public void importEvents(String filepath) throws IOException {
        //initialize connection
        BufferedReader reader = null;
        String output = "";
        try {
            reader = new BufferedReader(new FileReader(filepath));
            output = String.valueOf(reader.read());
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        var response = ClientBuilder.newClient ()
                .target("http://localhost:8080/api/import")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(output, MediaType.APPLICATION_JSON));
    }
}
