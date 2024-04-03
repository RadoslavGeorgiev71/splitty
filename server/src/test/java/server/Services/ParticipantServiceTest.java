package server.Services;

import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
<<<<<<< HEAD
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.database.ParticipantRepository;

import java.util.Arrays;
=======
import org.mockito.Mock;
import server.Repositories.TestParticipantRepository;
import server.database.ParticipantRepository;

>>>>>>> f22dd26a22f878be5a4815c14cc5931d61b4c59f
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
<<<<<<< HEAD
import static org.mockito.Mockito.*;

public class ParticipantServiceTest {

    @Mock
    private ParticipantRepository participantRepo;

    @InjectMocks
    private ParticipantService sut;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testParticipantServiceConstructor() {
=======

public class ParticipantServiceTest {
    @Mock
    private ParticipantRepository participantRepo;
    private ParticipantService sut;

    @BeforeEach
    void participantServiceSetUp() {
        participantRepo = new TestParticipantRepository();
        sut = new ParticipantService(participantRepo);
    }

    @Test
    void testConstructor() {
>>>>>>> f22dd26a22f878be5a4815c14cc5931d61b4c59f
        assertNotNull(sut);
    }

    @Test
<<<<<<< HEAD
    void testFindAll() {
        Participant participant = new Participant();
        participant.setId(1);
        when(participantRepo.findAll()).thenReturn(Arrays.asList(participant));
        List<Participant> participants = sut.getAll();
        assertNotNull(participants);
        assertEquals(1, participants.size());
        assertEquals(participant, participants.get(0));
    }

    @Test
    void testCreateParticipant() {
        Participant participant = new Participant();
        participant.setId(1);
        when(participantRepo.save(any(Participant.class))).thenReturn(participant);
        Participant result = sut.create(participant);
        assertNotNull(participant);
        assertEquals(1, result.getId());
        verify(participantRepo, times(1)).save(participant);
    }

=======
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
>>>>>>> f22dd26a22f878be5a4815c14cc5931d61b4c59f
}
