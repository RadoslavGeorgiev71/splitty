package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Participant;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

import javafx.util.StringConverter;
import javafx.collections.FXCollections;


public class EventOverviewCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private ChoiceBox participantsMenu;

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
    }

    @FXML
    public void onSettleDebtsClick() {
    }

    public void setEvent(Event event){
        this.event = event;
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

    }
}