package scenes;

import com.google.inject.Inject;
import commons.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import utils.Admin;



public class OverviewCtrl {
    private final Admin admin;
    private final MainCtrl mainCtrl;
    @FXML
    private Button deleteButton;

    @FXML
    private Button dumpButton;

    @FXML
    private Button importButton;

    @FXML
    private TableView<Event> table;

    @FXML
    private TableColumn<Event, String> titleColumn;

    @FXML
    private TableColumn<Event, String> creationDateColumn;

    @FXML
    private TableColumn<Event, String> lastActivityColumn;

    private  static ObservableList<Event> events = FXCollections.observableArrayList();


    /**
     * Constructor for OverviewCtrl for dependency injection
     * @param admin
     * @param mainCtrl
     */
    @Inject
    public OverviewCtrl(Admin admin, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.admin = admin;
    }

    @FXML
    void delete(ActionEvent event) {
        Event selectedEvent = table.getSelectionModel().getSelectedItem();
        if (selectedEvent != null){
            admin.deleteEvent(selectedEvent);
            table.getItems().remove(selectedEvent);
            initialize();
        }
    }

    @FXML
    void dumpJSON(ActionEvent event) {
        Event selectedEvent = table.getSelectionModel().getSelectedItem();
        if (selectedEvent != null){
            long eventID = selectedEvent.getId();
            Boolean success = admin.jsonDump(eventID);
            if(success){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("JSON dump");
                alert.setHeaderText("Success");
                alert.setContentText("Event " + eventID + " has been dumped succesfully and " +
                        "can be found in the root directory");
                alert.showAndWait();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("JSON dump");
                alert.setHeaderText("Error");
                alert.setContentText("Event " + eventID + " could not be dumped due to an error");
                alert.showAndWait();
            }
        }
    }

    @FXML
    void importJSON(ActionEvent event) {

    }

    void initialize(){
        events = FXCollections.observableArrayList(admin.getEvents());
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        lastActivityColumn.setCellValueFactory(new PropertyValueFactory<>("LastActivity"));
        creationDateColumn.setCellValueFactory(new PropertyValueFactory<>("DateTime"));
        table.setItems(events);
        table.getSelectionModel().selectFirst();
    }

}
