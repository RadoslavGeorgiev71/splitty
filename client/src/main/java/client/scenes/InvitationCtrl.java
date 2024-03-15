package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;

import java.net.URL;
import java.util.ResourceBundle;

import commons.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class InvitationCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private Text eventNameText;

    @FXML
    private Text inviteCodeText;

    @FXML
    private Button sendInvitesButton;

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

    public void setInviteCodeText(){
        inviteCodeText.setText(this.event.getInviteCode());
    }

    public void setEventNameText(){
        eventNameText.setText(this.event.getTitle());
    }
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

    }
    @FXML
    private void backToStart(){
        mainCtrl.showStartScreen();
    }

    @FXML
    private void goToEvent(){
        mainCtrl.showEventOverview(this.event);
    }
}
