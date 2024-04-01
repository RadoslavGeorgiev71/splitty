package client.scenes;

import client.utils.LanguageResourceBundle;
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

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EditExpenseCtrl{

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Event event;
    private Expense expense;

    private LanguageResourceBundle languageResourceBundle;

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
                    return null;
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
        currChoiceBox.setItems(FXCollections.observableArrayList(currencies));
        int j = 0;
        while(j <= 2 && currencies.get(j) != expense.getCurrency()){
            j++;
        }
        if(j < 3){
            currChoiceBox.getSelectionModel().select(j);
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

            languageResourceBundle = LanguageResourceBundle.getInstance();
            switchTextLanguage();
            initializePayer();
            titleField.setText(expense.getTitle());
            amountField.setText("" + expense.getAmount());
            initializeCurr();
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
        }
    }

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
}
