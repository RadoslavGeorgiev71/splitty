package server.Controllers;

import commons.Expense;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.Repositories.TestEventRepository;
import server.Repositories.TestExpenseRepository;
import server.Services.ExpenseService;
import server.api.ExpenseController;
import server.database.EventRepository;
import server.database.ExpenseRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestExpenseController {
    @Mock
    private ExpenseRepository expenseRepo;
    @Mock
    private EventRepository eventRepo;
    @Mock
    private ExpenseService expenseService;
    private ExpenseController sut;

    @BeforeEach
    void participantControllerSetUp() {
        eventRepo = new TestEventRepository();
        expenseRepo = new TestExpenseRepository();
        expenseService = new ExpenseService(expenseRepo, eventRepo);
        sut = new ExpenseController(expenseService);
    }

    @Test
    void testConstructor() {
        assertNotNull(sut);
    }

    @Test
    void testGetAll() {
        Expense e1 = new Expense("e1", null,
        10.0, null);
        Expense e2 = new Expense("e2", null,
                20.0, null);
        List<Expense> events = new ArrayList<>();
        List<Expense> eventsSaved = sut.getAll();
        assertEquals(events, eventsSaved);
        expenseService.create(e1);
        expenseService.create(e2);
        events.add(e1);
        events.add(e2);
        eventsSaved = sut.getAll();
        assertEquals(eventsSaved, events);
    }

    @Test
    void testGetById() {
        Expense e1 = new Expense("e1", null,
                10.0, null);
        Expense expenseSaved = expenseService.create(e1);
        ResponseEntity<?> response = sut.getExpense(expenseSaved.getId());
        assertEquals(response.getBody(), expenseSaved);
    }

    @Test
    void testGetByIdNonExistent() {
        ResponseEntity<?> response = sut.getExpense(0);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertNull(response.getBody());
    }

    @Test
    void testCreate() {
        Expense e1 = new Expense("e1", null,
                10.0, null);
        ResponseEntity<?> response = sut.createExpense(e1);
        assertEquals(response.getBody(), e1);
        List<Expense> expenses = expenseService.getAll();
        assertEquals(expenses, List.of(e1));
    }

    @Test
    void testDelete() {
        Expense e1 = new Expense("e1", null,
                10.0, null);
        Expense expenseSaved = expenseService.create(e1);
        ResponseEntity<?> response = sut.delete(expenseSaved.getId());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        List<Expense> expensesFound = expenseService.getAll();
        assertEquals(expensesFound, new ArrayList<>());
    }

    @Test
    void testDeleteNonExistent() {
        ResponseEntity<?> response = sut.delete(0);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertNull(response.getBody());
    }

    @Test
    void testUpdateNonExistent() {
        ResponseEntity<?> response = sut.update(0, null);
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        assertEquals(response.getBody(), "Expense not found");
    }

    @Test
    void testUpdate() {
        Expense e1 = new Expense("e1", null,
                10.0, null);
        Expense e2 = new Expense("e2", null,
                20.0, null);
        sut.createExpense(e1);
        sut.update(e1.getId(), e2);
        List<Expense> expenses = expenseService.getAll();
        assertEquals(expenses.size(), 2);
        assertEquals(expenses.get(1), e2);
    }
}