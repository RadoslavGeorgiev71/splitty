package server.api;

import commons.Participant;
//import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.ParticipantRepository;

import java.util.List;

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

    /**TODO method that creates a new participant
     * method that deletes participant
     * method that updates participant
     * method that retrieves participant by id
     */
}