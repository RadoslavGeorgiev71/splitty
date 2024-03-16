package client.scenes;

import client.utils.ServerUtils;
import commons.Debt;
import commons.Participant;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
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
    private TitledPane[] titledPanes;
    private TextFlow[] textFlows;
    private Button[] buttonReceived;

    @FXML
    private GridPane gridPane;

    /**
     * Constructor for the controller
     * with injected server and mainCtrl
     *
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
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gridPane.setAlignment(Pos.CENTER);
//        gridPane.setStyle("-fx-grid-lines-visible: true");
        this.testDebts();
        debts = getDebts();
        titledPanes = new TitledPane[debts.size()];
        textFlows = new TextFlow[debts.size()];
        buttonReceived = new Button[debts.size()];
        for (int i = 0; i < debts.size(); i++) {
            textFlows[i] = generateTextFlow(debts.get(i));
            textFlows[i].setStyle("-fx-max-height: 30");
            titledPanes[i] = new TitledPane();
            titledPanes[i].setGraphic(textFlows[i]);
            titledPanes[i].setAnimated(true);
            titledPanes[i].setExpanded(false);
            titledPanes[i].setContent(generateExpandableLabel(debts.get(i)));
            gridPane.add(titledPanes[i], 0, i, 1, 1);
            buttonReceived[i] = new Button("Mark Received");
            GridPane.setValignment(buttonReceived[i], javafx.geometry.VPos.TOP); // Align to top
            GridPane.setHalignment(buttonReceived[i], javafx.geometry.HPos.LEFT);
            GridPane.setMargin(buttonReceived[i], new Insets(0, 0, 0, 10));
            gridPane.add(buttonReceived[i], 2, i, 1, 1);
        }
    }

    /**
     * Generates the appropriate TextFlow for the debt
     *
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
     * Generates a label for the expandable part of the debt
     *
     * @param debt - the debt for which we generate the label
     * @return the appropriate label
     */
    private Label generateExpandableLabel(Debt debt) {
        String text = "";
        int size = 0;
        if (debt.getPersonOwed().getBic() != null
            && debt.getPersonOwed().getIban() != null) {
            text += "Bank information available, transfer the money to:\n" +
                "Account Holder: " + debt.getPersonOwed().getName() + "\n" +
                "IBAN: " + debt.getPersonOwed().getIban() + "\n" +
                "BIC: " + debt.getPersonOwed().getBic();
            size += 80;
        } else {
            text += "Bank information not available.";
            size += 20;
        }
        text += "\n\n";
        size += 20;
        if (debt.getPersonOwed().getEmail() != null) {
            text += "Email configured: ";
            size += 20;
        } else {
            text += "Email not configured.";
            size += 20;
        }
        Label label = new Label(text);
        label.setStyle("-fx-min-height: " + size);
        return label;
    }

    /**
     * Retrieve the debts all debts from the server
     *
     * @return the debts from the server
     */
    private List<Debt> getDebts() {
        try {
            return server.getDebts();
        } catch (WebApplicationException e) {
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
