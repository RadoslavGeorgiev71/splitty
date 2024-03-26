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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import commons.Debt;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import commons.Event;
import commons.Participant;
import org.glassfish.jersey.client.ClientConfig;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;

public class ServerUtils {

    private static final String SERVER = "http://localhost:8080/";

    /**
     * Gets all quotes forcibly
     *
     * @throws IOException        - if the input stream is not found
     * @throws URISyntaxException - if the URI is not found
     */

    public void getQuotesTheHardWay() throws IOException, URISyntaxException {
        var url = new URI("http://localhost:8080/api/quotes").toURL();
        var is = url.openConnection().getInputStream();
        var br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }

    /**
     * Get debts for an event
     *
     * @param event the event
     * @return a list of debts
     */
    public List<Debt> getPaymentInstructions(Event event) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/debts/event/" + event.getId())
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<>() {
                });
    }

    /**
     * Adds debt to the database
     *
     * @param debt the debt to add
     * @return the debt added
     */

    public Debt addDebt(Debt debt) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/debts")
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
                .target(SERVER).path("api/debts/" + debt.getId())
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
        Response response = ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("/api/events/id/" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get();
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(Event.class);
        } else {
            throw new WebApplicationException("Event not found" + response.getStatus());
        }
    }

    /**
     * Method to send an event to the server to be saved
     *
     * @param event to be saved
     * @return the saved event by the server
     */
    public Event addEvent(Event event) {

        Response response = ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("/api/events")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.json(event));

        if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
            return response.readEntity(Event.class);
        } else {
            throw new WebApplicationException("Failed to create event. Status code: "
                    + response.getStatus());
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
        Response response = ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("/api/events/" + inviteCode)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get();
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(Event.class);
        } else {
            throw new WebApplicationException("Event not found" + response.getStatus());
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
                .target(SERVER).path("api/participants/")
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
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/participants/" + participant.getId())
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete();
    }

    /**
     * Adds a participant to the server
     *
     * @param participant - the participant to add
     * @return the response from the server
     */

    public Response addParticipant(Participant participant) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/participants/")
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
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/events/persist/" + event.getId())
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(entity, Event.class);
    }

    /**
     * Sends the invite code of the event to the specified emails
     *
     * @param emails list of emails to sent
     * @param event  of which the invite code it sends
     * @return true if the emails were sent successfully
     */
    public boolean sendInvites(List<String> emails, Event event) {
        Response response = ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("/api/email/" + event.getInviteCode())
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.json(emails));
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return true;
        } else {
            return false;
        }
    }
}