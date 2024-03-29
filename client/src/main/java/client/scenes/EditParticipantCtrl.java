package client.scenes;

import client.utils.LanguageResourceBundle;
import client.utils.ServerUtils;
import commons.Event;
import commons.Participant;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import com.google.inject.Inject;

import java.util.ResourceBundle;

public class EditParticipantCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private LanguageResourceBundle languageResourceBundle;
    private Event event;
    private Participant participant;
    @FXML
    private Label editParticipantLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Button removeParticipantButton;
    @FXML
    private Button participantCancelButton;
    @FXML
    private Button participantOkButton;
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
//        participant = server.persistParticipant(participant);
        clearFields();
        server.persistEvent(event);
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
        if (participant != null){
            languageResourceBundle = LanguageResourceBundle.getInstance();
            switchTextLanguage();
            nameField.setText(participant.getName());
            nameField.positionCaret(participant.getName().length());
            emailField.setText(participant.getEmail());
            ibanField.setText(participant.getIban());
            bicField.setText(participant.getBic());
        }

    }

    /**
     * Switches the text language.
     */
    public void switchTextLanguage(){
        ResourceBundle bundle = languageResourceBundle.getResourceBundle();

        editParticipantLabel.setText(bundle.getString("editParticipantLabel"));
        nameLabel.setText(bundle.getString("nameLabel"));
        removeParticipantButton.setText(bundle.getString("removeParticipantButton"));
        participantCancelButton.setText(bundle.getString("participantCancelButton"));
        participantOkButton.setText(bundle.getString("participantOkButton"));

    }

    /**
     * Removes the participant from the event
     * @param actionEvent ...
     */
    public void removeParticipant(ActionEvent actionEvent) {
        if (participant != null){
            Alert alert =  new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Remove Participant");
            alert.setHeaderText("You will remove this participant permanently from this event");
            alert.setContentText("Are you sure you want to remove " + this.participant.getName());
            if (alert.showAndWait().get() == ButtonType.OK){
                event.removeParticipant(participant);
                server.persistEvent(event);
                server.deleteParticipant(participant);
                mainCtrl.showEventOverview(server.getEvent(event.getId()));
            }
        }

    }
}
