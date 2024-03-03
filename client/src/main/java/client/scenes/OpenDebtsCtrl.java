package client.scenes;

import client.utils.ServerUtils;
import commons.Debt;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.stage.Modality;

import javax.inject.Inject;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class OpenDebtsCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public OpenDebtsCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO: initialize with appropriate text
        List<Debt> debts = initialGetDebts();
    }

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
