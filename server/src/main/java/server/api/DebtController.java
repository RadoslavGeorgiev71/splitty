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

    public DebtController(DebtRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/")
    public List<Debt> getAll() {
        return repo.findAll();
    }

    @PostMapping("/")
    public ResponseEntity<Debt> add(@RequestBody Debt debt) {

    }

    @GetMapping("/{id}")
    public ResponseEntity<Debt> getById(@PathVariable("id") int id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }
}
