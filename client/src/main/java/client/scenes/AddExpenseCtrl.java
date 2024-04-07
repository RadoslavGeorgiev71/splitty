package client.scenes;

import client.utils.ConfigClient;
import client.utils.LanguageResourceBundle;
import client.utils.ServerUtils;
import commons.Debt;
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
import java.util.ResourceBundle;

public class AddExpenseCtrl{

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Event event;
    private Participant participant;
    private Expense expense;
    private String currency;
    private List<Participant> participants;

    private LanguageResourceBundle languageResourceBundle;

    @FXML
    private Text expenseField;                 //Title
    @FXML
    private ChoiceBox<Participant> payerChoiceBox;               //Who paid?
    @FXML
    private TextField titleField;                   //What for?
    @FXML
    private TextField amountField;                  //How much?
    @FXML
    private ChoiceBox currChoiceBox;
    @FXML
    private DatePicker datePicker;                  //When?
    @FXML
    private RadioButton equally;                       //How to split?
    @FXML
    private RadioButton onlySome;
    @FXML
    private GridPane allGridPane;
    @FXML
    private TextField tags;                         //Expense Type
    @FXML
    private Button expenseAddButton;
    @FXML
    private Button expenseAbortButton;
    @FXML
    private Label addExpenseWhoPaid;
    @FXML
    private Label addExpenseForWhat;
    @FXML
    private Label addExpenseHowMuch;
    @FXML
    private Label addExpenseWhen;
    @FXML
    private Label addExpenseHow;
    @FXML
    private Label addExpenseType;

    /**
     * Constructor for AddExpenseCtrl
     * @param server client is on
     * @param mainCtrl of client
     */
    @Inject
    public AddExpenseCtrl(ServerUtils server, MainCtrl mainCtrl) {
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
    public void onAddClick(ActionEvent actionEvent) {
        Event undoEvent = event;
        Expense expense = new Expense();
        expense.setTitle(titleField.getText());
        expense.setPayingParticipant(payerChoiceBox.getSelectionModel().getSelectedItem());
        try {
            expense.setAmount(Double.parseDouble(amountField.getText()));
        } catch (NumberFormatException e) {
            Alert alert =  new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Amount is not a number");
            alert.setContentText("The value you entered " +
                    "for the amount of money paid should be a number");
            alert.showAndWait();
            return;
        }
        expense.setCurrency(currChoiceBox.getSelectionModel().getSelectedItem().toString());
        if(equally.isSelected() || participants.size() == event.getParticipants().size()){
            expense.setParticipants(event.getParticipants());
        }
        else{
            if(participants == null || participants.size() == 0){
                Alert alert =  new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Input");
                alert.setHeaderText("Participants cannot be empty");
                alert.setContentText("You must select " +
                        "at least 1 participant");
                alert.showAndWait();
                return;
            }
            else expense.setParticipants(participants);
        }
        expense.setDateTime(datePicker.getValue().toString());
        //Expense newExpense = server.addExpense(event.getId(), expense);
        //Expense newExpense = server.addExpense(expense);
        for(Participant participant : expense.getParticipants()) {
            if(participant.equals(expense.getPayingParticipant())) {
                continue;
            }
            Debt debt = new Debt(expense.getPayingParticipant(), participant,
                expense.getAmount() / (expense.getParticipants().size()));
            //expense.add(debt);
            //server.addDebt(debt);
        }
        event.addExpense(expense);
        server.persistEvent(event);
        clearFields();
        event = server.getEvent(event.getId());
        if(event != null){
            mainCtrl.showEventOverview(event);
        }
        else{
            mainCtrl.showEventOverview(undoEvent);
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
        equally.setSelected(true);
        onlySome.setSelected(false);
        allGridPane.getChildren().clear();
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
     * Setter for participant
     * @param participant to set
     */
    public void setParticipant(Participant participant) {
        this.participant = participant;
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
            onAddClick(null);
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
                hasParticipated.setSelected(false);
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
     * Initiallizes the currency choice box with the data
     */
    public void initializeCurr() {
        currency = ConfigClient.getCurrency();
        if(currency == null || currency.length() < 1) {
            currChoiceBox.getSelectionModel().selectFirst();
        }
        List<String> currencies = new ArrayList<>();
        currencies.add("EUR");
        currencies.add("USD");
        currencies.add("CHF");
        currencies.add("AUD");
        currChoiceBox.setItems(FXCollections.observableArrayList(currencies));
        if(currencies.contains(currency)){
            currChoiceBox.getSelectionModel().select(currency);
        }
        else {
            currChoiceBox.getSelectionModel().selectFirst();
        }
    }

    /**
     * Initiallizes the fields with the data
     */
    public void initialize() {
        if (event != null){

            languageResourceBundle = languageResourceBundle.getInstance();
            switchTextLanguage();

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
                    int i = 0;
                    while(event.getParticipants().get(i).getName() != string &&
                            i < event.getParticipants().size()){
                        i++;
                    }
                    return event.getParticipants().get(i);
                }
            } );

            payerChoiceBox.getSelectionModel().select(participant);
            initializeCurr();
            datePicker.setValue(LocalDate.now());
            equally.setSelected(true);
            onlySome.setSelected(false);

            this.participants = new ArrayList<>();
        }
    }

    /**
     * Switches the language of the text
     */

    public void switchTextLanguage(){
        ResourceBundle bundle = languageResourceBundle.getResourceBundle();


        expenseField.setText(bundle.getString("addExpenseTitle"));
        addExpenseHow.setText(bundle.getString("addExpenseHow"));
        addExpenseForWhat.setText(bundle.getString("addExpenseForWhat"));
        addExpenseHowMuch.setText(bundle.getString("addExpenseHowMuch"));
        addExpenseType.setText(bundle.getString("addExpenseType"));
        addExpenseWhen.setText(bundle.getString("addExpenseWhen"));
        addExpenseWhoPaid.setText(bundle.getString("addExpenseWhoPaid"));
        expenseAddButton.setText(bundle.getString("expenseAddButton"));
        expenseAbortButton.setText(bundle.getString("expenseAbortButton"));
        onlySome.setText(bundle.getString("addExpenseOnlySome"));
        equally.setText(bundle.getString("addExpenseEqually"));

    }
}
