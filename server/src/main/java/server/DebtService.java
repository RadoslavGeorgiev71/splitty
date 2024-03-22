package server;

import commons.Debt;
import commons.Event;
import org.springframework.stereotype.Service;
import server.database.DebtRepository;
import server.database.EventRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DebtService {
    private final DebtRepository debtRepo;
    private final EventRepository eventRepo;

    /**
     * Constructor for DebtService
     * @param debtRepo - the repository for debts
     * @param eventRepo - the repository for events
     */
    public DebtService(DebtRepository debtRepo, EventRepository eventRepo) {
        this.debtRepo = debtRepo;
        this.eventRepo = eventRepo;
    }

    /**
     * Get all debts
     * @return all debts
     */
    public List<Debt> getAll() {
        return debtRepo.findAll();
    }

    /**
     * Return all debts associated with a specific event
     * @param eventId - the id of event we retrieve the debts for
     * @return all debts corresponding to the event
     */
    public List<Debt> getDebtsForEvent(long eventId) {
        Optional<Event> event = eventRepo.findById(eventId);
        if(event.isPresent()) {
            return debtRepo.findAll().stream().
                filter(x -> event.get().getParticipants().contains(x.getPersonPaying())).toList();
        }
        else {
            return null;
        }
    }

    /**
     * Save a new debt if it is valid and returns a response
     * @param debt - the debt to be saved
     * @return the debt if the debt was added,
     * null otherwise
     */
    public Debt add(Debt debt) {
        if (debt.getPersonPaying() == null ||
            debt.getPersonOwed() == null || debt.isPaid()) {
            return null;
        }
        return debtRepo.save(debt);
    }

    /**
     * Deletes a debt by given id
     * @param id - the id of the debt we delete
     * @return the debt if it was deleted successfully,
     * null otherwise
     */
    public Debt delete(long id) {
        if (!debtRepo.existsById(id)) {
            return null;
        }
        Debt debt = debtRepo.findById(id).get();
        debtRepo.deleteById(id);
        debtRepo.flush();
        return debt;
    }

    /**
     * Returns a debt by its id if it exists
     * @param id - the id to be searched with
     * @return the debt if found successfully,
     * null otherwise
     */
    public Debt getById(long id) {
        if (id < 0 || !debtRepo.existsById(id)) {
            return null;
        }
        return debtRepo.findById(id).get();
    }
}
