package server.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSendEmail() {
        // Given
        String to = "recipient@example.com";
        String subject = "Test Subject";
        String body = "Test Body";

        // When
        emailService.sendEmail(to, subject, body);

        // Then
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}