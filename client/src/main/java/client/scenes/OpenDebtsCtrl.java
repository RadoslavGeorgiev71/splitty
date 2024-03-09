package client.scenes;

import client.utils.ServerUtils;
import commons.Debt;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;

import com.google.inject.Inject;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class OpenDebtsCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private GridPane gridPane;

    /**
     * Constructor for the controller
     * with injected server and mainCtrl
     * @param server
     * @param mainCtrl
     */
    @Inject
    public OpenDebtsCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Initializes the stage and all its required components
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO: initialize with appropriate text
          List<Debt> debts = initialGetDebts();
//        for (int i = 0; i < debts.size(); i++) {
//            TitledPane tp = new TitledPane();
//            tp.setText(debts.get(i).getPersonPaying().getName() +
//                " gives " + debts.get(i).getAmount() + " to " + debts.get(i).getPersonOwed());
//            gridPane.add(tp, 1, i + 1, 1, 1);
//            Button button = new Button("Mark Received");
//            gridPane.add(button, 3, i + 1, 1, 1);
//        }
    }

    /**
     * Retrieve the debts all debts from the server
     * @return the debts from the server
     */
    public List<Debt> initialGetDebts() {
        try {
            return server.getDebts();
        }
        catch (WebApplicationException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return null;
        }
    }

}
