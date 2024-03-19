package server.api;

import commons.Debt;
import commons.Event;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.DebtRepository;
import server.database.EventRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/debts")
public class DebtController {
    private final DebtRepository repo;
    private final EventRepository eventRepo;

    /**
     * Constructor for the controller
     * @param repo - type DebtRepository which extends JpaRepository
     * @param eventRepo - type EventRepository which extends JpaRepository
     */
    public DebtController(DebtRepository repo, EventRepository eventRepo) {
        this.repo = repo;
        this.eventRepo = eventRepo;
    }

    /**
     * Get all debts
     * @return all debts
     */
    @GetMapping(path = {"", "/"})
    public List<Debt> getAll() {
        return repo.findAll();
    }

    /**
     * Return all debts associated with a specific event
     * @param eventId - the id of event we retrieve the debts for
     * @return all debts corresponding to the event
     */
    @GetMapping(path = {"event/{eventId}"})
    public ResponseEntity<List<Debt>> getDebtsForEvent(@PathVariable("eventId") long eventId) {
        Optional<Event> event = eventRepo.findById(eventId);
        return event.map(value -> ResponseEntity.ok(repo.findAll().stream().
                filter(x -> value.getParticipants().contains(x.getPersonPaying())).toList()))
            .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * Save a new debt if it is valid and returns a response
     * @param debt - the debt to be saved
     * @return response entity with "ok" status message if it is successful,
     * "bad request" message otherwise
     */
    @PostMapping(path = {"", "/"})
    public ResponseEntity<Debt> add(@RequestBody Debt debt) {
        if (debt.getPersonPaying() == null ||
            debt.getPersonOwed() == null || debt.isPaid()) {
            return ResponseEntity.badRequest().build();
        }

        Debt saved = repo.save(debt);
        return ResponseEntity.ok(saved);
    }

    /**
     * Deletes a debt by id if it exists
     * @param id - the id of the debt we search for
     * @return "Successful delete" response
     * if delete was successful, "Bad request" otherwise
     */
    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        repo.deleteById(id);
        return ResponseEntity.ok().body("Successful delete");
    }

    /**
     * Returns a debt by its id if it exists
     * @param id - the id to be searched with
     * @return response with either "ok" or "bad request"
     * response message
     */
    @GetMapping("/{id}")
    public ResponseEntity<Debt> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }
}
