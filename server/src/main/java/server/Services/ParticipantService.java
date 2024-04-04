package server.Services;

import commons.Participant;
import org.springframework.stereotype.Service;
import server.database.ParticipantRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ParticipantService {
    private final ParticipantRepository participantRepo;

    /**
     * Constructor for ParticipantService
     * @param participantRepo - the repository for participants
     */
    public ParticipantService(ParticipantRepository participantRepo) {
        this.participantRepo = participantRepo;
    }

    /**
     * Get all participants
     * @return all participants
     */
    public List<Participant> getAll() {
        return participantRepo.findAll();
    }

    /**
     * Updates a participants in the database iff it exists
     * @param id The id of the participant to be updated
     * @param updatedParticipant The already updated participant object
     * @return A participant object which reflects the one that was just persisted
     */
    public Participant update(long id, Participant updatedParticipant){
        Optional<Participant> existingParticipant = participantRepo.findById(id);
        if (existingParticipant.isPresent()){
            existingParticipant.get().setName(updatedParticipant.getName());
            existingParticipant.get().setEmail(updatedParticipant.getEmail());
            existingParticipant.get().setBic(updatedParticipant.getBic());
            existingParticipant.get().setIban(updatedParticipant.getIban());
            Participant savedParticipant = participantRepo.save(existingParticipant.get());
            return savedParticipant;
        }
        return null;
    }

    /**
     * Creates a new participant in the database
     * @param newParticipant The participant to be created
     * @return A participant object which reflects the one that was just created
     */
    public Participant create(Participant newParticipant){
        Participant savedParticipant = participantRepo.save(newParticipant);
        return savedParticipant;
    }

    /**
     * Deletes a participant if it exists in the database
     * @param participantId the id of the participant to be deleted
     * @return boolean if participant was deleted or not
     */
    public boolean delete(long participantId){
        if (participantRepo.existsById(participantId)){
            participantRepo.deleteById(participantId);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns optional Participant by its id
     * @param participantId id of the participant to be fetched
     * @return An optional Participant, which will be present if found in the database
     */
    public Optional<Participant> getById(long participantId){
        Optional<Participant> participantOptional = participantRepo.findById(participantId);
        return participantOptional;
    }

    public void flush() {
        participantRepo.flush();
    }
}
