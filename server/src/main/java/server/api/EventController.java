package server.api;

import commons.Event;
//import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;

import java.util.List;

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

    /**TODO a method that returns a specific event by id(or more useful by invitecode)
     * method that creates a new event and returns the invite code
     * method that adds participants to an event
     * method that removes participants from an event
     * method that adds an expense
     * method that removes an expense
     * method that deletes an event
     */
}