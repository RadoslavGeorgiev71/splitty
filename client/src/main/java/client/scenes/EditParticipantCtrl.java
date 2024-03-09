package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import commons.Participant;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import com.google.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class EditParticipantCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Event event;
    private Participant participant;

    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField ibanField;
    @FXML
    private TextField bicField;

    /**
     * Constructor for EditParticipantCtrl
     * @param server
     * @param mainCtrl
     */
    @Inject
    public EditParticipantCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Controller method for cancel button
     * Sends back to overviewEvent window
     * @param actionEvent to handle
     */
    public void cancel(ActionEvent actionEvent) {
        clearFields();
        mainCtrl.showEventOverview(event);
    }

    /**
     * Controller class for the ok button
     * Sends back to Overview Event window back with the participant altered
     * @param actionEvent to handle
     */
    public void ok(ActionEvent actionEvent) {
        participant.setName(nameField.getText());
        participant.setIban(ibanField.getText());
        participant.setEmail(emailField.getText());
        participant.setBic(bicField.getText());
        participant = server.persistParticipant(participant);
        clearFields();
        //event = server.getEvent(event.getId());
        mainCtrl.showEventOverview(event);
    }

    /**
     * Clear all fields for the next use
     */
    private void clearFields() {
        nameField.clear();
        emailField.clear();
        ibanField.clear();
        bicField.clear();
    }

    /**
     * Setter for event
     * @param event to set
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     * Setter for participant
     * @param participant
     */
    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    /**
     * Handles the key event pressed
     * @param e the KeyEvent to handle
     */
    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                ok(null);
                break;
            case ESCAPE:
                cancel(null);
                break;
            default:
                break;
        }
    }

    /**
     * Initiallizes the fields with the participant's data
     */
    public void initialize() {
        if(participant != null){
            nameField.setText(participant.getName());
            nameField.positionCaret(participant.getName().length());
            emailField.setText(participant.getEmail());
            ibanField.setText(participant.getIban());
            bicField.setText(participant.getBic());
        }

    }

}
