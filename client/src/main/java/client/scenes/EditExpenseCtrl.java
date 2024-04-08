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

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EditExpenseCtrl{

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Event event;
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
    private Button expenseSaveButton;
    @FXML
    private Button expenseDeleteButton;
    @FXML
    private Button expenseAbortButton;
    @FXML
    private Label editExpenseHowMuch;
    @FXML
    private Label editExpenseForWhat;
    @FXML
    private Label editExpenseWhoPaid;
    @FXML
    private Label editExpenseWhen;
    @FXML
    private Label editExpenseHow;
    @FXML
    private Label editExpenseType;

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
        Event undoEvent = event;
        expense.setTitle(titleField.getText());
        expense.setPayingParticipant(payerChoiceBox.getSelectionModel().getSelectedItem());
        saveAsEuro();
        try {
            expense.setAmount(Double.parseDouble(amountField.getText()));
            expense.setCurrency(currChoiceBox.getSelectionModel().getSelectedItem().toString());
        } catch (NumberFormatException e) {
            Alert alert =  new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Amount is not a number");
            alert.setContentText("The value you entered " +
                    "for the amount of money paid should be a number");
            alert.showAndWait();
            return;
        }
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
            else{
                expense.setParticipants(participants);}
        }
        expense.setDateTime(datePicker.getValue().toString());
        saveDebts(expense);
        clearFields();
        server.persistEvent(event);
        event = server.getEvent(event.getId());
        if(event != null){
            mainCtrl.showEventOverview(event);
        }
        else{
            mainCtrl.showEventOverview(undoEvent);
        }
    }

    /**
     * @param expense
     */
    public void saveDebts(Expense expense){
        for(Debt debt : expense.getDebts()) {
            //server.deleteDebt(debt);
        }
        for(Participant participant : expense.getParticipants()) {
            if(participant.equals(expense.getPayingParticipant())) {
                continue;
            }
            Debt debt = new Debt(expense.getPayingParticipant(), participant,
                    expense.getAmount() / (expense.getParticipants().size()));
            //expense.add(debt);
            //server.addDebt(debt);
        }
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

            for(Debt debt : expense.getDebts()) {
                //server.deleteDebt(debt);
            }

            if (alert.showAndWait().get() == ButtonType.OK){
                Event undoEvent = event;
                event.removeExpense(expense);
                server.persistEvent(event);
                //server.deleteExpense(expense);
                //server.deleteExpense(event.getId(), expense);
                event = server.getEvent(event.getId());
                if(event != null){
                    mainCtrl.showEventOverview(event);
                }
                else{
                    mainCtrl.showEventOverview(undoEvent);
                }
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
     *  converted currency
     * @return converted amount
     */
    public Double convertCurrency(){
        String currency = ConfigClient.getCurrency();
        Double res = expense.getAmount();
        res *= server.convertRate(expense.getDateTime(), expense.getCurrency(), currency);
        DecimalFormat df = new DecimalFormat("#.##");
        res = Double.valueOf(df.format(res));
        return res;
    }

    /**
     *  converted currency to save to server as EUR
     */
    public void saveAsEuro(){
        boolean yes = false;
        if(!yes){
            return;
        }
        Double res = Double.parseDouble(amountField.getText());
        res *= server.convertRate(datePicker.getValue().toString(),
                currChoiceBox.getSelectionModel().getSelectedItem().toString(),
                "EUR");
        DecimalFormat df = new DecimalFormat("#.##");
        res = Double.valueOf(df.format(res));
        expense.setAmount(res);
        expense.setCurrency("EUR");
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
            Participant name = expense.getPayingParticipant();
            payerChoiceBox.getSelectionModel().select(name);
        }
    }

    /**
     * Initiallizes the currency choice box with the data
     */
    public void initializeCurr() {
        currency = ConfigClient.getCurrency();
        List<String> currencies = new ArrayList<>();
        currencies.add("EUR");
        currencies.add("USD");
        currencies.add("CHF");
        currencies.add("AUD");
        currChoiceBox.setItems(FXCollections.observableArrayList(currencies));
        try{
            amountField.setText("" + convertCurrency());
            currChoiceBox.getSelectionModel().select(currency);
        }
        catch (Exception e){
            amountField.setText("" + expense.getAmount());
            currChoiceBox.getSelectionModel().select(expense.getCurrency());
        }
    }


    /**
     * Initiallizes the fields with the data
     */
    public void initialize() {
        if(event != null){

            languageResourceBundle = LanguageResourceBundle.getInstance();
            switchTextLanguage();
            initializePayer();
            titleField.setText(expense.getTitle());
            initializeCurr();
            if(expense.getDateTime() != null){
                datePicker.setValue(LocalDate.parse(expense.getDateTime()));
            }
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

    /**
     * Switches the language of the text
     */

    public void switchTextLanguage(){
        ResourceBundle bundle = languageResourceBundle.getResourceBundle();

        expenseField.setText(bundle.getString("editExpenseTitle"));
        editExpenseHowMuch.setText(bundle.getString("editExpenseHowMuch"));
        editExpenseForWhat.setText(bundle.getString("editExpenseForWhat"));
        editExpenseWhoPaid.setText(bundle.getString("editExpenseWhoPaid"));
        editExpenseWhen.setText(bundle.getString("editExpenseWhen"));
        editExpenseHow.setText(bundle.getString("editExpenseHow"));
        editExpenseType.setText(bundle.getString("editExpenseType"));
        expenseSaveButton.setText(bundle.getString("expenseSaveButton"));
        expenseDeleteButton.setText(bundle.getString("expenseDeleteButton"));
        expenseAbortButton.setText(bundle.getString("expenseAbortButton"));
        equally.setText(bundle.getString("editExpenseEqually"));
        onlySome.setText(bundle.getString("editExpenseOnlySome"));
    }

    //For testing

    /**
     * Getter for participant list
     * @return participants
     */
    public List<Participant> getParticipants() {
        return participants;
    }
}
