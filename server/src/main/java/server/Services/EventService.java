package server.Services;

import commons.Debt;
import commons.Event;
import commons.Expense;
import commons.Participant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.database.EventRepository;

import server.database.DebtRepository;
import server.database.ExpenseRepository;
import server.database.ParticipantRepository;

import java.util.List;
import java.util.Optional;


@Service
public class EventService {
    private final EventRepository eventRepo;
    private final ParticipantRepository participantRepo;
    private final DebtRepository debtRepo;
    private final ExpenseRepository expenseRepo;

    /**
     * Constructor for EventService
     * @param eventRepo - the repository for events
     * @param participantRepo - the repository for participants
     * @param debtRepo - the repository for debts
     * @param expenseRepo - the repository for expenses
     */
    public EventService(EventRepository eventRepo,ParticipantRepository participantRepo,
                        DebtRepository debtRepo, ExpenseRepository expenseRepo ) {
        this.eventRepo = eventRepo;
        this.participantRepo = participantRepo;
        this.expenseRepo = expenseRepo;
        this.debtRepo = debtRepo;
    }

    /**
     * Get all events
     * @return all events
     */
    public List<Event> findAll() {
        return eventRepo.findAll();
    }

    /**
     * Method to get an event by inviteCode
     * @param inviteCode code to join event
     * @return Either the event or null
     */
    public Optional<Event> findByInviteCode(String inviteCode) {
        return eventRepo.findByInviteCode(inviteCode);
    }

    /**
     * Method to create a new event
     * @param newEvent the event to be created
     * @return the event that was just created
     */

    public Event create(Event newEvent){
        Event savedEvent = eventRepo.save(newEvent);
        return savedEvent;
    }

    /**
     * Method to update an event
     * @param id The id of the event to be updated
     * @param updatedEvent The already updated event object
     * @return A event object which reflects the one that was just persisted
     */
    public Event update(long id, Event updatedEvent){
        Optional<Event> existingEvent = eventRepo.findById(id);
        if (existingEvent.isPresent()){
            existingEvent.get().setTitle(updatedEvent.getTitle());
            existingEvent.get().setInviteCode(updatedEvent.getInviteCode());
            existingEvent.get().setParticipants(updatedEvent.getParticipants());
            existingEvent.get().setExpenses(updatedEvent.getExpenses());
            existingEvent.get().setSettledDebts(updatedEvent.getSettledDebts());
            Event savedEvent = eventRepo.save(existingEvent.get());
            return savedEvent;
        }
        return null;
    }

    /**
     * Method to get an event by id
     * @param id of event
     * @return Either the event or null
     */
    public Optional<Event> findById(long id){
        return eventRepo.findById(id);
    }

    /**
     * Method to delete an event by id
     * @param id of event
     */
    @Transactional
    public void deleteById(long id){
        Optional<Event> optional = eventRepo.findById(id);
        Event event = optional.get();
        List<Participant> participants = event.getParticipants();
        List<Expense> expenses = event.getExpenses();
        List<Debt> debts = event.getSettledDebts();
        event.setSettledDebts(null);
        event.setExpenses(null);
        event.setParticipants(null);
        eventRepo.save(event);
        eventRepo.flush();
        eventRepo.deleteById(id);
        for(Expense expense : expenses){
            List<Debt> debts1 = expense.getDebts();
            expense.setDebts(null);
            expenseRepo.save(expense);
            expenseRepo.deleteById(expense.getId());
            for(Debt debt: debts1){
                debtRepo.deleteById(debt.getId());
            }
        }
        for(Debt debt : debts){
            debtRepo.deleteById(debt.getId());
        }
//        debts = debtRepo.findAll();
//        for(Participant participant : participants){
//            debts = debts.stream().filter(x -> x.getPersonPaying().getId() == participant.getId()
//      || x.getPersonOwing().getId() == participant.getId()).collect(Collectors.toList());
//            for(Debt debt : debts){
//                debtRepo.deleteById(debt.getId());
//            }
//            debts = debtRepo.findAll();
//        }
        for(Participant participant: participants){
            participantRepo.deleteById(participant.getId());
        }
        participantRepo.flush();
        expenseRepo.flush();
        debtRepo.flush();
    }

    /**
     * Method to check if an event exists by id
     * @param id of event
     * @return boolean if event exists or not
     */
    public boolean existsById(long id){
        return eventRepo.existsById(id);
    }

    /**
     * Method to flush the event repository
     */
    public void flush(){
        eventRepo.flush();
    }


}
