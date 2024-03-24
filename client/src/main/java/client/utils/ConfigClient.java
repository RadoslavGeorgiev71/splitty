package client.utils;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class ConfigClient {
    private String serverUrl;

    private String email;

    private String iban;

    private String bic;

    private String language;

    private String currency;

    private String name;

    private String recentEvents;

    /**
     * Default constructor just in case.
     */
    public ConfigClient() {

    }

    /**
     * Constructor for ConfigClient class
     *
     * @param serverUrl    -> Url of the server
     * @param email        -> Email of client
     * @param iban         -> iban of client
     * @param bic          -> bic of client
     * @param language     -> currently preferred language of client
     * @param currency     -> currently preferred currency of client
     * @param name         -> name of client
     * @param recentEvents -> recently viewed events of client
     */
    public ConfigClient(String serverUrl, String email, String iban,
                        String bic, String language, String currency,
                        String name, String recentEvents) {
        this.serverUrl = serverUrl;
        this.email = email;
        this.iban = iban;
        this.bic = bic;
        this.language = language;
        this.currency = currency;
        this.name = name;
        this.recentEvents = recentEvents;
    }

    /**
     * Gets serverUrl
     *
     * @return serverUrl from client
     */
    public String getServerUrl() {
        return serverUrl;
    }

    /**
     * @return email of client
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return Iban of client
     */
    public String getIban() {
        return iban;
    }

    /**
     * @return bic of client
     */
    public String getBic() {
        return bic;
    }

    /**
     * @return currently preferred language of client
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @return currently preferred currency of client
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * @param serverUrl new serverUrl for client
     */
    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    /**
     * @param email new email for client
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @param iban new iban for client
     */
    public void setIban(String iban) {
        this.iban = iban;
    }

    /**
     * @param bic new bic for client
     */
    public void setBic(String bic) {
        this.bic = bic;
    }

    /**
     * @param language new language for client
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @param currency new currency for client
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * Getter for the name of the client
     *
     * @return name of the client
     */

    public String getName() {
        return name;
    }

    /**
     * Setter for the name of the client
     *
     * @param name name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the recent events of the client
     *
     * @return recent events of the client
     */

    public String getRecentEvents() {
        return recentEvents;
    }

    /**
     * Setter for the recent events of the client
     *
     * @param recentEvents recent events to set
     */

    public void setRecentEvents(String recentEvents) {
        this.recentEvents = recentEvents;
    }

    /**
     * method that reads a file and creates a new ConfigClient in accordance to it.
     *
     * @param path path to the file being read
     * @return null if file not found, new ConfigClient according to specifications if found
     */
    public ConfigClient readFromFile(String path) {
        try {
            File config = new File(path);
            Scanner configParse = new Scanner(config);
            String[] newClient = new String[8];
            int counter = 0;
            while (configParse.hasNextLine()) {
                String[] data = configParse.nextLine().split(": ");
                if (data.length < 2) {
                    newClient[counter] = "";
                    counter++;
                } else {
                    newClient[counter] = data[1];
                    counter++;
                }
            }
            configParse.close();
            return new ConfigClient(newClient[0], newClient[1],
                    newClient[2], newClient[3], newClient[4], newClient[5],
                    newClient[6], newClient[7]);

        } catch (FileNotFoundException e) { // if the file is not found it should
            try {
                // Get the absolute path of the resources folder
                String filepath = "client/src/main/resources/config.txt";
                Path filePath = Paths.get(filepath);
                // Create the file
                Files.createFile(filePath);
            } catch (IOException ioException) {
                System.out.println("An error occurred: " + ioException.getMessage());
                ioException.printStackTrace();
            }
            return new ConfigClient();
        }
    }

    /**
     * method for writing config to a file at a certain path
     * it should be pretty self-explanatory,
     * creates a BufferedWriter based on file path, then writes every single element of
     * configContent there in the correct format.
     * Params can be replaced with a map of key-value pairs instead of 2 arrays.
     *
     * @param path          the path where config should be written
     * @param configContent the content of the config passed as a
     *                      String array for easy writer handling
     * @param keys          this is just the different words in the config file
     *                      for example the 'email' in email: configContent[1]
     *                      this helps with actually writing the file.
     */
    public void writeToFile(String path, String[] configContent, String[] keys) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (int i = 0; i < keys.length; i++) {
                writer.write(keys[i] + ": " + configContent[i]);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}
