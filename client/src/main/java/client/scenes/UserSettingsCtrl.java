package client.scenes;

import client.utils.ConfigClient;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class UserSettingsCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private ConfigClient configClient;

    private Path filePath = Paths.get("src/main/resources/config.txt").toAbsolutePath();

//    private String[] keys = {"serverUrl", "email", "iban", "bic", "language",
//            "currency", "name", "recentEvents"};

    @FXML
    private ChoiceBox currencyMenu;

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField ibanField;

    @FXML
    private TextField bicField;


    /**
     *
     * @param server
     * @param mainCtrl
     * @param configClient
     */
    @Inject
    public UserSettingsCtrl(ServerUtils server, MainCtrl mainCtrl, ConfigClient configClient) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.configClient = configClient;
    }

    /**
     * Cancels the changes
     */
    @FXML
    public void onCancelClick() {
        clearFields();
        mainCtrl.showStartScreen();
    }

    /**
     * Changes the User Config according to the inputs from the fields
     */
    @FXML
    public void onConfirmClick() {
        String[] keys = new String[7];
        keys[0] = "serverUrl";
        keys[1] = "email";
        keys[2] = "iban";
        keys[3] = "bic";
        keys[4] = "language";
        keys[5] = "currency";
        keys[6] = "name";

        if(configClient != null) {
            String[] configContent = new String[7];
            configContent[0] = configClient.getServerUrl();
            configContent[1] = emailField.getText();
            configContent[2] = ibanField.getText();
            configContent[3] = bicField.getText();
            configContent[4] = configClient.getLanguage();
            configContent[5] = currencyMenu.getValue().toString();
            configContent[6] = nameField.getText();
            configClient.writeToFile(String.valueOf(filePath), configContent, keys);
            System.out.println("changes confirmed");
        }

        clearFields();
        mainCtrl.showStartScreen();
    }

    /**
     * Clears all the fields
     */
    private void clearFields() {
        nameField.clear();
        emailField.clear();
        ibanField.clear();
        bicField.clear();
        currencyMenu.getSelectionModel().selectFirst();
    }

    /**
     * Initializes the fields with the correct values (if available)
     * @param configClient
     */
    public void initialize(ConfigClient configClient) {
        if(configClient.getName() != null) {
            nameField.setText(configClient.getName());
        }

        if(configClient.getEmail() != null) {
            emailField.setText(configClient.getEmail());
        }

        if(configClient.getIban() != null) {
            ibanField.setText(configClient.getIban());
        }

        if(configClient.getBic() != null) {
            bicField.setText(configClient.getBic());
        }

        // Placeholder for now, there's probably a better way to do this
        List<String> currencies = new ArrayList<>();
        currencies.add("EUR");
        currencies.add("USD");
        currencies.add("AUD");
        currencyMenu.setItems(FXCollections.observableArrayList(currencies));
        currencyMenu.getSelectionModel().selectFirst();
    }

}
