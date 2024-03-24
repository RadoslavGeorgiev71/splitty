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

    @FXML
    public void onCancelClick() {
        clearFields();
        mainCtrl.showStartScreen();
    }

    @FXML
    public void onConfirmClick() {
        configClient.setName(nameField.getText());
        configClient.setEmail(emailField.getText());
        configClient.setIban(ibanField.getText());
        configClient.setBic(bicField.getText());
        clearFields();
        mainCtrl.showStartScreen();
    }

    private void clearFields() {
        nameField.clear();
        emailField.clear();
        ibanField.clear();
        bicField.clear();
    }

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
        List<String> currencys = new ArrayList<>();
        currencys.add("EUR");
        currencys.add("USD");
        currencys.add("AUD");
        currencyMenu.setItems(FXCollections.observableArrayList(currencys));
        currencyMenu.getSelectionModel().selectFirst();
    }

}
