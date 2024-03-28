package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import commons.Expense;
import commons.Participant;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import com.google.inject.Inject;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EditExpenseCtrl{

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Event event;
    private Expense expense;

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
//        int index = 0;
//        while(event.getParticipants().get(index).getName() != payerChoiceBox.getValue())
        expense.setPayingParticipant((Participant) payerChoiceBox.getValue());
        expense.setAmount(Double.parseDouble(amountField.getText()));
        expense.setCurrency(currChoiceBox.getSelectionModel().getSelectedItem().toString());
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
     * Controller class for the ok button
     * Sends back to Overview Event window back with the participant altered
     * @param actionEvent to handle
     */
    public void onDeleteClick(ActionEvent actionEvent) {
        //server.addExpense(expense);
        event.removeExpense(expense);
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
     * Setter for expense
     * @param expense to set
     */
    public void setExpense(Expense expense) {
        this.expense = expense;
    }

    /**
     * Handles the key event pressed
     * @param e the KeyEvent to handle
     */
    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                onSaveClick(null);
                break;
            case ESCAPE:
                onAbortClick(null);
                break;
            default:
                break;
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
        if (event != null) {
            for (int i = 0; i < event.getParticipants().size(); i++) {
                Label nameLabel = new Label(event.getParticipants().get(i).getName());
                nameLabel.setWrapText(true); // Wrap text to prevent truncation
                CheckBox hasParticipated = new CheckBox("");

                // Set fixed column widths
                nameLabel.setMaxWidth(Double.MAX_VALUE);

                GridPane.setFillWidth(nameLabel, true);

                allGridPane.add(hasParticipated, 0, i);
                allGridPane.add(nameLabel, 1, i);

                Expense expensei = event.getExpenses().get(i);
                hasParticipated.setSelected(false);
            }
        }
    }

    /**
     * Initiallizes the fields with the data
     */
    public void initialize() {
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

            int i = 0;
            String name = expense.getPayingParticipant().getName();
            while(event.getParticipants().get(i).getName() != name){
                i++;
            }
            if(i >= event.getParticipants().size()){
                payerChoiceBox.getSelectionModel().selectFirst();
            }
            else{
                payerChoiceBox.getSelectionModel().select(i);
            }

            List<String> currencies = new ArrayList<>();
            currencies.add("EUR");
            currencies.add("USD");
            currencies.add("CHF");
            currChoiceBox.setItems(FXCollections.observableArrayList(currencies));
            int j = 0;
            while(currencies.get(j) != expense.getCurrency()){
                j++;
            }
            if(j > 2){
                payerChoiceBox.getSelectionModel().selectFirst();
            }
            else{
                payerChoiceBox.getSelectionModel().select(j);
            }

            expenseField.setText("Edit Expense");
            titleField.setText(expense.getTitle());
            amountField.setText("" + expense.getAmount());
            equally.setAllowIndeterminate(false);
            onlySome.setAllowIndeterminate(false);
            if(event.getParticipants().equals(expense.getParticipants())){
                equally.setSelected(true);
                onlySome.setSelected(false);
            }
            else{
                equally.setSelected(false);
                onlySome.setSelected(true);
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            LocalDate date = LocalDate.parse(expense.getDateTime(), formatter);
            //datePicker.setConverter(formatter);
        }
    }
}
