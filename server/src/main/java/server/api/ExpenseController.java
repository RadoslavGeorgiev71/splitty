package server.api;


import commons.Expense;
//import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.ExpenseRepository;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    private final ExpenseRepository repo;

    /**
     * Constructor for the controller
     * @param repo - type ExpenseRepository which extends JpaRepository
     */
    public ExpenseController(ExpenseRepository repo) {
        this.repo = repo;
    }

    /**
     * Get all expenses
     * @return all expenses
     */
    @GetMapping(path = {"", "/"})
    public List<Expense> getAll() {
        return repo.findAll();
    }


    /*TODO a method that returns all expenses of a specific event
     * method that adds a new expense in a specific event
     * method that updates an expense
     */

}
