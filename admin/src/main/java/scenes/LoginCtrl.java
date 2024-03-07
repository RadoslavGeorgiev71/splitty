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
        String password = passwordField.getText();
        //cannot implement password verification logic for now
        if (1<2){ //placeholder for if password is correct
            mainCtrl.showOverview();
        }
    }

    @FXML
    void generatePassword(ActionEvent event) {
        admin.generatePassword();
    }

}
