package server.Services;

import commons.Event;
import commons.Expense;
import org.springframework.stereotype.Service;
import server.database.EventRepository;
import server.database.ExpenseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepo;
    private final EventRepository eventRepo;

    /**
     * Constructor for ExpenseService
     * @param expenseRepo - the repository for expenses
     * @param eventRepo - the repository for events
     */
    public ExpenseService(ExpenseRepository expenseRepo, EventRepository eventRepo) {
        this.expenseRepo = expenseRepo;
        this.eventRepo = eventRepo;
    }

    /**
     * Get all expenses
     * @return all expenses
     */
    public List<Expense> getAll() {
        return expenseRepo.findAll();
    }

    /**
     * Method to create a new expense
     * @param newExpense the expense to be created
     * @return the expense that was just created
     */
    public Expense create(Expense newExpense){
        return expenseRepo.save(newExpense);
    }

    /**
     * Method to create a new expense
     * @param id The id of the event
     * @param newExpense the expense to be created
     * @return the expense that was just created
     */
    public Expense create(long id, Expense newExpense){
        Optional<Event> existingEvent = eventRepo.findById(id);
        if (existingEvent.isPresent()){
            Expense savedExpense = expenseRepo.save(newExpense);
            List<Expense> addedExpense = existingEvent.get().getExpenses();
            addedExpense.add(newExpense);
            existingEvent.get().setExpenses(addedExpense);
            eventRepo.save(existingEvent.get());
            return savedExpense;
        }
        return null;
    }

    /**
     * Method to update an expense
     * @param id The id of the expense to be updated
     * @param updatedExpense The already updated expense object
     * @return A expense object which reflects the one that was just persisted
     */
    public Expense update(long id, Expense updatedExpense){
        Optional<Expense> existingExpense = expenseRepo.findById(id);
        if (existingExpense.isPresent()){
            existingExpense.get().setTitle(updatedExpense.getTitle());
            existingExpense.get().setPayingParticipant(updatedExpense.getPayingParticipant());
            existingExpense.get().setAmount(updatedExpense.getAmount());
            existingExpense.get().setParticipants(updatedExpense.getParticipants());
            existingExpense.get().setDateTime(updatedExpense.getDateTime());
            return expenseRepo.save(existingExpense.get());
        }
        return null;
    }

    /**
     * Method to get an expense by id
     * @param id of expense
     * @return Either the expense or null
     */
    public Optional<Expense> findById(long id){
        return expenseRepo.findById(id);
    }

    /**
     * Method to delete an expense by id
     * @param id of expense
     */
    public void deleteById(long id){
        expenseRepo.deleteById(id);
    }

    /**
     * Method to check if an expense exists by id
     * @param id of expense
     * @return boolean if expense exists or not
     */
    public boolean existsById(long id){
        return expenseRepo.existsById(id);
    }

    /**
     * Method to flush the event repository
     */
    public void flush(){
        eventRepo.flush();
        expenseRepo.flush();
    }

    /**
     * Method to get expenses by event id
     * @param id of event
     * @return Either the expenses or null
     */
    public List<Expense> findByEventId(long id){
        Optional<Event> existingEvent = eventRepo.findById(id);
        if(existingEvent.isPresent()){
            return existingEvent.get().getExpenses();
        }
        return new ArrayList<>();
    }
}
