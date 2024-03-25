package server.Services;

import commons.Event;
import org.springframework.stereotype.Service;
import server.database.EventRepository;

import java.util.List;
import java.util.Optional;


@Service
public class EventService {
    private final EventRepository eventRepo;

    /**
     * Constructor for EventService
     * @param eventRepo - the repository for events
     */
    public EventService(EventRepository eventRepo) {
        this.eventRepo = eventRepo;
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
    public void deleteById(long id){
        eventRepo.deleteById(id);
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
