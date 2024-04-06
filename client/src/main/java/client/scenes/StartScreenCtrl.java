package client.scenes;

import client.utils.ConfigClient;
import client.utils.LanguageButtonUtils;
import client.utils.LanguageResourceBundle;
import com.google.inject.Inject;

import client.utils.ServerUtils;
import commons.Event;
import commons.Participant;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class StartScreenCtrl {

    private ConfigClient config = new ConfigClient();

    private String[] keys = {"serverUrl", "email", "iban", "bic", "language",
                             "currency", "name", "recentEvents"};
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private LanguageResourceBundle languageResourceBundle;

    @FXML
    private TextField newEventText;

    @FXML
    private TextField joinEventText;

    @FXML
    private Text newEventStaticText;

    @FXML
    private Text joinEventStaticText;

    @FXML
    private Text recentEventsText;

    @FXML
    private Button settingsButton;

    @FXML
    private Button createEventButton;

    @FXML
    private Button joinEventButton;

    @FXML
    private ListView<String> recentlyViewedEventsListView;

    @FXML
    private MenuButton languageButton;

    /**
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
        recentlyViewedEventsListView.getItems().clear();

        languageButton.getItems().clear();

        config = config.readFromFile("config.txt");

        String language = config.getLanguage();
        if (language != null) {
            languageResourceBundle = LanguageResourceBundle.getInstance();

            languageResourceBundle.switchLanguage(language);

            LanguageButtonUtils.updateLanguageMenuButton(languageButton, config);

            LanguageButtonUtils.languageMenu(languageButton, config,
                    languageResourceBundle, this::initialize, keys);

            languageButton.setPopupSide(Side.TOP);

            switchTextLanguage();
        }

        if (config.getRecentEvents() == null) {
            return;
        }

        setupRecentlyViewedEvents();
    }

    private void setupRecentlyViewedEvents() {
        HashMap<String, String> eventMap = new HashMap<>();
        String[] recentEvents = config.getRecentEvents().split(", ");
        for (String invite : recentEvents) {
            if (!invite.equals("null")) {
                Event event = server.getEventByCode(invite);
                if (event != null) {
                    recentlyViewedEventsListView.getItems().add(event.getTitle());
                    eventMap.put(event.getTitle(), invite);
                }
            }
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
                    Button button1 = new Button(languageResourceBundle
                            .getResourceBundle().getString("goToEventText"));
                    Button button2 = new Button(languageResourceBundle
                            .getResourceBundle().getString("removeFromRecentText"));

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
     * Switches the text language.
     */
    public void switchTextLanguage() {

        ResourceBundle bundle = languageResourceBundle.getResourceBundle();
        settingsButton.setText(bundle.getString("settingsButtonText"));
        newEventStaticText.setText(bundle.getString("newEventStaticText"));
        joinEventStaticText.setText(bundle.getString("joinEventStaticText"));
        recentEventsText.setText(bundle.getString("recentEventsText"));
        createEventButton.setText(bundle.getString("createEventButton"));
        joinEventButton.setText(bundle.getString("joinEventButton"));
    }

    /**
     * Should allow joining events.
     */
    public void joinEvent() {
        String inviteCode = joinEventText.getText();
        if (!inviteCode.isEmpty()) {
            Event event = server.getEventByCode(inviteCode);
            if (event != null) {
                writeEventToConfig(event);
                mainCtrl.showEventOverview(event);
            }
        }
    }

    /**
     * Will be for the button that creates events.
     */
    public void createEvent() {
        String title = newEventText.getText();
        if (!title.isEmpty()) {
            Event event = new Event();
            event.setTitle(title);
            event.createInviteCode();
            if (ConfigClient.getName() != null){
                Participant participant = new Participant();
                participant.setName(ConfigClient.getName());
                if(ConfigClient.getEmail() != null){
                    participant.setEmail(ConfigClient.getEmail());
                }
                if(ConfigClient.getBic() != null){
                    participant.setBic(ConfigClient.getBic());
                }
                if(ConfigClient.getIban() != null){
                    participant.setIban(ConfigClient.getIban());
                }
                event.addParticipant(participant);
            }
            event = server.addEvent(event);
            if (event != null) {
                writeEventToConfig(event);
                mainCtrl.showInvitation(event);
            }
            else{
                clearFields();
            }
        }
    }

    /**
     * Switches to the User Settings scene
     */
    public void onSettingsClick() {
        mainCtrl.showUserSettings();
    }

    /**
     * Clears text fields.
     */
    public void clearFields() {
        newEventText.clear();
        joinEventText.clear();
        recentlyViewedEventsListView.getItems().clear();
    }

    /**
     * When the user presses enter, it triggers the
     * create or join button
     *
     * @param e
     */
    public void keyPressed(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            if (e.getSource().equals(newEventText)) {
                createEvent();
            }
            if (e.getSource().equals(joinEventText)) {
                joinEvent();
            }
        }
        if (e.isControlDown() && e.getCode() == KeyCode.W) {
            mainCtrl.closeWindow();
        }
    }

    /**
     * Writes an event to recently viewed in config.
     *
     * @param event the event
     */
    public void writeEventToConfig(Event event) {
        if (config.getRecentEvents() == null || config.getRecentEvents().equals("")) {
            config.setRecentEvents(event.getInviteCode());
        }else {
            config.setRecentEvents(config.getRecentEvents() + ", " + event.getInviteCode());
        }
        String[] contents = {config.getServerUrl(), config.getEmail(),
                config.getIban(), config.getBic(),
                config.getLanguage(), config.getCurrency(),
                config.getName(), config.getRecentEvents()};
        config.writeToFile("config.txt", contents, keys);
    }

    /**
     * Deletes an event from recently viewed in config
     *
     * @param invite - the invite code of the event
     */
    public void deleteEventFromConfig(String invite) {
        String[] recentEvents = config.getRecentEvents().split(", ");
        ArrayList<String> newRecentEvents = new ArrayList<>();
        for (String event : recentEvents) {
            if (!event.equals(invite)) {
                newRecentEvents.add(event);
            }
        }
        String newRecentEventsString = String.join(", ", newRecentEvents);
        config.setRecentEvents(newRecentEventsString);
        String[] contents = {config.getServerUrl(), config.getEmail(),
                config.getIban(), config.getBic(),
                config.getLanguage(), config.getCurrency(),
                config.getName(), config.getRecentEvents()};
        config.writeToFile("config.txt", contents, keys);

    }
}
