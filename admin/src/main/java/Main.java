
import static com.google.inject.Guice.createInjector;

//import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;


import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;
import scenes.LoginCtrl;
import scenes.MainCtrl;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);
    /**
     * Launches the app
     * @param args
     * @throws URISyntaxException
     * @throws IOException
     */
    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }

    /**
     *
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws IOException
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        var login = FXML.load(LoginCtrl.class, "admin", "scenes", "Login.fxml");

        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        mainCtrl.initialize(primaryStage, login);
    }
}