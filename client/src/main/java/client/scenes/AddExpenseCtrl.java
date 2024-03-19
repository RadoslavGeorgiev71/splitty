package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import commons.Participant;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.input.KeyEvent;

import com.google.inject.Inject;
import java.net.URL;
import java.util.Currency;
import java.util.ResourceBundle;

public class AddExpenseCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Event event;
    private Participant participant;

    @FXML
    private ChoiceBox<Participant> payerChoiceBox;  //Who paid?
    @FXML
    private TextField titleField;                   //What for?
    @FXML
    private TextField amountField;                  //How much?
    @FXML
    private ChoiceBox<Currency> currChoiceBox;
    @FXML
    private DatePicker datePicker;                  //When?
    @FXML
    private CheckBoxListCell<Participant> participantAll;   //How to split?
    @FXML
    private CheckBoxListCell<Participant> participantSome;
    @FXML
    private TextField tags;                         //Expense Type

    /**
     * Constructor for AddExpenseCtrl
     * @param server
     * @param mainCtrl
     */
    @Inject
    public AddExpenseCtrl(ServerUtils server, MainCtrl mainCtrl) {
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
        //TODO
        mainCtrl.showEventOverview(event);
    }

    /**
     * Clear all fields for the next use
     */
    private void clearFields() {
        titleField.clear();
        amountField.clear();
    }

    /**
     * Setter for event
     * @param event to set
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     * Setter for paying participant
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
     * Initiallizes the fields with the data
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //To do
    }
}
