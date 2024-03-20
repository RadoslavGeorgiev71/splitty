package utils;

import commons.Event;
import jakarta.ws.rs.client.Client;
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
     * Retrieves json object of an event
     * into a JSON file in the specified filepath
     * @param eventID the id of the event to be dumped
     * @return boolean success
     */
    public boolean jsonDump(long eventID){
        Client client = ClientBuilder.newClient();
        Response response = client.target(SERVER)
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
     *
     * @param events events to add to the database
     */
    public void importEvents(List<Event> events) {

    }
}
