package client.scenes;

import client.utils.ConfigClient;
import client.utils.LanguageResourceBundle;
import client.utils.ServerUtils;
import com.google.inject.Inject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import commons.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;


public class InvitationCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private LanguageResourceBundle languageResourceBundle;

    @FXML
    private Text eventNameText;

    @FXML
    private Text invitationSendPeopleText;

    @FXML
    private Text invitationPeopleByEmailText;

    @FXML
    private TextField inviteCodeText;

    @FXML
    private Button sendInvitesButton;

    @FXML
    private Button goEventButton;

    @FXML
    private Button goBackButton;

    @FXML
    private TextArea emailTextArea;

    private Event event;
    /**
     *
     * @param server
     * @param mainCtrl
     */
    @Inject
    public InvitationCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Initializes eventNameText and inviteCodeText based on what they should be.
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    public void initialize(URL location, ResourceBundle resources){
        eventNameText.setText("");
        inviteCodeText.setText("");

    }

    /**
     * initialize method for invitationCtrl
     */
    public void initialize(){
        languageResourceBundle = LanguageResourceBundle.getInstance();
        switchTextLanguage();
        setInviteCodeText();
        setEventNameText();
    }

    /**
     * Switches the text language.
     */

    public void switchTextLanguage(){

        ResourceBundle bundle = languageResourceBundle.getResourceBundle();
        sendInvitesButton.setText(bundle.getString("sendInvitesButton"));
        goEventButton.setText(bundle.getString("goEventButton"));
        goBackButton.setText(bundle.getString("backButton"));
        invitationPeopleByEmailText.setText(bundle.getString("invitationPeopleByEmailText"));
        invitationSendPeopleText.setText(bundle.getString("invitationSendPeopleText"));
    }

    /**
     * Sets inviteCodeText.
     */
    public void setInviteCodeText(){
        inviteCodeText.setText(this.event.getInviteCode());
    }

    /**
     * Sets eventNameText.
     */
    public void setEventNameText(){
        eventNameText.setText(this.event.getTitle());
    }

    /**
     * Sets event.
     * @param event the event for the invitation
     */
    public void setEvent(Event event){
        this.event = event;
    }

    /**
     * Resets texts.
     */
    private void resetFields(){
        eventNameText.setText("");
        inviteCodeText.setText("");
    }

    /**
     * Clears email area.
     */
    private void clearEmail(){
        emailTextArea.clear();
    }

    @FXML
    /**
     * Should send invites.
     */
    private void sendInvites(){
        List<String> emails = new ArrayList<>();
        String name = ConfigClient.getName();
        Scanner scanner = new Scanner(emailTextArea.getText());
        while(scanner.hasNextLine()){
            emails.add(scanner.nextLine());
        }
        if(server.sendInvites(emails, event, name)){
            Alert alert =  new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Invitation emails were sent successfully");
            alert.showAndWait();
            clearEmail();
        }

    }

    @FXML
    private void backToStart(){
        mainCtrl.showStartScreen();
        resetFields();
        clearEmail();
    }

    @FXML
    private void goToEvent(){
        mainCtrl.showEventOverview(this.event);
        resetFields();
        clearEmail();
    }

    public void keyPressed(KeyEvent e) {
        if (e.isControlDown() && e.getCode() == KeyCode.W) {  //close window
            mainCtrl.closeWindow();
        }
        switch (e.getCode()) {
            case ENTER:
                sendInvites();
                break;
            case ESCAPE:
                backToStart();
                break;
            default:
                break;
        }
    }
}
