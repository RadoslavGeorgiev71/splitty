package scenes;

import com.google.inject.Inject;
import commons.Event;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import utils.Admin;

import java.util.List;

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
    private TableView<?> table;

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

    }

    @FXML
    void dumpJSON(ActionEvent event) {

    }

    @FXML
    void importJSON(ActionEvent event) {

    }

    void initialize(){
        List<Event> events = admin.getEvents();
        System.out.println(events);
    }

}
