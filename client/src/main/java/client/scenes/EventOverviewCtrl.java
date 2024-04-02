package client.scenes;

import client.utils.ConfigClient;
import client.utils.LanguageButtonUtils;
import client.utils.LanguageResourceBundle;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.Participant;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import javafx.collections.FXCollections;

import javafx.event.ActionEvent;

import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class EventOverviewCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private LanguageResourceBundle languageResourceBundle;

    private String[] keys = {"serverUrl", "email", "iban", "bic", "language",
        "currency", "name", "recentEvents"};

    @FXML
    private MenuButton languageButton;

    @FXML
    private Text overviewParticipantsText;

    @FXML
    private Button overviewEditParticipantButton;

    @FXML
    private Button overviewAddParticipantButton;

    @FXML
    private Button overviewRemoveParticipantButton;

    @FXML
    private Button overviewAddExpenseButton;

    @FXML
    private Button sendInvitesButton;

    @FXML
    private Button backButton;

    @FXML
    private Button overviewSettleDebtsButton;

    @FXML
    private Tab tabPaneAll;

    @FXML
    private ChoiceBox<Participant> participantsMenu;

    @FXML
    private GridPane tabPaneAllGridPane;

    @FXML
    private Text participatingParticipants;

    @FXML
    private Label eventTitleLabel;

    @FXML
    private Tab tabPaneFromPerson;

    @FXML
    private GridPane tabPaneFromGridPane;

    @FXML
    private Tab tabPaneIncludingPerson;

    @FXML
    private GridPane tabPaneIncludingGridPane;

    private TextField titleTextField;

    private Event event;

    /**
     *
     * @param server
     * @param mainCtrl
     */

    @Inject
    public EventOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Method to be executed when send invites button is clicked
     * goes to invitation window to send invites
     * @param actionEvent
     */

    @FXML
    public void onSendInvites(ActionEvent actionEvent) {
        mainCtrl.showInvitation(this.event);
    }

    /**
     * Method to be executed when edit participants button is clicked
     */

    @FXML
    public void onEditParticipantsClick() {
        if (event.getParticipants().isEmpty()) {
            ResourceBundle bundle = languageResourceBundle.getResourceBundle();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(bundle.getString("eventNoParticipantsToEdit"));
            if (alert.showAndWait().get() == ButtonType.OK){
                mainCtrl.showEventOverview(this.event);
            }
        }
        else {
            mainCtrl.showEditParticipant(this.event, (Participant) participantsMenu.getValue());
        }
    }

    /**
     * Method to be executed when add participant button is clicked
     */

    @FXML
    public void onAddParticipantClick() {
        mainCtrl.showAddParticipant(this.event);
    }

    /**
     * Method to be executed when add expense button is clicked
     */

    @FXML
    public void onAddExpenseClick() {
        Participant p = participantsMenu.getValue();
        mainCtrl.showAddExpense(this.event, p);
    }

    /**
     * Method to be executed when edit expense button is clicked
     * @param expense to be edited
     */

    @FXML
    public void onEditExpenseClick(Expense expense) {
        mainCtrl.showEditExpense(this.event, expense);
    }

    /**
     * Method to be executed when settle debts button is clicked
     */

    @FXML
    public void onSettleDebtsClick() {
        mainCtrl.showOpenDebts(event);
    }

    /**
     * Method to be executed when back button is clicked
     */

    @FXML
    public void onBackClick() {
        Alert alert =  new Alert(Alert.AlertType.CONFIRMATION);
        ResourceBundle bundle = languageResourceBundle.getResourceBundle();
        alert.setTitle(bundle.getString("exitEventAlertTitleText"));
        alert.setHeaderText(bundle.getString("exitEventAlertHeaderText"));
        alert.setContentText(bundle.getString("exitEventAlertContentText"));
        if (alert.showAndWait().get() == ButtonType.OK){
            mainCtrl.showStartScreen();
        }

    }

    /**
     *  converted currency
     * @param expense to convert
     * @return converted amount
     */
    public Double convertCurrency(Expense expense){
        String currency = ConfigClient.getCurrency();
        Double res = expense.getAmount();
        res *= server.convertRate(expense.getDateTime(), expense.getCurrency(), currency);
        DecimalFormat df = new DecimalFormat("#.##");
        res = Double.valueOf(df.format(res));
        return res;
    }

    /**
     * Method for converting
     * @param expense
     * @return updated expense with correct currency
     */
    public Expense foreignCurrency(Expense expense){
        String currency = ConfigClient.getCurrency();
        Expense show = new Expense();
        if(currency != null && expense.getCurrency() != currency &&
                currency.length() == 3){
            try{
                show.setAmount(convertCurrency(expense));
                show.setCurrency(currency);
                show.setTitle(expense.getTitle());
                show.setPayingParticipant(expense.getPayingParticipant());
            }
            catch (Exception e){
                show = expense;
            }
        }
        return show;
    }

    /**
     * Method to be executed when delete event button is clicked
     */

    @FXML
    public void tabPaneAllClick() {
        tabPaneAllGridPane.getChildren().clear();
        tabPaneAllGridPane.setVgap(10);
        tabPaneAllGridPane.setHgap(10);
        if (event != null) {
            for (int i = 0; i < event.getExpenses().size(); i++) {
                Expense show = foreignCurrency(event.getExpenses().get(i));
                Label dateLabel = new Label(event.getExpenses().get(i).getDateTime());
                Label nameLabel = new Label(show.getActivity());
                nameLabel.setWrapText(true); // Wrap text to prevent truncation
                Button editButton = new Button("Edit");

                // Set fixed column widths
                dateLabel.setMaxWidth(Double.MAX_VALUE);
                nameLabel.setMaxWidth(Double.MAX_VALUE);

                GridPane.setFillWidth(dateLabel, true);
                GridPane.setFillWidth(nameLabel, true);

                tabPaneAllGridPane.add(dateLabel, 0, i);
                tabPaneAllGridPane.add(nameLabel, 1, i);
                tabPaneAllGridPane.add(editButton, 2, i);

                Expense expensei = event.getExpenses().get(i);
                editButton.setOnAction(event -> onEditExpenseClick(expensei));
            }
            fromPersonTabName();
        }
    }

    /**
     * Method to be executed when tabPaneFromPerson is clicked
     */

    @FXML
    public void tabPaneFromPersonClick() {
        tabPaneFromGridPane.getChildren().clear();
        tabPaneFromGridPane.setVgap(10);
        tabPaneFromGridPane.setHgap(10);
        if (event != null) {
            int j = 0;
            for (int i = 0; i < event.getExpenses().size(); i++) {
                Participant participant = event.getExpenses().get(i).getPayingParticipant();
                if (participant.equals(participantsMenu.
                        getSelectionModel().getSelectedItem())) {
                    Expense show = foreignCurrency(event.getExpenses().get(i));
                    Label dateLabel = new Label(event.getExpenses().get(i).getDateTime());
                    Label nameLabel = new Label(show.getActivity());
                    nameLabel.setWrapText(true); // Wrap text to prevent truncation
                    Button editButton = new Button("Edit");

                    editButton.setOnAction(event -> {
                       //maintCtrl.showEditExpense(event.getExpenses().get(i))
                    });

                    // Set fixed column widths
                    dateLabel.setMaxWidth(Double.MAX_VALUE);
                    nameLabel.setMaxWidth(Double.MAX_VALUE);

                    GridPane.setFillWidth(dateLabel, true);
                    GridPane.setFillWidth(nameLabel, true);

                    tabPaneFromGridPane.add(dateLabel, 0, j);
                    tabPaneFromGridPane.add(nameLabel, 1, j);
                    tabPaneFromGridPane.add(editButton, 2, j++);

                    Expense expensei = event.getExpenses().get(i);
                    editButton.setOnAction(event -> onEditExpenseClick(expensei));
                }
            }
        }
    }

    /**
     * Method to be executed when tabPaneIncludingPerson is clicked
     */

    @FXML
    public void tabPaneIncludingPersonClick() {
        tabPaneIncludingGridPane.getChildren().clear();
        tabPaneIncludingGridPane.getChildren().clear();
        tabPaneIncludingGridPane.setVgap(10);
        tabPaneIncludingGridPane.setHgap(10);
        if (event != null) {
            int j = 0;
            for (int i = 0; i < event.getExpenses().size(); i++) {
                Participant participant = (Participant) participantsMenu.
                        getSelectionModel().getSelectedItem();
                if (event.getExpenses().get(i).getParticipants().contains(participant)) {
                    Expense show = foreignCurrency(event.getExpenses().get(i));
                    Label dateLabel = new Label(event.getExpenses().get(i).getDateTime());
                    Label nameLabel = new Label(show.getActivity());
                    nameLabel.setWrapText(true); // Wrap text to prevent truncation
                    Button editButton = new Button("Edit");

                    editButton.setOnAction(event -> {
                        //maintCtrl.showEditExpense(event.getExpenses().get(i))
                    });

                    // Set fixed column widths
                    dateLabel.setMaxWidth(Double.MAX_VALUE);
                    nameLabel.setMaxWidth(Double.MAX_VALUE);

                    GridPane.setFillWidth(dateLabel, true);
                    GridPane.setFillWidth(nameLabel, true);

                    tabPaneIncludingGridPane.add(dateLabel, 0, j);
                    tabPaneIncludingGridPane.add(nameLabel, 1, j);
                    tabPaneIncludingGridPane.add(editButton, 2, j++);

                    Expense expensei = event.getExpenses().get(i);
                    editButton.setOnAction(event -> onEditExpenseClick(expensei));
                }
            }
        }
    }

    /**
     * Sets the event
     * @param event
     */
    public void setEvent(Event event){
        this.event = event;
    }

    /**
     * Sets the event name
     */

    public void eventName() {
        eventTitleLabel.setText(event.getTitle());
    }

    /**
     * Sets the from tab with the current selected participant
     */
    public void fromPersonTabName() {
        if (event.getParticipants().isEmpty()){
            tabPaneFromPerson.setText(languageResourceBundle.
                    getResourceBundle().getString("tabPaneFrom"));
        }
        else {
            Participant selectedParticipant = (Participant) participantsMenu.getValue();
            if(selectedParticipant != null) {
                tabPaneFromPerson.setText(languageResourceBundle.
                        getResourceBundle().getString("tabPaneFrom")
                        + " " + selectedParticipant.getName());
            }
        }

    }

    /**
     * Sets the Including tab with the current selected participant name
     */
    public void includingPersonTabName() {
        if (event.getParticipants().isEmpty()){
            tabPaneFromPerson.setText(languageResourceBundle.
                    getResourceBundle().getString("tabPaneIncluding"));
        }
        else {
            Participant selectedParticipant = (Participant) participantsMenu.getValue();
            if(selectedParticipant != null) {
                tabPaneIncludingPerson.setText(languageResourceBundle.
                        getResourceBundle().getString("tabPaneIncluding")
                        + " " + selectedParticipant.getName());
            }
        }
    }

    /**
     * Sets the text that displays all the participants in the event
     */
    public void participatingParticipants() {
        String participantsText = "";
        if (event != null) {
            for (int i = 0; i < event.getParticipants().size(); i++) {
                participantsText += event.getParticipants().get(i).getName();
                if (i < event.getParticipants().size() - 1) {
                    participantsText += ", ";
                }
            }
        }
        participatingParticipants.setText(participantsText);
    }

    /**
     * This method is called when the user tries to edit the
     * title of an event.
     * After the user presses enter it persists the event with the new title
     * It does not allow the user to give an empty title
     * @param mouseEvent
     */
    public void editTitle(MouseEvent mouseEvent) {
        titleTextField = new TextField();
        titleTextField.setText(eventTitleLabel.getText());
        titleTextField.setPrefWidth(eventTitleLabel.getWidth());
        titleTextField.setPrefHeight(eventTitleLabel.getHeight());

        Pane parent = (Pane) eventTitleLabel.getParent();
        parent.getChildren().remove(eventTitleLabel);
        parent.getChildren().add(titleTextField);
        titleTextField.requestFocus();


        titleTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if(titleTextField.getText().isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Event title is missing");
                    alert.setContentText("Event title cannot be empty");
                    alert.showAndWait();
                }
                else {
                    saveTitle();
                }
            }
        });
    }

    /**
     * It takes the newtitle from titleTextField and updates
     * the event's title
     */
    private void saveTitle() {
        String newTitle = titleTextField.getText();
        event.setTitle(newTitle);
        server.persistEvent(event);
        eventTitleLabel.setText(newTitle);
        Pane parent = (Pane) titleTextField.getParent();
        parent.getChildren().remove(titleTextField);
        parent.getChildren().add(eventTitleLabel);
    }

    /**
     * Initializes the event overview
     */

    public void initialize(){
        if (event != null){
            languageButton.getItems().clear();
            languageResourceBundle = LanguageResourceBundle.getInstance();
            LanguageButtonUtils.updateLanguageMenuButton(languageButton, new ConfigClient());
            LanguageButtonUtils.languageMenu(languageButton, new ConfigClient(),
                    languageResourceBundle, this::initialize, keys);

            languageButton.setPopupSide(Side.TOP);
            switchLanguage();
            event.setExpenses(server.getEvent(event.getId()).getExpenses());

            participantsMenu.setItems(FXCollections.observableArrayList(event.getParticipants()));
            participantsMenu.setConverter(new StringConverter<Participant>() {
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

            participantsMenu.getSelectionModel().selectFirst();
            eventTitleLabel.setText(event.getTitle());
            participatingParticipants();
            fromPersonTabName();
            includingPersonTabName();

            participantsMenu.setOnAction(event -> {
                fromPersonTabName();
                includingPersonTabName();
            });
            tabPaneAllClick();
        }

        server.registerEventUpdate(event -> {
            this.event = server.getEvent(event.getId());
            Platform.runLater(this::initialize);
        });
    }

    /**
     * Method that always updates language on initialize.
     */

    public void switchLanguage(){
        ResourceBundle bundle = languageResourceBundle.getResourceBundle();

        overviewParticipantsText.setText(bundle.getString("overviewParticipantsText"));
        overviewEditParticipantButton.setText(bundle.getString("overviewEditParticipantButton"));
        overviewAddParticipantButton.setText(bundle.getString("overviewAddParticipantButton"));
        overviewAddExpenseButton.setText(bundle.getString("overviewAddExpenseButton"));
        sendInvitesButton.setText(bundle.getString("sendInvitesButton"));
        backButton.setText(bundle.getString("backButton"));
        overviewSettleDebtsButton.setText(bundle.getString("overviewSettleDebtsButton"));
        tabPaneAll.setText(bundle.getString("tabPaneAll"));

    }

    /**
     * Method to be called when a key is pressed
     * @param e keyevent to listen
     */
    public void keyPressed(KeyEvent e) {
        if (e.isControlDown() && e.getCode() == KeyCode.W) {  //close window
            mainCtrl.closeWindow();
        }
        switch (e.getCode()) {
            case ESCAPE:
                onBackClick();
                break;
            default:
                break;
        }
    }
}
