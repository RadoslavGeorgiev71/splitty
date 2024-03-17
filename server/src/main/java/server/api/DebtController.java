package server.api;

import commons.Debt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.DebtRepository;

import java.util.List;

@RestController
@RequestMapping("/api/debts")
public class DebtController {
    private final DebtRepository repo;

    /**
     * Constructor for the controller
     * @param repo - type DebtRepository which extends JpaRepository
     */
    public DebtController(DebtRepository repo) {
        this.repo = repo;
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
