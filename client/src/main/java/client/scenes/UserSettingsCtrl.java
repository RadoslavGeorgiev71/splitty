package client.scenes;

import client.utils.ConfigClient;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.List;

public class UserSettingsCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private ConfigClient configClient;

    private String[] keys =
            {"serverUrl", "email", "iban", "bic", "language", "currency", "name", "recentEvents"};


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
     */
    @Inject
    public UserSettingsCtrl(ServerUtils server, MainCtrl mainCtrl) {
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

        if (configClient.getName() != null && !configClient.getName().equals("null")) {
            nameField.setText(configClient.getName());
        }

        if (configClient.getEmail() != null && !configClient.getEmail().equals("null")) {
            emailField.setText(configClient.getEmail());
        }

        if (configClient.getIban() != null  && !configClient.getIban().equals("null")) {
            ibanField.setText(configClient.getIban());
        }

        if (configClient.getBic() != null  && !configClient.getBic().equals("null")) {
            bicField.setText(configClient.getBic());
        }

        if (configClient.getServerUrl() != null  && !configClient.getServerUrl().equals("null")) {
            serverURLField.setText(configClient.getServerUrl());
        }
        initializeChoiceBox();
    }

    private void initializeChoiceBox() {

        List<String> currencies = new ArrayList<>();
        currencies.add("EUR");
        currencies.add("USD");
        currencies.add("CHF");
        currencies.add("AUD");
        currencyMenu.setItems(FXCollections.observableArrayList(currencies));
        if (configClient.getCurrency() != null && currencies.contains(configClient.getCurrency())) {
            currencyMenu.getSelectionModel().select(configClient.getCurrency());
        } else {
            currencyMenu.getSelectionModel().selectFirst();
        }
    }

    /**
     * Method to be called when a key is pressed
     * @param e keyevent to listen
     */
    public void keyPressed(KeyEvent e) {
        if (e.isControlDown() && e.getCode() == KeyCode.W) {  //close window
            mainCtrl.closeWindow();
        }
        if (e.isControlDown() && e.getCode() == KeyCode.S) {  //close window
            onConfirmClick();
        }
        switch (e.getCode()) {
//            case ENTER:
//                moveToNextTextField((TextField) e.getSource());
//                break;
            case ESCAPE:
                onCancelClick();
                break;
            case TAB:
                moveToNextTextField((TextField) e.getSource());
                break;
            default:
                break;
        }
    }

    private void moveToNextTextField(TextField currentTextField) {
        // Find the index of the current text field
        int index = -1;
        TextField[] textFields = {nameField, emailField, ibanField, bicField, serverURLField}; // Add all text fields here
        for (int i = 0; i < textFields.length; i++) {
            if (textFields[i] == currentTextField) {
                index = i;
                break;
            }
        }

        // Move focus to the next text field
        if (index != -1 && index < textFields.length - 1) {
            textFields[index + 1].requestFocus();
        }
    }

}
