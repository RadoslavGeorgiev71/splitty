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
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;

import com.google.inject.Inject;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class OpenDebtsCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private List<Debt> debts;
    private boolean[] expanded;
    private TextFlow[] textFlows;
    private Pane[] expandedMenus;
    private Button[] buttonReceived;

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
        debts = getDebts();
        expanded = new boolean[debts.size()];
        textFlows = new TextFlow[debts.size()];
        expandedMenus = new Pane[debts.size()];
        buttonReceived = new Button[debts.size()];
        for (int i = 0; i < debts.size(); i++) {
            textFlows[i] = generateTextFlow(debts.get(i));
            expandedMenus[i] = new Pane();
            int finalI = i;
            textFlows[i].setOnMouseClicked(event -> {
                System.out.println("sdgdfgdf");
//                if (expanded[finalI]) {
//                    expandedMenus[finalI].getChildren().removeAll();
//                    expanded[finalI] = false;
//                }
//                else {
//                    expandedMenus[finalI].getChildren().add(new Label("Example"));
//                    expanded[finalI] = true;
//                }
            });
            gridPane.add(textFlows[i], 0, i, 1, 1);
            gridPane.add(expandedMenus[i], 0, i, 3, 1);
            buttonReceived[i] = new Button("Mark Received");
            gridPane.add(buttonReceived[i], 2, i, 1, 1);
        }
    }

    /**
     * Generates the appropriate TextFlow for the debt
     * @param debt - the debt for which we generate TextFlow
     * @return the appropriate TextFlow
     */
    private TextFlow generateTextFlow(Debt debt) {
        TextFlow tf = new TextFlow();
        Text payer = new Text(debt.getPersonPaying().getName());
        payer.setStyle("-fx-font-weight: bold");
        Text gives = new Text(" gives ");
        Text amount = new Text(Double.toString(debt.getAmount()));
        amount.setStyle("-fx-font-weight: bold");
        Text to = new Text(" to ");
        Text receiver = new Text(debt.getPersonOwed().getName());
        receiver.setStyle("-fx-font-weight: bold");
        tf.getChildren().addAll(payer, gives, amount, to, receiver);
        tf.setStyle("-fx-alignment: center-left; -fx-padding: 10");
        return tf;
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
