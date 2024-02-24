package client;

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
}
