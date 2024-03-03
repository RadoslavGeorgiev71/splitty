package client.scenes;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class OpenDebtsCtrl implements Initializable {
    @FXML
    public Label first_debt;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO: initialize with appropriate text
        first_debt.setText("debt one");
    }

}
