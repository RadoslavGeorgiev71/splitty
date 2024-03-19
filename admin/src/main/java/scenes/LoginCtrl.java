package scenes;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import utils.Admin;

public class LoginCtrl {

    private final Admin admin;
    private final MainCtrl mainCtrl;
    @FXML
    private Label errorLabel;

    @FXML
    private Button loginButton;
    private Button generateButton;

    @FXML
    private PasswordField passwordField;

    /**
     * Constructor for LoginCtrl for dependency injection
     * @param admin
     * @param mainCtrl
     */
    @Inject
    public LoginCtrl(Admin admin, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.admin = admin;

    }

    /**
     * Code to be run on scene start-up. Simply hides the error message
     */
    public void initialize() {
        errorLabel.setVisible(false);
    }

    @FXML
    void login(ActionEvent event) {
        errorLabel.setVisible(false); //in case this login attempt follows an unsuccessful one
        String password = passwordField.getText();
        if (admin.login(password)){
            admin.setPassword(password);
            mainCtrl.showOverview();
        }
        else {
            errorLabel.setVisible(true);
        }
    }

    @FXML
    void generatePassword(ActionEvent event) {
        admin.generatePassword();
    }

}
