package server.Services;

import commons.Debt;
import commons.Event;
import commons.Participant;
import org.springframework.stereotype.Service;
import server.database.DebtRepository;
import server.database.EventRepository;

import java.util.*;

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
     * Return payment instructions (in the form of debts)
     * associated with a specific event
     * @param eventId - the id of event we retrieve the debts for
     * @return all payment instructions corresponding to the event
     */
    public List<Debt> getPaymentInstructions(long eventId) {
        Optional<Event> event = eventRepo.findById(eventId);
        if(event.isPresent()) {
            List<Debt> debts = debtRepo.findAll().stream().
                filter(x -> event.get().getParticipants().contains(x.getPersonPaying())).toList();
            Map<Participant, Double> participantsDebts = new HashMap<>();
            
            calculateDebtPerParticipant(debts, participantsDebts);
            
            List<Debt> result = new ArrayList<>();
            List<Participant> participants = new ArrayList<>(participantsDebts.keySet());
            while(!participantsDebts.isEmpty()) {
                Participant mostOwing = participants.getFirst();
                Participant mostOwed = participants.getFirst();
                for(Participant participant : participants) {
                    if(participantsDebts.get(participant) > participantsDebts.get(mostOwing)) {
                        mostOwing = participant;
                    }
                    if(participantsDebts.get(participant) < participantsDebts.get(mostOwed)) {
                        mostOwed = participant;
                    }
                }
                double amount = Math.min(participantsDebts.get(mostOwing),
                    - participantsDebts.get(mostOwed));

                settleTransaction(participantsDebts, mostOwed, amount, mostOwing, participants);

                result.add(new Debt(0, mostOwing, mostOwed, amount));
            }
            return result;
        }
        else {
            return null;
        }
    }

    /**
     * Transfers money between the two participants
     * @param participantsDebts - the map in which the money is transferred
     * @param mostOwed - the person to whom is owned the most money
     * @param amount - the amount to be transferred
     * @param mostOwing - the person who is owing the most money
     */
    private void settleTransaction(Map<Participant, Double> participantsDebts,
                                   Participant mostOwed, double amount, Participant mostOwing,
                                   List<Participant> participants) {
        if(participantsDebts.get(mostOwed) + amount == 0) {
            participantsDebts.remove(mostOwed);
            participants.remove(mostOwed);
        }
        else {
            participantsDebts.put(mostOwed, participantsDebts.get(mostOwed) + amount);
        }
        if(participantsDebts.get(mostOwing) - amount == 0) {
            participantsDebts.remove(mostOwing);
            participants.remove(mostOwing);
        }
        else {
            participantsDebts.put(mostOwing, participantsDebts.get(mostOwing) - amount);
        }
    }

    /**
     * Calculates the money owned/owing per participant
     * @param debts - the debts from which we calculate
     * @param participantsDebts - the map with the participants
     * as keys and their money owed/owing as associated values
     */
    private void calculateDebtPerParticipant(List<Debt> debts,
                                        Map<Participant, Double> participantsDebts) {
        for(Debt debt : debts) {
            if(!participantsDebts.containsKey(debt.getPersonOwing())) {
                participantsDebts.put(debt.getPersonOwing(), 0.0);
            }
            participantsDebts.put(debt.getPersonOwing(),
                participantsDebts.get(debt.getPersonOwing()) + debt.getAmount());
            if(!participantsDebts.containsKey(debt.getPersonPaying())) {
                participantsDebts.put(debt.getPersonPaying(), 0.0);
            }
            participantsDebts.put(debt.getPersonPaying(),
                participantsDebts.get(debt.getPersonPaying()) - debt.getAmount());
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
            debt.getPersonOwing() == null || debt.getAmount() <= 0) {
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
