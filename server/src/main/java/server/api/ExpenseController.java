package server.api;



import commons.Expense;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.Services.ExpenseService;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    private ExpenseService expenseService;

    /**
     * Constructor for the controller
     * @param expenseService - the expense service for the expense repository
     */
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    /**
     * Get all expenses
     * @return all expenses
     */
    @GetMapping(path = {"", "/"})
    public List<Expense> getAll() {
        return expenseService.getAll();
    }


    /*TODO a method that returns all expenses of a specific event
     * method that adds a new expense in a specific event
     * method that updates an expense
     */

    /**
     * Get all expenses of a specific event
     * @param eventId of event
     * @return all expenses
     */
    @GetMapping(path = {"", "/{eventId}"})
    public List<Expense> getAllEvent(@PathVariable("eventId") long eventId) {
        return expenseService.findByEventId(eventId);
    }

    /**
     * Mapping to create an expense
     * @param expense the expense to add to database
     * @return Response when expense created
     */
    @PostMapping(path = {""})
    public ResponseEntity<?> createExpense(@RequestBody Expense expense){
        Expense savedExpense = expenseService.create(expense);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedExpense);
    }

    /**
     * Mapping to create an expense
     * @param eventId of the event where the new expense is added
     * @param expense the expense to add to database
     * @return Response when expense created
     */
    @PostMapping(path = {"", "/{eventId}"})
    public ResponseEntity<?> createEventExpense(@PathVariable("eventId") long eventId, @RequestBody Expense expense){
        Expense savedExpense = expenseService.create(eventId,expense);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedExpense);
    }

    /**
     * Method to update an expense
     * @param id of expense
     * @param updatedExpense the changed expense
     * @return response when expense updated or not found
     */
    @PutMapping(path = {"", "/persist/{id}"})
    public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody Expense updatedExpense) {
        Expense existingExpense = expenseService.update(id, updatedExpense);
        if (existingExpense != null) {
            return ResponseEntity.ok(existingExpense);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Expense not found");
    }

}
