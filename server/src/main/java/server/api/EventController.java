package server.api;

import commons.Event;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.Services.EventService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private EventService eventService;

    /**
     * Constructor for the controller
     * @param eventService - the service for handling events
     */
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * Get all events
     * @return all events
     */
    @GetMapping(path = {"", "/"})
    public List<Event> getAll() {
        return eventService.findAll();
    }

    /**TODO CHECK a method that returns a specific event by id(or more useful by invitecode)
     * CHECK method that creates a new event and returns the invite code
     * CHECK method that adds participants to an event
     * CHECK method that removes participants from an event
     * method that adds an expense
     * method that removes an expense
     * method that deletes an event
     */

    /**
     * Method to get an event by inviteCode
     * @param inviteCode code to join event
     * @return Either the event + ok or not found
     */
    @GetMapping(path = {"/{invite}"})
    public ResponseEntity<?> getByCode(@PathVariable("invite") String inviteCode){
        Optional<Event> eventOptional = eventService.findByInviteCode(inviteCode);

        if (eventOptional.isPresent()){
            return ResponseEntity.ok(eventOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found.");
        }
    }

    /**
     * Mapping to create an event
     * @param event the event to add to database
     * @return Response when event created
     */
    @PostMapping(path = {""})
    public ResponseEntity<?> createEvent(@RequestBody Event event){
        Event savedEvent = eventService.create(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent);
    }

    /**
     * Method to update an event
     * @param id of event
     * @param updatedEvent the changed event
     * @return response when event updated or not found
     */
    @PutMapping(path = {"", "/persist/{id}"})
    public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody Event updatedEvent) {
        long eventId = updatedEvent.getId();

        Event existingEvent = eventService.update(eventId, updatedEvent);
        if (existingEvent != null) {
            return ResponseEntity.ok(existingEvent);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found");
    }

    /**
     * Returns an event by its id if it exists
     * @param id - the id to be searched with
     * @return response with either "ok" or "bad request"
     * response message
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<Event> getById(@PathVariable("id") long id) {
        if (id < 0 || !eventService.existsById(id)){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(eventService.findById(id).get());
    }

    /**
     * Deletes an event by id if it exists
     * @param id - the id of the event we search for
     * @return "Successful delete" response
     * if delete was successful, "Bad request" otherwise
     */
    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        if (!eventService.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        Event event = eventService.findById(id).get();
        eventService.deleteById(id);
        eventService.flush();
        return ResponseEntity.ok().body("Successful delete");
    }
}