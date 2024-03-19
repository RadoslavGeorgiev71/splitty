package server.api;

import commons.Participant;
//import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.ParticipantRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/participants")
public class ParticipantController {
    private final ParticipantRepository repo;

    /**
     * Constructor for the controller
     * @param repo - type ParticipantRepository which extends JpaRepository
     */
    public ParticipantController(ParticipantRepository repo) {
        this.repo = repo;
    }

    /**
     * Get all participants
     * @return all participants
     */
    @GetMapping(path = {"", "/"})
    public List<Participant> getAll() {
        return repo.findAll();
    }

    /**
     * Update a participant
     * @param updatedParticipant
     * @return all participants
     */
    @PutMapping(path = {"", "/"})
    public ResponseEntity<?> update(@RequestBody Participant updatedParticipant) {

        long participantId = updatedParticipant.getId(); // Assuming Participant has an ID field

        // Retrieve the existing participant from the database based on the ID
        Optional<Participant> existingParticipant = repo.findById(participantId);

        if (existingParticipant.isPresent()){
            // Update the existing participant with data from updatedParticipant
            existingParticipant.get().setName(updatedParticipant.getName());
            existingParticipant.get().setEmail(updatedParticipant.getEmail());
            existingParticipant.get().setBic(updatedParticipant.getBic());
            existingParticipant.get().setIban(updatedParticipant.getIban());
            // Update other fields as needed

            // Save the updated participant back to the database
            Participant savedParticipant = repo.save(existingParticipant.get());

            // Return the updated participant
            return ResponseEntity.ok(savedParticipant);
        }
        //Maybe here it should add the participant in the database
        //instead of returning a bad request message
        //To be discussed
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Participant not found");
    }

    /**
     *
     * @param newParticipant
     * @return the new participant
     */
    @PostMapping(path = {"", "/"})
    public ResponseEntity<?> create(@RequestBody Participant newParticipant){
        Participant savedParticipant = repo.save(newParticipant);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedParticipant);
    }

    /**
     *
     * @param participantId
     * @return if participant was deleted or not
     */
    @DeleteMapping(path = {"", "/{id}"})
    public ResponseEntity<?> delete(@PathVariable("id") long participantId){
        if (repo.existsById(participantId)){
            repo.deleteById(participantId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Participant not found!");
        }
    }

    /**
     *
     * @param participantId
     * @return participant with the id or "No participant by id"
     */
    @GetMapping(path = {"/{id}"})
    public ResponseEntity<?> getById(@PathVariable("id") long participantId){
        Optional<Participant> participantOptional = repo.findById(participantId);

        if (participantOptional.isPresent()){
            return ResponseEntity.ok(participantOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No participant by id");
        }
    }

    /**TODO  check the method that creates a new participant -> create
     * check method that deletes participant -> delete
     * check method that updates participant -> update
     * check method that retrieves participant by id -> getById
     */
}