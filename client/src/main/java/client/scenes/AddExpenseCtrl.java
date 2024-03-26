package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import commons.Expense;
import commons.Participant;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import com.google.inject.Inject;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.Currency;
import java.util.ResourceBundle;

public class AddExpenseCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Event event;
    private Expense expense;
    private Participant participant;

    @FXML
    private ChoiceBox payerChoiceBox;               //Who paid?
    @FXML
    private TextField titleField;                   //What for?
    @FXML
    private TextField amountField;                  //How much?
    @FXML
    private ChoiceBox currChoiceBox;
    @FXML
    private DatePicker datePicker;                  //When?
    @FXML
    private CheckBox equally;                       //How to split?
    @FXML
    private CheckBox onlySome;
    @FXML
    private TextField tags;                         //Expense Type
    @FXML
    private Button add;
    @FXML
    private Button abort;

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
        Expense expense = new Expense();
        expense.setTitle(titleField.getText());
//        int index = 0;
//        while(event.getParticipants().get(index).getName() != payerChoiceBox.getValue())
        expense.setPayingParticipant((Participant) payerChoiceBox.getValue());
        expense.setAmount(Double.parseDouble(amountField.getText()));
        expense.setParticipants(event.getParticipants());
        expense.setDateTime(datePicker.getValue().toString());
        //server.addExpense(expense);
        event.addExpense(expense);
        server.persistEvent(event);
        clearFields();
        //event = server.getEvent(event.getId());
        mainCtrl.showEventOverview(event);
    }

    /**
     * Clear all fields for the next use
     */
    private void clearFields() {
        payerChoiceBox.getSelectionModel().selectFirst();
        titleField.clear();
        amountField.clear();
        currChoiceBox.getSelectionModel().selectFirst();
        //datePicker.setConverter(event.getDateTime());
        equally.setSelected(true);
        onlySome.setSelected(false);
        tags.clear();
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
        if (event != null){
            payerChoiceBox.setItems(FXCollections.observableArrayList(event.getParticipants()));
            payerChoiceBox.setConverter(new StringConverter<Participant>() {
                @Override
                public String toString(Participant participant) {
                    if (participant != null) {
                        return participant.getName();
                    }
                    else {
                        return "";
                    }
                }
                @Override
                public Participant fromString(String string) {
                    return null;
                }
            } );

            payerChoiceBox.getSelectionModel().selectFirst();
            equally.setAllowIndeterminate(false);
            equally.setSelected(true);
            onlySome.setAllowIndeterminate(false);
            onlySome.setSelected(false);
        }
    }
}
