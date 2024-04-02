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

public class AddExpenseCtrl{

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Event event;
    private Expense expense;
    private String currency;

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
        Expense expense = new Expense();
        expense.setTitle(titleField.getText());
//        int index = 0;
//        while(event.getParticipants().get(index).getName() != payerChoiceBox.getValue())
        expense.setPayingParticipant((Participant) payerChoiceBox.getValue());

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
        expense.setParticipants(event.getParticipants());
        expense.setDateTime(datePicker.getValue().toString());
        //server.addExpense(expense);
        event.addExpense(expense);
        server.persistEvent(event);
        clearFields();
        event = server.getEvent(event.getId());
        mainCtrl.showEventOverview(event);
    }

    /**
     * Clear all fields for the next use
     */
    public void clearFields() {
        payerChoiceBox.getSelectionModel().selectFirst();
        titleField.clear();
        amountField.clear();
        currChoiceBox.getSelectionModel().selectFirst();
        //datePicker.setConverter(event.getDateTime());
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
     * Initiallizes the currency choice box with the data
     */
    public void initializeCurr() {
        if(currency == null || currency.length() < 1) {
            currChoiceBox.getSelectionModel().selectFirst();
        }
        List<String> currencies = new ArrayList<>();
        currencies.add("EUR");
        currencies.add("USD");
        currencies.add("CHF");
        currChoiceBox.setItems(FXCollections.observableArrayList(currencies));
        int j = 0;
        while(j <= 2 && currencies.get(j) != currency){
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
                    return null;
                }
            } );

            payerChoiceBox.getSelectionModel().selectFirst();
            initializeCurr();
            equally.setAllowIndeterminate(false);
            equally.setSelected(true);
            onlySome.setAllowIndeterminate(false);
            onlySome.setSelected(false);
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
