package client.scenes;

import java.net.URL;
import java.util.ResourceBundle;

import com.google.inject.Inject;

import client.utils.ServerUtils;
import commons.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class StartScreenCtrl implements Initializable {
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
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    public void initialize(URL location, ResourceBundle resources) {
        
        recentlyViewedEventsListView.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(10);

                    Text text = new Text(item);
                    Button button1 = new Button("Button 1");
                    Button button2 = new Button("Button 2");

                    hbox.getChildren().addAll(text, button1, button2);

                    button1.setOnAction(event -> {
                        //joinEvent();
                    });

                    button2.setOnAction(event -> {
                        getListView().getItems().remove(getItem());
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
        Event event = server.getEventByCode(inviteCode);
        mainCtrl.showEventOverview(event);

    }

    /**
     * Will be for the button that creates events.
     */
    public void createEvent(){
        String title = newEventText.getText();
        Event event = new Event();
        event.setTitle(title);
        event.createInviteCode();

        event = server.addEvent(event);
        mainCtrl.showInvitation(event);
    }
    /**
     * Clears text fields.
     */
    public void clearFields(){
        newEventText.clear();
        joinEventText.clear();
    }
}
