package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import commons.Expense;
import commons.Participant;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import com.google.inject.Inject;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EditExpenseCtrl{

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Event event;
    private Expense expense;
    private String currency;
    private List<Participant> participants;


    @FXML
    private Text expenseField;                 //Title
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
    private GridPane allGridPane;
    @FXML
    private TextField tags;                         //Expense Type
    @FXML
    private Button expenseSaveButton;
    @FXML
    private Button expenseDeleteButton;
    @FXML
    private Button expenseAbortButton;

    /**
     * Constructor for EditExpenseCtrl
     * @param server client is on
     * @param mainCtrl of client
     */
    @Inject
    public EditExpenseCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Controller method for abort button
     * Sends back to overviewEvent window
     * @param actionEvent to handle
     */
    public void onAbortClick(ActionEvent actionEvent) {
        clearFields();
        mainCtrl.showEventOverview(event);
    }

    /**
     * Controller class for the ok button
     * Sends back to Overview Event window back with the participant altered
     * @param actionEvent to handle
     */
    public void onSaveClick(ActionEvent actionEvent) {
        expense.setTitle(titleField.getText());
        expense.setPayingParticipant((Participant)
                payerChoiceBox.getSelectionModel().getSelectedItem());
        expense.setAmount(Double.parseDouble(amountField.getText()));
        expense.setCurrency(currChoiceBox.getSelectionModel().getSelectedItem().toString());
        if(equally.isSelected() || participants.size() == event.getParticipants().size()){
            expense.setParticipants(event.getParticipants());
        }
        else{
            expense.setParticipants(participants);
        }
        expense.setDateTime(datePicker.getValue().toString());
        server.updateExpense(event.getId(), expense);
        clearFields();
        mainCtrl.showEventOverview(event);
    }

    /**
     * Controller class for the ok button
     * Sends back to Overview Event window back with the participant altered
     * @param actionEvent to handle
     */
    public void onDeleteClick(ActionEvent actionEvent) {
        if (expense != null){
            Alert alert =  new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Remove Expense");
            alert.setHeaderText("You will remove this expense permanently from this event");
            alert.setContentText("Are you sure you want to remove " +
                    this.expense.getTitle() + "?");
            if (alert.showAndWait().get() == ButtonType.OK){
                server.deleteExpense(event.getId(), expense);
                mainCtrl.showEventOverview(server.getEvent(event.getId()));
            }
        }
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
     * Setter for expense
     * @param expense to set
     */
    public void setExpense(Expense expense) {
        this.expense = expense;
    }

    /**
     * Setter for currency
     * @param currency to set
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * Setter for participants
     * @param participants to set
     */
    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }


    /**
     * Handles the key event pressed
     * @param e the KeyEvent to handle
     */
    public void keyPressed(KeyEvent e) {
        if (e.isControlDown() && e.getCode() == KeyCode.W) {  //close window
            mainCtrl.closeWindow();
        }
        if (e.isControlDown() && e.getCode() == KeyCode.S) {  //close window
            onSaveClick(null);
        }
        switch (e.getCode()) {
//            case ENTER:
//                moveToNextTextField((TextField) e.getSource());
//                break;
            case ESCAPE:
                onAbortClick(null);
                break;
            case TAB:
                moveToNextTextField((TextField) e.getSource());
                break;
            default:
                break;
        }
    }

    /**
     * Moves the focus to the next field
     * @param currentTextField current field where the focus is now
     */
    private void moveToNextTextField(TextField currentTextField) {
        // Find the index of the current text field
        int index = -1;
        TextField[] textFields = {titleField, amountField, tags}; // Add all text fields here
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

    /**
     * Method to be executed when only some people have to pay
     */
    @FXML
    public void onlySomeChecked() {
        allGridPane.getChildren().clear();
        allGridPane.setVgap(5);
        allGridPane.setHgap(5);
        if (event != null && onlySome.isSelected()) {
            for (int i = 0; i < event.getParticipants().size(); i++) {
                Label nameLabel = new Label(event.getParticipants().get(i).getName());
                nameLabel.setWrapText(true); // Wrap text to prevent truncation
                CheckBox hasParticipated = new CheckBox("");

                // Set fixed column widths
                nameLabel.setMaxWidth(Double.MAX_VALUE);

                GridPane.setFillWidth(nameLabel, true);

                allGridPane.add(hasParticipated, 0, i);
                allGridPane.add(nameLabel, 1, i);

                Participant p = event.getParticipants().get(i);
                if(expense.getParticipants().contains(p)){
                    hasParticipated.setSelected(true);
                }
                else{
                    hasParticipated.setSelected(false);
                }
                hasParticipated.setOnAction(event -> addRemoveParticipant(p));
            }
        }
    }

    /**
     * Method to be executed when only some people have to pay
     *
     * @param participant to be added/ removed
     */
    @FXML
    public void addRemoveParticipant(Participant participant) {
        if(participants.contains(participant)){
            participants.remove(participant);
        }
        else{
            participants.add(participant);
        }
    }

    /**
     * Initiallizes the payer choice box with the data
     */
    public void initializePayer() {
        if (event != null) {
            payerChoiceBox.setItems(FXCollections.observableArrayList(event.getParticipants()));
            payerChoiceBox.setConverter(new StringConverter<Participant>() {
                @Override
                public String toString(Participant participant) {
                    if (participant != null) {
                        return participant.getName();
                    } else {
                        return "";
                    }
                }

                @Override
                public Participant fromString(String string) {
                    int i = 0;
                    while(event.getParticipants().get(i).getName() != string &&
                            i < event.getParticipants().size()){
                        i++;
                    }
                    return event.getParticipants().get(i);
                }
            });
            int i = 0;
            String name = expense.getPayingParticipant().getName();
            List<Participant> people = event.getParticipants();
            while (i < people.size() && people.get(i).getName() != name) {
                i++;
            }
            if (i < event.getParticipants().size()) {
                payerChoiceBox.getSelectionModel().select(i);
            } else {
                payerChoiceBox.getSelectionModel().selectFirst();
            }
        }
    }

    /**
     * Initiallizes the currency choice box with the data
     */
    public void initializeCurr() {
        if(event == null) {
            currChoiceBox.getSelectionModel().selectFirst();
        }
        List<String> currencies = new ArrayList<>();
        currencies.add("EUR");
        currencies.add("USD");
        currencies.add("CHF");
        currencies.add("AUD");
        currChoiceBox.setItems(FXCollections.observableArrayList(currencies));
        if(currencies.contains(expense.getCurrency())){
            currChoiceBox.getSelectionModel().select(expense.getCurrency());
        }
        else {
            currChoiceBox.getSelectionModel().selectFirst();
        }
    }


    /**
     * Initiallizes the fields with the data
     */
    public void initialize() {
        if(event != null){
            initializePayer();
            expenseField.setText("Edit Expense");
            titleField.setText(expense.getTitle());
            amountField.setText("" + expense.getAmount());
            initializeCurr();
            if(expense.getDateTime() != null){
                datePicker.setValue(LocalDate.parse(expense.getDateTime()));
            }
            equally.setAllowIndeterminate(false);
            onlySome.setAllowIndeterminate(false);
            List<Participant> edited = new ArrayList<>();
            for(Participant p: expense.getParticipants()){
                edited.add(p);
            }
            setParticipants(edited);
            if(event.getParticipants().equals(expense.getParticipants())){
                equally.setSelected(true);
                onlySome.setSelected(false);
            }
            else{
                equally.setSelected(false);
                onlySome.setSelected(true);
            }
            onlySomeChecked();
        }
    }
}
