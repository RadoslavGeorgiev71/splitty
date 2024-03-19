package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Participant;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import javafx.collections.FXCollections;

import javafx.event.ActionEvent;



public class EventOverviewCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private ChoiceBox participantsMenu;

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
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("There are no available participants to edit");
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
        alert.setTitle("Exit");
        alert.setHeaderText("You will exit this event and go back to start screen");
        alert.setContentText("Are you sure you want to exit?");
        if (alert.showAndWait().get() == ButtonType.OK){
            mainCtrl.showStartScreen();
        }

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
                Label dateLabel = new Label(event.getExpenses().get(i).getDateTime());
                Label nameLabel = new Label("expense creator");
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
            for (int i = 0; i < event.getExpenses().size(); i++) {
                if (event.getParticipants().get(i).equals(participantsMenu.
                        getSelectionModel().getSelectedItem())) {
                    Label dateLabel = new Label(event.getExpenses().get(i).getDateTime());
                    Label nameLabel = new Label("expense creator");
                    nameLabel.setWrapText(true); // Wrap text to prevent truncation
                    Button editButton = new Button("Edit");

                    // Set fixed column widths
                    dateLabel.setMaxWidth(Double.MAX_VALUE);
                    nameLabel.setMaxWidth(Double.MAX_VALUE);

                    GridPane.setFillWidth(dateLabel, true);
                    GridPane.setFillWidth(nameLabel, true);

                    tabPaneFromGridPane.add(dateLabel, 0, i);
                    tabPaneFromGridPane.add(nameLabel, 1, i);
                    tabPaneFromGridPane.add(editButton, 2, i);
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
            tabPaneFromPerson.setText("From");
        }
        else {
            Participant selectedParticipant = (Participant) participantsMenu.getValue();
            tabPaneFromPerson.setText("From " + selectedParticipant.getName());
        }

    }

    /**
     * Sets the Including tab with the current selected participant name
     */
    public void includingPersonTabName() {
        if (event.getParticipants().isEmpty()){
            tabPaneFromPerson.setText("Including");
        }
        else {
            Participant selectedParticipant = (Participant) participantsMenu.getValue();
            tabPaneIncludingPerson.setText("Including " + selectedParticipant.getName());
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
     * Initializes the event overview
     */

    public void initialize(){
        if (event != null){
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
    }


}