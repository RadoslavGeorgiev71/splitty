package client;

import java.io.*;

import java.util.Scanner;

public class ConfigClient {
    private String serverUrl;

    private String email;

    private String iban;

    private String bic;

    private String language;

    private String currency;

    /**
     * Constructor for ConfigClient class
     * @param serverUrl -> Url of the server
     * @param email -> Email of client
     * @param iban -> iban of client
     * @param bic -> bic of client
     * @param language -> currently preferred language of client
     * @param currency -> currently preferred currency of client
     */
    public ConfigClient(String serverUrl, String email, String iban,
                        String bic, String language, String currency){
        this.serverUrl = serverUrl;
        this.email = email;
        this.iban = iban;
        this.bic = bic;
        this.language = language;
        this.currency = currency;
    }

    /**
     * Gets serverUrl
     * @return serverUrl from client
     */
    public String getServerUrl() {
        return serverUrl;
    }

    /**
     *
     * @return email of client
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @return Iban of client
     */
    public String getIban() {
        return iban;
    }

    /**
     *
     * @return bic of client
     */
    public String getBic() {
        return bic;
    }

    /**
     *
     * @return currently preferred language of client
     */
    public String getLanguage() {
        return language;
    }

    /**
     *
     * @return currently preferred currency of client
     */
    public String getCurrency() {
        return currency;
    }

    /**
     *
     * @param serverUrl new serverUrl for client
     */
    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    /**
     *
     * @param email new email for client
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @param iban new iban for client
     */
    public void setIban(String iban) {
        this.iban = iban;
    }

    /**
     *
     * @param bic new bic for client
     */
    public void setBic(String bic) {
        this.bic = bic;
    }

    /**
     *
     * @param language new language for client
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     *
     * @param currency new currency for client
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * method that reads a file and creates a new ConfigClient in accordance to it.
     * In my opinion this should not be placed in the ConfigClient class though,
     * it should be part of the Client class itself.
     * For now, I'll leave it here until we fully decide what
     * to do with it as a team.
     * @param path path to the file being read
     * @return null if file not found, new ConfigClient according to specifications if found
     */
    public ConfigClient readFromFile(String path){
        try {
            File config = new File(path);
            Scanner configParse = new Scanner(config);
            String[] newClient = new String[6];
            int counter = 0;
            while (configParse.hasNextLine()){
                String[] data = configParse.nextLine().split(": ");
                newClient[counter] = data[1];
                counter++;
            }
            configParse.close();
            return new ConfigClient(newClient[0], newClient[1],
                    newClient[2], newClient[3], newClient[4], newClient[5]);

        } catch (FileNotFoundException e){
            System.out.println("Error reading from file: " + e.getMessage());
            return null;
        }
    }

    /**
     * method for writing config to a file at a certain path
     * it should be pretty self-explanatory,
     * creates a BufferedWriter based on file path, then writes every single element of
     * configContent there in the correct format.
     * Params can be replaced with a map of key-value pairs instead of 2 arrays.
     * @param path the path where config should be written
     * @param configContent the content of the config passed as a
     *                      String array for easy writer handling
     * @param keys this is just the different words in the config file
     *             for example the 'email' in email: configContent[1]
     *             this helps with actually writing the file.
     */
    public void writeToFile(String path, String[] configContent, String[] keys){

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (int i = 0; i < keys.length; i++) {
                writer.write(keys[i] + ": " + configContent[i]);
                writer.newLine();
            }
        } catch (IOException e){
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}
