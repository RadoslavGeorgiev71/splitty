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


public class EventOverviewCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private ChoiceBox participantsMenu;

    @FXML
    private GridPane tabPaneAllGridPane;

    @FXML
    private ListView tabPaneFromPersonListView;

    @FXML
    private ListView tabPaneIncludingPersonListView;

    @FXML
    private Text ParticipatingParticipants;

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

    @FXML
    public void onEditEventClick() {
    }

    @FXML
    public void onEditParticipantsClick() {
        mainCtrl.showEditParticipant(this.event, (Participant) participantsMenu.getValue());
    }

    @FXML
    public void onAddParticipantClick() {
    }

    @FXML
    public void onAddExpenseClick() {
        // Doesn't work
        mainCtrl.showAdd();
    }

    @FXML
    public void onSettleDebtsClick() {
    }

    @FXML
    public void onBackClick() {
        mainCtrl.showOverview();
    }

    @FXML
    public void tabPaneAllClick() {
        tabPaneAllGridPane.getChildren().clear();
        tabPaneAllGridPane.setVgap(10);
        tabPaneAllGridPane.setHgap(75);
        if(event != null) {
            for (int i = 0; i < event.getExpenses().size(); i++) {
                Label dateLabel = new Label("dateTime");
                Label nameLabel = new Label("expense creator");
                Button editButton = new Button("Edit");
                dateLabel.setWrapText(true);
                dateLabel.setWrapText(true);
                dateLabel.setWrapText(true);

                //editButton.setOnAction()

                tabPaneAllGridPane.add(dateLabel, 0, i);
                tabPaneAllGridPane.add(nameLabel, 1, i);
                tabPaneAllGridPane.add(editButton, 2, i);
            }
        }
    }

    @FXML
    public void tabPaneFromPersonClick() {
        tabPaneFromPersonListView.getItems().clear();
    }

    @FXML
    public void tabPaneIncludingPersonClick() {
        tabPaneIncludingPersonListView.getItems().clear();
    }

    /**
     * Sets the event
     * @param event
     */
    public void setEvent(Event event){
        this.event = event;
    }

    /**
     * Sets the text that displays all the participants in the event
     */
    public void participatingParticipants() {
        String participantsText = "";
        if(event != null) {
            for (int i = 0; i < event.getParticipants().size(); i++) {
                participantsText += event.getParticipants().get(i).getName();
                if(i < event.getParticipants().size() - 1) {
                    participantsText += ", ";
                }
            }
        }
        ParticipatingParticipants.setText(participantsText);
    }

    public void initialize(){
        if(event != null){
            participantsMenu.setItems(FXCollections.observableArrayList(event.getParticipants()));
            participantsMenu.setConverter(new StringConverter<Participant>() {
                @Override
                public String toString(Participant participant) {
                    if(participant != null)
                        return participant.getName();
                    else
                        return "";
                }
                @Override
                public Participant fromString(String string) {
                    return null;
                }
            } );
            participantsMenu.getSelectionModel().selectFirst();
        }

        tabPaneAllClick();
        participatingParticipants();
    }
}