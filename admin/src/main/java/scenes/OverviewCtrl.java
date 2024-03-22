package scenes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import commons.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.Admin;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


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
        Stage filestage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        String userDir = "admin";
        File defaultDirectory = new File(userDir);
        fileChooser.setInitialDirectory(defaultDirectory);
        File selectedFile = fileChooser.showOpenDialog(filestage);
        if (selectedFile != null) {
            try {
                List<Event> events = readEvents(
                        new Scanner(new File( selectedFile.getAbsolutePath())));
                admin.importEvents(events);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        initialize();
    }

    /**
     * Takes a scanner and parses the JSON to Events
     * Every line should be a different event
      * @param scanner to use
     * @return a list of events read
     */
    public List<Event> readEvents(Scanner scanner){
        List<Event> events = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            while(scanner.hasNextLine()){
                String json = scanner.nextLine();
                Event event = objectMapper.readValue(json, Event.class);
                events.add(event);
            }
        }
        catch (JsonProcessingException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("JSON dump");
            alert.setHeaderText("Error reading JSON file");
            alert.setContentText("Seems that the JSON file is corrupted or not well formatted. \n" +
                    "Ensure that there is one event per line");
            alert.showAndWait();
        }
        return events;
    }

    /**
     * refreshes the table
     * @param event button clicked
     */
    public void refresh(ActionEvent event){
        initialize();
    }

    /**
     * goes back to the login page
     * @param event
     */
    @FXML
    void back(ActionEvent event) {
        mainCtrl.showLogin();
    }

    /**
     * When the user presses a key, it triggers the
     * refresh method
     * @param e
     */
    public void keyPressed(KeyEvent e) {
        if(e.isControlDown() && e.getCode() == KeyCode.R){
            refresh(null);
        }
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
