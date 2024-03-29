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
     */
    @Inject
    public UserSettingsCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
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
    }

    /**
     * Initializes the fields with the correct values (if available)
     * @param configClient
     */
    public void initialize(ConfigClient configClient) {
        this.configClient = configClient;
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
