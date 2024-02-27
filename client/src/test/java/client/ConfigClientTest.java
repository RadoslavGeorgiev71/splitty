package client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigClientTest {

    private final String TEST_FILE_PATH = "src/test/resources/test_config";
    private final String[] expected_config = {"generic","blabla@gmail.com", "1234567890", "BANK123", "en", "EUR"};
    private final String[] keys = {"serverUrl", "email", "iban", "bic", "language", "currency"};
    private ConfigClient configClient = new ConfigClient(expected_config[0], expected_config[1],
            expected_config[2], expected_config[3], expected_config[4], expected_config[5]);

    @Test
    void readFromFile() {
        ConfigClient result = configClient.readFromFile(TEST_FILE_PATH);
        assertNotNull(result);
        assertEquals(expected_config[0], result.getServerUrl());
        assertEquals(expected_config[1], result.getEmail());
        assertEquals(expected_config[2], result.getIban());
        assertEquals(expected_config[3], result.getBic());
        assertEquals(expected_config[4], result.getLanguage());
        assertEquals(expected_config[5], result.getCurrency());
    }

    @Test
    void writeToFile() {
        configClient.writeToFile(TEST_FILE_PATH, expected_config, keys);

        ConfigClient result = configClient.readFromFile(TEST_FILE_PATH);


        assertNotNull(result);


        assertEquals(expected_config[0], result.getServerUrl());
        assertEquals(expected_config[1], result.getEmail());
        assertEquals(expected_config[2], result.getIban());
        assertEquals(expected_config[3], result.getBic());
        assertEquals(expected_config[4], result.getLanguage());
        assertEquals(expected_config[5], result.getCurrency());
    }
}