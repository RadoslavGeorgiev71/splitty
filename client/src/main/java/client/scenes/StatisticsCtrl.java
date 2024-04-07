package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import jakarta.inject.Inject;
import javafx.fxml.FXML;

public class StatisticsCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Event event;

    /**
     * Constructor for the controller
     * with injected server and mainCtrl
     * @param server - the server
     * @param mainCtrl - the mainCtrl
     */
    @Inject
    public StatisticsCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void initialize() {
        if(this.event != null) {

        }
    }

    /**
     * Set the event for the controller
     * @param event - the event to be set
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    @FXML
    public void onBackClick() {
        mainCtrl.showEventOverview(event);
    }
}
