import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.messaging.simp.stomp.StompSession;
import utils.Admin;

class AdminTest {

    @Mock
    Client mockClient;

    @Mock
    WebTarget mockWebTarget;

    @Mock
    Invocation.Builder mockBuilder;

    @Mock
    Response mockResponse;
    @Mock
    StompSession mockStompSession;

    Admin admin;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        admin = new Admin("http://mocked-server/", mockStompSession);
    }

    @Test
    void setPassword() {
        admin.setPassword("123");
        assertEquals("123", admin.getPassword());
    }

    @Test
    void getPassword() {
        assertEquals(admin.getPassword(), "none set");
    }

    @Test
    void setURL() {
        admin.setURL("http://www.google.com");
        assertEquals("http://www.google.com", admin.getURL());
    }

    @Test
    void getURL() {
        assertEquals("http://mocked-server/", admin.getURL());
    }

    @Test
    public void testGeneratePassword_Success() {
        // Set up expectations for the mock server
        new MockServerClient("localhost", 8080)
                .when(
                        HttpRequest.request()
                                .withMethod("GET")
                                .withPath("/api/admin/")
                )
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                );

        // Call the method to be tested
        boolean result = admin.generatePassword();

        // Assert that the method returns true on success
        assertTrue(result);
    }
}