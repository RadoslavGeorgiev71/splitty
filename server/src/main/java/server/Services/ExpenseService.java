package server.Services;

import commons.Expense;
import org.springframework.stereotype.Service;
import server.database.ExpenseRepository;

import java.util.List;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepo;

    /**
     * Constructor for ExpenseService
     * @param expenseRepo - the repository for expenses
     */
    public ExpenseService(ExpenseRepository expenseRepo) {
        this.expenseRepo = expenseRepo;
    }

    /**
     * Get all expenses
     * @return all expenses
     */
    public List<Expense> getAll() {
        return expenseRepo.findAll();
    }
}
