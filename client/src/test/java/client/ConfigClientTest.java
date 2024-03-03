package client;

import client.utils.ConfigClient;
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

    @Test
    void setterAndGetter() {
        assertEquals(expected_config[0], configClient.getServerUrl());
        assertEquals(expected_config[1], configClient.getEmail());
        assertEquals(expected_config[2], configClient.getIban());
        assertEquals(expected_config[3], configClient.getBic());
        assertEquals(expected_config[4], configClient.getLanguage());
        assertEquals(expected_config[5], configClient.getCurrency());

        configClient.setServerUrl("1");
        configClient.setEmail("2");
        configClient.setIban("3");
        configClient.setBic("4");
        configClient.setLanguage("5");
        configClient.setCurrency("6");

        assertEquals("1", configClient.getServerUrl());
        assertEquals("2", configClient.getEmail());
        assertEquals("3", configClient.getIban());
        assertEquals("4", configClient.getBic());
        assertEquals("5", configClient.getLanguage());
        assertEquals("6", configClient.getCurrency());
    }
}