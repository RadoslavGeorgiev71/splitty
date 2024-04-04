package server.Services;

import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.database.ParticipantRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
        assertNotNull(sut);
    }

    @Test
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

    @Test
    void testFlush() {
        sut.flush();
        verify(participantRepo, times(1)).flush();
    }

}
