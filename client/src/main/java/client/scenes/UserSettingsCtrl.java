package client.scenes;

import client.utils.ConfigClient;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class UserSettingsCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private ConfigClient configClient;

    private String[] keys = {"serverUrl", "email", "iban",
                             "bic", "language", "currency", "name", "recentEvents"};


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
    @FXML
    private TextField serverURLField;


    /**
     * @param server
     * @param mainCtrl
     * @param configClient
     */
    @Inject
    public UserSettingsCtrl(ServerUtils server, MainCtrl mainCtrl, ConfigClient configClient) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.configClient = new ConfigClient();
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
        configClient.setName(nameField.getText());
        configClient.setEmail(emailField.getText());
        configClient.setIban(ibanField.getText());
        configClient.setBic(bicField.getText());
        configClient.setServerUrl(serverURLField.getText());
        ServerUtils.setURL(serverURLField.getText());
        configClient.setCurrency(currencyMenu.getSelectionModel().getSelectedItem().toString());
        String[] contents = {configClient.getServerUrl(), configClient.getEmail(),
                configClient.getIban(), configClient.getBic(),
                configClient.getLanguage(), configClient.getCurrency(),
                configClient.getName(), configClient.getRecentEvents()};
        configClient.writeToFile("client/src/main/resources/config.txt", contents, keys);
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
        serverURLField.clear();
    }

    /**
     * Initializes the fields with the correct values (if available)
     */
    public void initialize() {
        if (configClient.getName() != null) {
            nameField.setText(configClient.getName());
        }

        if (configClient.getEmail() != null) {
            emailField.setText(configClient.getEmail());
        }

        if (configClient.getIban() != null) {
            ibanField.setText(configClient.getIban());
        }

        if (configClient.getBic() != null) {
            bicField.setText(configClient.getBic());
        }

        if (configClient.getServerUrl() != null) {
            serverURLField.setText(configClient.getServerUrl());
        }
        initializeChoiceBox();
    }

    private void initializeChoiceBox() {

        List<String> currencies = new ArrayList<>();
        currencies.add("EUR");
        currencies.add("USD");
        currencies.add("AUD");
        currencyMenu.setItems(FXCollections.observableArrayList(currencies));
        if (configClient.getCurrency() != null && currencies.contains(configClient.getCurrency())) {
            currencyMenu.getSelectionModel().select(configClient.getCurrency());
        } else {
            currencyMenu.getSelectionModel().selectFirst();
        }
    }

}
