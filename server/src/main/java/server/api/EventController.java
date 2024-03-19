package server.api;

import commons.Event;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventRepository repo;

    /**
     * Constructor for the controller
     * @param repo - type EventRepository which extends JpaRepository
     */
    public EventController(EventRepository repo) {
        this.repo = repo;
    }

    /**
     * Get all events
     * @return all events
     */
    @GetMapping(path = {"", "/"})
    public List<Event> getAll() {
        return repo.findAll();
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
        Optional<Event> eventOptional = repo.findByInviteCode(inviteCode);

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
        Event newEvent = repo.save(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(newEvent);
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

        Optional<Event> existingEvent = repo.findById(eventId);

        if (existingEvent.isPresent()) {
            existingEvent.get().setTitle(updatedEvent.getTitle());
            existingEvent.get().setInviteCode(updatedEvent.getInviteCode());
            existingEvent.get().setParticipants(updatedEvent.getParticipants());
            existingEvent.get().setExpenses(updatedEvent.getExpenses());


            Event savedEvent = repo.save(existingEvent.get());


            return ResponseEntity.ok(savedEvent);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found");
    }
}