package client.scenes;

import client.utils.ConfigClient;
import client.utils.LanguageResourceBundle;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UserSettingsCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private ConfigClient configClient;

    private String[] keys = {"serverUrl", "email", "iban",
                             "bic", "language", "currency", "name", "recentEvents"};

    private LanguageResourceBundle languageResourceBundle;


    @FXML
    private ChoiceBox currencyMenu;

    @FXML
    private Label settingsTitle;

    @FXML
    private Label nameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label ibanLabel;

    @FXML
    private Label bicLabel;

    @FXML
    private Label currencyLabel;

    @FXML
    private Label serverLabel;

    @FXML
    private Button cancelButton;

    @FXML
    private Button onConfirmClick;
    @FXML
    private Button sendDefault;

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
        configClient.writeToFile("config.txt", contents, keys);
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

        languageResourceBundle = LanguageResourceBundle.getInstance();

        switchTextLanguage();

        initializeChoiceBox();
    }

    private void switchTextLanguage(){
        ResourceBundle bundle = languageResourceBundle.getResourceBundle();

        nameLabel.setText(bundle.getString("settingsNameLabel"));
        emailLabel.setText(bundle.getString("settingsEmailLabel"));
        ibanLabel.setText(bundle.getString("settingsIbanLabel"));
        bicLabel.setText(bundle.getString("settingsBICLabel"));
        currencyLabel.setText(bundle.getString("settingsCurrencyLabel"));
        serverLabel.setText(bundle.getString("settingsURLLabel"));
        cancelButton.setText(bundle.getString("settingsCancelButton"));
        sendDefault.setText(bundle.getString("settingsSendDefault"));
        onConfirmClick.setText(bundle.getString("settingsConfirmButton"));
        settingsTitle.setText(bundle.getString("settingsTitle"));
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
        TextField[] textFields = {nameField, emailField,
                                  ibanField, bicField, serverURLField }; // Add all text fields here
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

    public void sendDefault(ActionEvent actionEvent) {
        if(emailField.getText() != null){
            server.sendDefault();
        }
        else{
            ResourceBundle bundle = languageResourceBundle.getResourceBundle();
            Alert alert =  new Alert(Alert.AlertType.WARNING);
            alert.setTitle(bundle.getString("settingsAlertTitleText"));
            alert.setHeaderText(bundle.getString("settingsAlertHeaderText"));
            alert.setContentText(bundle.getString("settingsAlertContentText"));
            alert.showAndWait();
        }
    }
}
