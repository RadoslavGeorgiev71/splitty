package server.Controllers;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import server.Services.ParticipantService;
import server.api.ParticipantController;
import server.database.ParticipantRepository;

public class TestParticipantController {
    @Mock
    private ParticipantRepository participantRepo;
    @Mock
    private ParticipantService participantService;
    private ParticipantController sut;

    @BeforeEach
    void participantControllerSetUp() {

    }
}
