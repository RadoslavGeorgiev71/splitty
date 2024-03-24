package server.api;


import commons.Expense;
import org.springframework.web.bind.annotation.*;
import server.ExpenseService;

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

}
