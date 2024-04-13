/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import commons.*;
import jakarta.ws.rs.ProcessingException;
//import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import javafx.scene.control.Alert;
import org.glassfish.jersey.client.ClientConfig;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;

public class ServerUtils {

    private static  String server = "http://localhost:8080/";

    /**
     * Setter for URL
     * @param url
     */
    public static void setURL(String url) {
        server = url;
    }

    /**
     * Get debts for an event
     *
     * @param event the event
     * @return a list of debts
     */
    public List<Debt> getPaymentInstructions(Event event) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/debts/event/" + event.getId())
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<>() {
                });
    }

    /**
     * Get all tags
     * @return - a list of the tags
     */
    public List<Tag> getTags() {
        return ClientBuilder.newClient(new ClientConfig())
            .target(server).path("api/tags")
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .get(new GenericType<>() {
            });
    }

    /**
     * Adds a new tag
     * @param tag - the tag to be added
     * @return the new tag
     */
    public Tag addTag(Tag tag) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(server).path("api/tags/add")
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .post(Entity.entity(tag, APPLICATION_JSON), Tag.class);
    }

    /**
     * Updates a tag
     * @param tag - the version of the tag
     * @return the tag
     */
    public Tag updateTag(Tag tag) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(server).path("api/tags/update")
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .post(Entity.entity(tag, APPLICATION_JSON), Tag.class);
    }

    /**
     * Deletes a tag
     * @param tag - the tag to be deleted
     * @return the response from the server
     */
    public Response deleteTag(Tag tag) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(server).path("api/tags/" + tag.getId())
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .delete();
    }

    /**
     * Adds debt to the database
     *
     * @param debt the debt to add
     * @return the debt added
     */
    public Debt addDebt(Debt debt) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/debts")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(debt, APPLICATION_JSON), Debt.class);
    }

    /**
     * Deletes a debt from the database
     *
     * @param debt the debt to delete
     * @return the response from the server
     */

    public Response deleteDebt(Debt debt) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/debts/" + debt.getId())
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete();
    }

    /**
     * This should get an event from the database by the id of the event
     *
     * @param id - the id it is looked for
     * @return the event with the specified id
     */
    public Event getEvent(long id) {
        try{
            Response response = ClientBuilder.newClient(new ClientConfig())
                    .target(server).path("/api/events/id/" + id)
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .get();
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return response.readEntity(Event.class);
            } else {
                showAlert();
                return  null;
            }
        }
        catch(ProcessingException e){
            showAlert();
            return null;
        }
    }

    /**
     * Method to send an event to the server to be saved
     *
     * @param event to be saved
     * @return the saved event by the server
     */
    public Event addEvent(Event event) {

        try{
            Response response = ClientBuilder.newClient(new ClientConfig())
                    .target(server).path("/api/events")
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .post(Entity.json(event));

            if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
                return response.readEntity(Event.class);
            } else {
                showAlert();
                return null;
            }
        }catch (ProcessingException e){
            showAlert();
            return null;
        }
    }

    /**
     * Method to get event by invite code
     * It uses a client and executes the api from eventController
     * Then if the response is ok it gets the event associated with the response
     * Otherwise throws exception
     *
     * @param inviteCode of event
     * @return event with that inviteCode or an exception
     */
    public Event getEventByCode(String inviteCode) {
        try{
            Response response = ClientBuilder.newClient(new ClientConfig())
                    .target(server).path("/api/events/" + inviteCode)
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .get();
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return response.readEntity(Event.class);
            } else {
                return  null;
            }
        }catch (ProcessingException e){
            showAlert(inviteCode);
            return null;
        }
    }

    /**
     * saves the changes to a participant
     *
     * @param participant - the participant we persist
     * @return the persisted participant
     */
    public Participant persistParticipant(Participant participant) {
        Entity<Participant> entity = Entity.entity(participant, APPLICATION_JSON);
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/participants/")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(entity, Participant.class);
    }

    /**
     * Deletes a participant from the server
     *
     * @param participant - the participant to delete
     * @return the response from the server
     */

    public Response deleteParticipant(Participant participant) {
        try{
            return ClientBuilder.newClient(new ClientConfig())
                    .target(server).path("api/participants/" + participant.getId())
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .delete();
        }
        catch(ProcessingException e){
            return null;
        }
    }

    /**
     * Adds a participant to the server
     *
     * @param participant - the participant to add
     * @return the response from the server
     */

    public Response addParticipant(Participant participant) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/participants/")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.json(participant));
    }

    /**
     * Persists an event
     *
     * @param event - the event to persist
     * @return the persisted event
     */
    public Event persistEvent(Event event) {
        Entity<Event> entity = Entity.entity(event, APPLICATION_JSON);
        try{
            return ClientBuilder.newClient(new ClientConfig())
                    .target(server).path("api/events/persist/" + event.getId())
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .put(entity, Event.class);
        }
        catch(ProcessingException e) {
            showAlert();
            return null;
        }
    }

    private static final ExecutorService EXEC = Executors.newSingleThreadExecutor();

    /**
     * Registers changes in the event
     * @param consumer - consumer that registers changes
     */
    public void registerEventUpdate(Consumer<Event> consumer) {
        EXEC.submit(() -> {
            while(!Thread.interrupted()) {
                Response res = ClientBuilder.newClient(new ClientConfig())
                        .target(server).path("api/events/update")
                        .request(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .get(Response.class);

                if(res.getStatus() == 204) {
                    continue;
                }
                Event event = res.readEntity(Event.class);
                consumer.accept(event);
            }
        });
    }

//    public void stop() {
//        EXEC.shutdownNow();
//    }

    /**
     * Sends the invite code of the event to the specified emails
     *
     * @param emails list of emails to sent
     * @param event  of which the invite code it sends
     * @param creatorname the persons who send the emails
     * @return true if the emails were sent successfully
     */
    public boolean sendInvites(List<String> emails, Event event, String creatorname) {
        try{
            Response response = ClientBuilder.newClient(new ClientConfig())
                    .target(server).path("/api/email/" + event.getInviteCode())
                    .queryParam("creatorName", creatorname)
                    .queryParam("creatorEmail", ConfigClient.getEmail())
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .post(Entity.json(emails));
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return true;
            } else {
                showAlert();
                return false;
            }
        }
        catch(ProcessingException e){
            showAlert();
            return false;
        }

    }

    /**
     * Sends a default email to the user to check
     * if the credentials are correct
     * @param email email address to sent
     * @return boolean
     */
    public boolean sendDefault(String email) {
        try{
            Participant participant = new Participant(0, ConfigClient.getName(),
                    email, ConfigClient.getIban(), ConfigClient.getBic());
            Response response = ClientBuilder.newClient(new ClientConfig())
                    .target(server).path("/api/email/default")
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .post(Entity.json(participant));
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return true;
            } else if(response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()){
                return false;
            } else {
                showAlert();
                return false;
            }
        }
        catch(ProcessingException e){
            showAlert();
            return false;
        }
    }

    /**
     * Method to send a remainder email to the person who has to pay a debt
     *
     * @param participant the participant of the person who will receive the money
     * @param amount amount that has to be paid
     * @param email email of the person who has to pay
     * @param eventTitle title of the event in which the expense is in
     * @return boolena
     */
    public boolean sendRemainder(Participant participant, double amount,
                                 String email, String eventTitle) {
        try{
            Response response = ClientBuilder.newClient(new ClientConfig())
                    .target(server).path("/api/email/")
                    .queryParam("creatorEmail", ConfigClient.getEmail())
                    .queryParam("amount", amount)
                    .queryParam("email", email)
                    .queryParam("eventTitle", eventTitle)
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .post(Entity.json(participant));
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return true;
            }
            else if(response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()){
                return false;
            }
            else {
                showAlert();
                return false;
            }
        }
        catch(ProcessingException e){
            showAlert();
            return false;
        }
    }



    /**
     * Deletes an expense from the server
     *
     * @param eventId - id
     * @param expense - the expense to delete
     * @return the response from the server
     */
    public Response deleteExpense(long eventId, Expense expense) {
        Entity<Expense> entity = Entity.entity(expense, APPLICATION_JSON);
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/expenses/remove/" + eventId)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(entity);
    }

    /**
     * Deletes an expense from the server
     *
     * @param expense - the expense to delete
     * @return the response from the server
     */
    public Response deleteExpense(Expense expense) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/expenses/" + expense.getId())
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete();
    }

    /**
     * Adds an expense to the server
     *
     * @param expense - the expense to add
     * @return the response from the server
     */
    public Expense addExpense(Expense expense) {
        try{
            //expense.setId(1000);
            Entity<Expense> entity = Entity.entity(expense, APPLICATION_JSON);
            Response response = ClientBuilder.newClient(new ClientConfig())
                            .target(server).path("api/expenses")
                            .request(APPLICATION_JSON)
                            .accept(APPLICATION_JSON)
                            .post(entity);
            if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
                return response.readEntity(Expense.class);
            }
            return null;
        }
        catch (ProcessingException e){
            return null;
        }
    }

    /**
     * Adds an expense to the server
     *
     * @param eventId - where to add
     * @param expense - the expense to add
     * @return the response from the server
     */
    public Expense addExpense(long eventId, Expense expense) {
        try{
            //expense.setId(1000);
            Response response = ClientBuilder.newClient(new ClientConfig())
                    .target(server).path("api/expenses/event/" + eventId)
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .post(Entity.json(expense));
            if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
                return response.readEntity(Expense.class);
            } else {
                return null;
            }
        }catch (ProcessingException e){
            return null;
        }
    }

    /**
     * List of expense to the server
     *
     * @param eventId - event
     * @return the response from the server
     */
    public List<Expense> getExpense(long eventId) {
        Response response = ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/expenses/event/" + eventId)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get();
        //return response.readEntity(List<Expense>.class);
        return null;
    }

    /**
     * Persists an expense
     *
     * @param eventId
     * @param expense - the expense to persist
     * @return the persisted expense
     */
    public Expense updateExpense(long eventId, Expense expense) {
        Entity<Expense> entity = Entity.entity(expense, APPLICATION_JSON);
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/expenses/event/" + eventId)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(entity, Expense.class);
    }

    /**
     * Persists an expense
     *
     * @param expense - the expense to persist
     * @return the persisted expense
     */
    public Expense persistExpense(Expense expense) {
        Entity<Expense> entity = Entity.entity(expense, APPLICATION_JSON);
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/expenses/id/" + expense.getId())
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(entity, Expense.class);
    }

    /**
     * This should give converted currency
     *
     * @param date - the date it is looked for
     * @param from currency
     * @param to currency
     * @return the event with the specified id
     */
    public Double convertRate(String date, String from, String to) throws IOException {
        String path = "src/main/resources/rates/"+ date +"/"
                + from + "/" + to + ".txt";
        Path filePath = Paths.get(path).toAbsolutePath();

        try{
            File myObj = new File(filePath.toString());
            Scanner myReader = new Scanner(myObj);
            String data = "";
            if (myReader.hasNextLine()) {
                data = myReader.nextLine();
            }
            myReader.close();
            return Double.parseDouble(data);
        } catch (Exception e) {

            String key = "488b2c548074f3e5d9e15ba3013a152d";
            String url = "http://data.fixer.io/api/" + date;
            url += "?access_key=" + key+ "&base=" + from + "&symbols=" + to;
//            String key2 = "";
//            String url2 = "https://free.currconv.com/api/v7/convert?q="+
//                    from + "_" + to + "&compact=ultra&date=" + date + "&apiKey=" + key2;
            String url3 = "api/expenses/date/"+ date + "/rate/"+from+"/"+to;
            Response response = ClientBuilder.newClient(new ClientConfig())
                    .target(url)
//                    .target(server).path(url3)
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .get();
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                Object res = response.readEntity(Object.class);
                String rate = res.toString();
                if(rate.contains(to+"=")){
                    rate = rate.split(to+"=")[1].split("}")[0];
                }
                File fDate = new File(Paths.get("src/main/resources/rates/"+ date).toAbsolutePath().toString());
                File fFrom = new File(Paths.get("src/main/resources/rates/"+ date + "/" + from).toAbsolutePath().toString());
                File myObj = new File(filePath.toString());
                boolean d = fDate.mkdir();
                boolean f = fFrom.mkdir();
                if(myObj.createNewFile()){
                    FileWriter myWriter = new FileWriter(filePath.toString());
                    myWriter.write(rate);
                    myWriter.close();
                }
                try{
                    return Double.parseDouble(rate);
                }
                catch (Exception e1){
                    return 1.0d;
                }
            } else {
                showAlert();
                return null;
            }
        }
    }

    /**
     * Show a pop up window with an alert when the client cannot connect
     * to the server
     */
    public void showAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error: Unable to connect to the server");
        alert.setContentText("Please make sure that the URL is " +
                "correct, that the server is running and that the email credentials are correct");
        alert.showAndWait();
    }

    /**
     * Show a pop up window with an alert when the client cannot connect
     * to the server
     * @param inviteCode that caused the problem
     */
    public void showAlert(String inviteCode){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error: Unable to connect to the server or " +
                "event with invite code: " + inviteCode + " does not exists");
        alert.setContentText("Please make sure that the URL and invite code are " +
                "correct, that the server is running and that the email credentials are correct");
        alert.showAndWait();
    }

    /**
     * Stops the client thread
     */
    public void stop() {
        EXEC.shutdownNow();
    }
}
