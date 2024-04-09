package server.Services;

import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.Repositories.TestParticipantRepository;
import server.database.ParticipantRepository;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

public class ParticipantServiceTest {

    @Mock
    private ParticipantRepository participantRepo;
    private ParticipantService sut;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        participantRepo = new TestParticipantRepository();
        sut = new ParticipantService(participantRepo, null, null);
    }


    @Test
    void testConstructor() {
        assertNotNull(sut);
    }

    @Test
    void testGetAll() {
        Participant p1 = new Participant("Bob");
        Participant p2 = new Participant("Ana");
        participantRepo.save(p1);
        participantRepo.save(p2);
        List<Participant> participantsSaved = sut.getAll();
        List<Participant> participants = List.of(p1, p2);
        assertEquals(participants, participantsSaved);
    }

    @Test
    void testUpdate() {
        Participant p1 = new Participant("Bob");
        Participant p2 = new Participant("Ana");
        Participant participantSaved = participantRepo.save(p1);
        Participant returnedParticipant = sut.update(participantSaved.getId(), p2);
        assertEquals(returnedParticipant, p2);
        Optional<Participant> participantFound = participantRepo.findById(returnedParticipant.getId());
        assertTrue(participantFound.isPresent());
        assertEquals(p2, participantFound.get());
    }

    @Test
    void testUpdateNull() {
        Participant p1 = new Participant("Bob");
        Participant returnedParticipant = sut.update(0, p1);
        assertNull(returnedParticipant);
    }

    @Test
    void testCreate() {
        Participant p1 = new Participant("Bob");
        Participant savedParticipant = sut.create(p1);
        assertEquals(p1, savedParticipant);
        List<Participant> participants = participantRepo.findAll();
        assertEquals(participants, List.of(p1));
    }

    @Test
    void testDelete() {
        Participant p1 = new Participant("Bob");
        Participant participantSaved = participantRepo.save(p1);
        assertTrue(sut.delete(participantSaved.getId()));
        assertFalse(sut.delete(participantSaved.getId()));
    }

    @Test
    void testGetById() {
        Participant p1 = new Participant("Bob");
        Participant participantSaved = participantRepo.save(p1);
        Optional<Participant> participantOptional = sut.getById(participantSaved.getId());
        assertTrue(participantOptional.isPresent());
        assertEquals(participantOptional.get(), participantSaved);
    }
}
