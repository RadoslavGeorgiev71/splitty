package client.scenes;

import client.utils.ConfigClient;
import com.google.inject.Inject;

import client.utils.ServerUtils;
import commons.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class StartScreenCtrl {

    private ConfigClient config = new ConfigClient();

    private String[] keys = {"serverUrl", "email", "iban", "bic", "language",
        "currency", "name", "recentEvents"};
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField newEventText;

    @FXML
    private TextField joinEventText;

    @FXML
    private Button createEventButton;

    @FXML
    private Button joinEventButton;

    @FXML
    private ListView<String> recentlyViewedEventsListView;

    /**
     *
     * @param server
     * @param mainCtrl
     */
    @Inject
    public StartScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Should theoretically create a cellFactory for the ListView above so that
     * it can create custom entities: a box with text and 2 buttons.
     * text: name of event
     * button1: button that takes you to event
     * button2: delete from recently viewed.
     * Functionality not yet there though.
     */
    public void initialize() {

        config = config.readFromFile("client/src/main/resources/config.txt");
        if(config.getRecentEvents() == null){
            return;
        }
        HashMap<String, String> eventMap = new HashMap<>();
        String[] recentEvents = config.getRecentEvents().split(", ");
        for (String invite : recentEvents) {
            Event event = server.getEventByCode(invite);
            recentlyViewedEventsListView.getItems().add(event.getTitle());
            eventMap.put(event.getTitle(), invite);
        }
        recentlyViewedEventsListView.setCellFactory(param -> new ListCell<String>() {
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {

                    HBox hbox = new HBox(10);

                    Text text = new Text(item);
                    Button button1 = new Button("Go to event");
                    Button button2 = new Button("Remove");

                    hbox.getChildren().addAll(text, button1, button2);

                    button1.setOnAction(event -> {
                        clearFields();
                        mainCtrl.showEventOverview(server.getEventByCode(eventMap.get(item)));
                    });

                    button2.setOnAction(event -> {
                        String inviteCode = eventMap.get(item);
                        getListView().getItems().remove(getItem());
                        deleteEventFromConfig(inviteCode);
                    });

                    setGraphic(hbox);
                }
            }
        });
    }

    /**
     * Should allow joining events.
     *
     */
    public void joinEvent(){
        String inviteCode = joinEventText.getText();
        if(!inviteCode.isEmpty()){
            Event event = server.getEventByCode(inviteCode);
            writeEventToConfig(event);
            mainCtrl.showEventOverview(event);
        }
    }

    /**
     * Will be for the button that creates events.
     */
    public void createEvent(){
        String title = newEventText.getText();
        if(!title.isEmpty()){
            Event event = new Event();
            event.setTitle(title);
            event.createInviteCode();
            event = server.addEvent(event);
            writeEventToConfig(event);
            mainCtrl.showInvitation(event);
        }
    }
    /**
     * Clears text fields.
     */
    public void clearFields(){
        newEventText.clear();
        joinEventText.clear();
        recentlyViewedEventsListView.getItems().clear();
    }

    /**
     * When the user presses enter, it triggers the
     * create or join button
     * @param e
     */
    public void keyPressed(KeyEvent e) {
        if(e.getCode() == KeyCode.ENTER){
            if(e.getSource().equals(newEventText)){
                createEvent();
            }
            if(e.getSource().equals(joinEventText)){
                joinEvent();
            }
        }
    }

    /**
     * Writes an event to recently viewed in config.
     * @param event the event
     */
    public void writeEventToConfig(Event event){
        if(config.getRecentEvents() == null){
            config.setRecentEvents(event.getInviteCode());
        }
        else if(config.getRecentEvents().equals("")){
            config.setRecentEvents(event.getInviteCode());
        } else {
            config.setRecentEvents(config.getRecentEvents() + ", " + event.getInviteCode());
        }
        String[] contents = {config.getServerUrl(), config.getEmail(),
                config.getIban(), config.getBic(),
                config.getLanguage(), config.getCurrency(),
                config.getName(), config.getRecentEvents()};
        config.writeToFile("client/src/main/resources/config.txt", contents, keys);
    }

    /**
     * Deletes an event from recently viewed in config
     * @param invite - the invite code of the event
     */
    public void deleteEventFromConfig(String invite){
        String[] recentEvents = config.getRecentEvents().split(", ");
        ArrayList<String> newRecentEvents = new ArrayList<>();
        for(String event : recentEvents){
            if(!event.equals(invite)){
                newRecentEvents.add(event);
            }
        }
        String newRecentEventsString = String.join(", ", newRecentEvents);
        config.setRecentEvents(newRecentEventsString);
        String[] contents = {config.getServerUrl(), config.getEmail(),
                config.getIban(), config.getBic(),
                config.getLanguage(), config.getCurrency(),
                config.getName(), config.getRecentEvents()};
        config.writeToFile("client/src/main/resources/config.txt", contents, keys);

    }
}
