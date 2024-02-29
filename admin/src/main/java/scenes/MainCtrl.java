package scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {


    private Stage primaryStage;

    private LoginCtrl overviewCtrl;
    private Scene overview;


    private Scene add;

    /**
     * Initializes stage
     * @param primaryStage
     * @param overview
     */
    public void initialize(Stage primaryStage, Pair<LoginCtrl, Parent> overview) {
        this.primaryStage = primaryStage;
        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());

        showOverview();
        primaryStage.show();
    }

    /**
     * Shows quote overview
     */
    public void showOverview() {
        primaryStage.setTitle("Login");
        primaryStage.setScene(overview);
    }

}
