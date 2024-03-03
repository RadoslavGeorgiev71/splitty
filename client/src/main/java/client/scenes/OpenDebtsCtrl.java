package client.scenes;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;

import java.net.URL;
import java.util.ResourceBundle;

public class OpenDebtsCtrl implements Initializable {
    @FXML
    public Label first_debt;
    @FXML
    public TitledPane first_debt_1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO: initialize with appropriate text
        first_debt.setText("debt one");
    }

}
