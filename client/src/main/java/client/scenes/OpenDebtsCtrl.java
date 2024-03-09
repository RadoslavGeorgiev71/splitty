package client.scenes;

import client.utils.ServerUtils;
import commons.Debt;
import commons.Participant;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
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
        gridPane.setAlignment(Pos.CENTER);
//        gridPane.setStyle("-fx-grid-lines-visible: true");
        // TODO: initialize with appropriate text
        this.testDebts();
        List<Debt> debts = getDebts();
        for (int i = 0; i < debts.size(); i++) {
            TextFlow tf = new TextFlow();
            Text payer = new Text(debts.get(i).getPersonPaying().getName());
            payer.setStyle("-fx-font-weight: bold");
            Text gives = new Text(" gives ");
            Text amount = new Text(Double.toString(debts.get(i).getAmount()));
            amount.setStyle("-fx-font-weight: bold");
            Text to = new Text(" to ");
            Text receiver = new Text(debts.get(i).getPersonOwed().getName());
            receiver.setStyle("-fx-font-weight: bold");
            tf.getChildren().addAll(payer, gives, amount, to, receiver);
            tf.setStyle("-fx-alignment: center-left; -fx-padding: 10");
            gridPane.add(tf, 0, i, 1, 1);
            Button button = new Button("Mark Received");
            gridPane.add(button, 2, i , 1, 1);
        }
    }

    /**
     * Retrieve the debts all debts from the server
     * @return the debts from the server
     */
    private List<Debt> getDebts() {
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

    // TODO: only to test the functionality for now, should be removed later
    private void testDebts() {
        Participant bob = new Participant("Bob");
        Participant ana = new Participant("Ana");
        for (Debt debt : server.getDebts()) {
            server.deleteDebt(debt);
        }
        server.addDebt(new Debt(5, bob, ana, 10));
        server.addDebt(new Debt(6, ana, bob, 8));
        server.addDebt(new Debt(7, ana, new Participant("Greg"), 30));
    }
}
