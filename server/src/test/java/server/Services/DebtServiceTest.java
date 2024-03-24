package server.Services;

import commons.Debt;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import server.Repositories.TestDebtRepository;
import server.Repositories.TestEventRepository;
import server.database.DebtRepository;
import server.database.EventRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DebtServiceTest {
    @Mock
    private DebtRepository debtRepo;
    @Mock
    private EventRepository eventRepo;
    private DebtService sut;

    @BeforeEach
    void DebtServiceSetUp() {
        debtRepo = new TestDebtRepository();
        eventRepo = new TestEventRepository();
        sut =  new DebtService(debtRepo, eventRepo);
    }
    @Test
    void testDebtServiceConstructor() {
        assertNotNull(sut);
    }
    @Test
    void testRemoveAllDebts() {
        List<Debt> debts = debtRepo.findAll();
        for(Debt debt : debts) {
            debtRepo.deleteById(debt.getId());
        }
        List<Debt> noDebts = debtRepo.findAll();
        assertEquals(noDebts.size(), 0);
    }

    @Test
    void testGetAll() {
        List<Debt> debts = debtRepo.findAll();
        for(Debt debt : debts) {
            sut.delete(debt.getId());
        }
        Debt debt1 = new Debt(new Participant("Ana"), new Participant("Bob"), 50);
        Debt debt2 = new Debt(new Participant("Bob"), new Participant("Ana"), 5);
        sut.add(debt1);
        sut.add(debt2);
        debts = debtRepo.findAll();
        assertTrue(debts.contains(debt1));
        assertTrue(debts.contains(debt2));
        assertEquals(debts.size(), 2);
    }

    @Test
    void testGetPaymentInstructions() {

    }

    @Test
    void testAddDebt() {
        Debt debt = new Debt(new Participant("Ana"), new Participant("Bob"), 500);
        sut.add(debt);
        List<Debt> debts = debtRepo.findAll();
        assertTrue(debts.contains(debt));
    }

    @Test
    void testDeleteDebt() {
        Debt debt = new Debt(new Participant("Ana"), new Participant("Bob"), 50);
        List<Debt> debts = debtRepo.findAll();
        assertFalse(debts.contains(debt));
        debtRepo.save(debt);
        debts = debtRepo.findAll();
        assertTrue(debts.contains(debt));
        sut.delete(debts.getFirst().getId());
        debts = debtRepo.findAll();
        assertFalse(debts.contains(debt));
    }

    @Test
    void testGetById() {
        Debt debt = new Debt(new Participant("Ana"), new Participant("Bob"), 500);
        sut.add(debt);
        long debtId = debtRepo.findAll().getFirst().getId();
        assertNull(sut.getById(-5));
        assertNull(sut.getById(1));
        assertEquals(debt, sut.getById(debtId));
    }
}
