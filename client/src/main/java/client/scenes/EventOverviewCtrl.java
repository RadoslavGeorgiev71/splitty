package client.scenes;

import client.utils.ConfigClient;
import client.utils.LanguageButtonUtils;
import client.utils.LanguageResourceBundle;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import javafx.collections.FXCollections;

import javafx.event.ActionEvent;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import javafx.scene.control.MenuButton;

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
    private ChoiceBox<Participant> participantsMenu;

    @FXML
    private Text participatingParticipants;

    @FXML
    private Label eventTitleLabel;

    @FXML
    private Tab tabPaneAll;

    @FXML
    private GridPane tabPaneAllGridPane;

    @FXML
    private Tab tabPaneFromPerson;

    @FXML
    private GridPane tabPaneFromGridPane;

    @FXML
    private Tab tabPaneIncludingPerson;

    @FXML
    private GridPane tabPaneIncludingGridPane;

    @FXML
    private Text amountText;

    private TextField titleTextField;

    private Event event;

    private String iconPath = "client/images/icons/";

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
        if(event.getParticipants().size() <= 1) {
            ResourceBundle bundle = languageResourceBundle.getResourceBundle();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(bundle.getString("eventNotEnoughParticipants"));
            if (alert.showAndWait().get() == ButtonType.OK){
                mainCtrl.showEventOverview(this.event);
            }
        }
        else {
            Participant p = participantsMenu.getValue();
            mainCtrl.showAddExpenseWithTag(this.event, p, null);
        }
    }

    /**
     * Method to be executed when edit expense button is clicked
     * @param expense to be edited
     */

    @FXML
    public void onEditExpenseClick(Expense expense) {
        mainCtrl.showEditExpenseWithTag(this.event, expense, expense.getTag());
    }

    /**
     * Method to be executed when settle debts button is clicked
     */

    @FXML
    public void onSettleDebtsClick() {
        mainCtrl.showOpenDebts(event);
    }

    /**
     * Opens the statistics page for the event
     */
    @FXML
    public void onStatisticsOpen() {
        if(event.getExpenses().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No expenses");
            alert.setContentText("There are no expenses to be made statistics of!");
            alert.showAndWait();
        }
        else {
            mainCtrl.showStatistics(event);
        }
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
     * Sets the icon of the chosen button.
     * @param iconName
     * @param button
     */
    @FXML
    public void setIcon(String iconName, Button button) {
        String path = iconPath + iconName;
        URL url = LanguageButtonUtils.class.getClassLoader().getResource(path);
        if (url == null) {
            throw new RuntimeException("Resources folder not found");
        }

        // Load the image
        Image image = new Image(String.valueOf(url));

        // Create an ImageView to display the image
        ImageView imageView = new ImageView();

        // Set the default image
        imageView.setImage(image);

        // Set the size of the image
        imageView.setFitWidth(15); // Set width to 100 pixels
        imageView.setFitHeight(15); // Set height to 100 pixels
        button.setGraphic(imageView);
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
        double amount = 0;
        if (event != null) {
            for (int i = 0; i < event.getExpenses().size(); i++) {
                Expense expense = foreignCurrency(event.getExpenses().get(i));
                amount += expense.getAmount();
                visualizeExpense(tabPaneAllGridPane, expense, i);
            }
            fromPersonTabName();
            includingPersonTabName();
            setAmount(amount);
        }
    }

    /**
     * Get a label for the tag
     * @param i - the number of the row
     * @return the label
     */
    private Label getTagLabel(int i) {
        Tag tag = event.getExpenses().get(i).getTag();
        Label tagLabel = new Label();
        if(tag != null) {
            tagLabel = new Label(tag.getType());
            tagLabel.setBackground(Background.fill(Color.web(tag.getColor())));
            if(Color.web(tag.getColor()).getBrightness() < 0.5) {
                tagLabel.setStyle("-fx-text-fill: white; -fx-border-color: black");
            }
            else {
                tagLabel.setStyle("-fx-text-fill: black; -fx-border-color: black");
            }
        }
        tagLabel.setMinHeight(20);
        tagLabel.setMinWidth(80);
        tagLabel.setAlignment(Pos.CENTER);
        return tagLabel;
    }

    /**
     * Method to be executed when tabPaneFromPerson is clicked
     */

    @FXML
    public void tabPaneFromPersonClick() {
        tabPaneFromGridPane.getChildren().clear();
        tabPaneFromGridPane.setVgap(10);
        tabPaneFromGridPane.setHgap(10);
        double amount = 0;
        int j = 0;
        if (event != null) {
            for (int i = 0; i < event.getExpenses().size(); i++) {
                Expense expense = foreignCurrency(event.getExpenses().get(i));
                Participant payingParticipant = expense.getPayingParticipant();
                if (payingParticipant.equals(participantsMenu.
                        getSelectionModel().getSelectedItem())) {
                    amount += expense.getAmount();
                    visualizeExpense(tabPaneFromGridPane, expense, j++);
                }
            }
            fromPersonTabName();
            includingPersonTabName();
            setAmount(amount);
        }
    }

    /**
     * Method to be executed when tabPaneIncludingPerson is clicked
     */

    @FXML
    public void tabPaneIncludingPersonClick() {
        tabPaneIncludingGridPane.getChildren().clear();
        tabPaneIncludingGridPane.setVgap(10);
        tabPaneIncludingGridPane.setHgap(10);
        double amount = 0;
        int j = 0;
        if (event != null) {
            for (int i = 0; i < event.getExpenses().size(); i++) {
                Participant participant = participantsMenu.getSelectionModel().getSelectedItem();
                Expense expense = foreignCurrency(event.getExpenses().get(i));
                if (expense.getParticipants().contains(participant)) {
                    amount += expense.getAmount();
                    visualizeExpense(tabPaneIncludingGridPane, expense, j++);
                }
            }
            fromPersonTabName();
            includingPersonTabName();
            setAmount(amount);
        }
    }

    /**
     * Sets the Grid Pane with the necessary expense info
     * @param gridPane
     * @param expense
     * @param i
     */

    @FXML
    private void visualizeExpense(GridPane gridPane, Expense expense, int i) {
        Label dateLabel = new Label(expense.getDateTime());
        Label infoLabel = new Label(expense.getActivity());
        Button editButton = new Button();
        Label tagLabel = getTagLabel(i);
        GridPane.setVgrow(editButton, Priority.ALWAYS); // Allow label to grow vertically

        Text participantsText = new Text(); // Create a Text node for participants
        participantsText.setText(setParticipantsText(expense));

        dateLabel.setWrapText(true); // Wrap text to prevent truncation
        infoLabel.setWrapText(true); // Wrap text to prevent truncation
        infoLabel.setMaxHeight(Double.MAX_VALUE); // Allow label to grow vertically
        infoLabel.setMaxWidth(Double.MAX_VALUE); // Allow label to grow horizontally
        GridPane.setVgrow(infoLabel, Priority.ALWAYS); // Allow label to grow vertically
        GridPane.setMargin(dateLabel, new Insets(0, 0, 0, 10));

        GridPane innerPane = new GridPane();
        innerPane.add(infoLabel, 0, 0);
        innerPane.add(participantsText, 0, 1);

        gridPane.add(dateLabel, 0, i);
        gridPane.add(innerPane, 1, i); // Add innerPane to gridPane at column 1
        tabPaneAllGridPane.add(tagLabel, 3, i);
        gridPane.add(editButton, 4, i);

        editButton.setOnAction(event -> onEditExpenseClick(expense));
        setIcon("graypencil.png", editButton);
    }


    /**
     * Sets the text to display which participants are
     * participating in an expense.
     * @param expense
     * @return a string of participants.
     */
    public String setParticipantsText(Expense expense) {
        if(expense.getParticipants().equals(event.getParticipants())) {
            return "(all)";
        }
        String participantsText = "(";
        for(int i = 0; i < expense.getParticipants().size(); i++) {
            participantsText += expense.getParticipants().get(i).getName();
            if(i != expense.getParticipants().size() - 1) {
                participantsText += ", ";
            }
            else {
                participantsText += ")";
            }
        }
        return participantsText;
    };

    /**
     * Sets the amount (in the set currency)
     * @param amount
     */

    @FXML
    public void setAmount(double amount) {
        // Will add the currency later
        amountText.setText("Amount: " + amount);
    }

    /**
     * Populates the Participant Menu with the participants of the event
     */
    @FXML
    public void populateParticipantMenu() {
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
            Participant selectedParticipant = participantsMenu.getValue();
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
            tabPaneIncludingPerson.setText(languageResourceBundle.
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
            event = server.getEvent(event.getId());
            if(event == null){
                mainCtrl.showStartScreen();
            }

            populateParticipantMenu();

            participantsMenu.getSelectionModel().selectFirst();
            eventTitleLabel.setText(event.getTitle());
            participatingParticipants();
            fromPersonTabName();
            includingPersonTabName();
            participantsMenu.setOnAction(e -> {
                fromPersonTabName();
                includingPersonTabName();
                tabPaneAllClick();
                tabPaneIncludingPersonClick();
            });
            tabPaneAllClick();
        }
        
        server.registerEventUpdate(event -> {
            if(this.event.getId() == event.getId()) {
                this.event = server.getEvent(event.getId());
                Platform.runLater(this::initialize);
            }
        });

        setIcon("graypencil.png", overviewEditParticipantButton);
        setIcon("addperson.png", overviewAddParticipantButton);
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

    /**
     * Stop the thread
     */
    public void stop() {
        server.stop();
    }
}
