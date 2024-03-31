package client.scenes;

import client.utils.ConfigClient;
import client.utils.ServerUtils;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserSettingsCtrlTest extends ApplicationTest {

    ServerUtils serverMock;
    ConfigClient configClientMock;
    MainCtrl mainCtrlMock;

    private UserSettingsCtrl userSettingsCtrl;

    @Override
    public void start(Stage stage) throws Exception {

        serverMock = Mockito.mock(ServerUtils.class);
        mainCtrlMock = Mockito.mock(MainCtrl.class);
        configClientMock = Mockito.mock(ConfigClient.class);
        userSettingsCtrl = new UserSettingsCtrl(serverMock, mainCtrlMock);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/scenes/userSettings.fxml"));
        loader.setControllerFactory(type -> {
            if (type == UserSettingsCtrl.class) {
                return userSettingsCtrl;
            } else {
                throw new RuntimeException("Requested unknown controller type");
            }
        });

        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void testCancel() {
        userSettingsCtrl.onCancelClick();
        Mockito.verify(mainCtrlMock).showStartScreen();
    }

    @Test
    public void testConfirm() {
        Platform.runLater(() -> userSettingsCtrl.initialize(configClientMock));

        clickOn("#nameField").write("Test");
        clickOn("#emailField").write("Test");
        clickOn("#ibanField").write("Test");
        clickOn("#bicField").write("Test");

        userSettingsCtrl.onConfirmClick();
        Mockito.verify(configClientMock).setName("Test");
        Mockito.verify(configClientMock).setEmail("Test");
        Mockito.verify(configClientMock).setIban("Test");
        Mockito.verify(configClientMock).setBic("Test");
    }

    @Test
    public void testInitializeAllBranches() throws Exception {
        Platform.runLater(() -> userSettingsCtrl.initialize(configClientMock));

        WaitForAsyncUtils.waitForFxEvents();

        Field nameField = UserSettingsCtrl.class.getDeclaredField("nameField");
        nameField.setAccessible(true);
        TextField nameTextField = (TextField) nameField.get(userSettingsCtrl);

        Field emailField = UserSettingsCtrl.class.getDeclaredField("emailField");
        emailField.setAccessible(true);
        TextField emailTextField = (TextField) emailField.get(userSettingsCtrl);

        Field ibanField = UserSettingsCtrl.class.getDeclaredField("ibanField");
        ibanField.setAccessible(true);
        TextField ibanTextField = (TextField) ibanField.get(userSettingsCtrl);

        Field bicField = UserSettingsCtrl.class.getDeclaredField("bicField");
        bicField.setAccessible(true);
        TextField bicTextField = (TextField) bicField.get(userSettingsCtrl);

        assertEquals("", nameTextField.getText());
        assertEquals("", emailTextField.getText());
        assertEquals("", ibanTextField.getText());
        assertEquals("", bicTextField.getText());
    }

    @Test
    public void testInitializeAllBranchesNonNull() throws Exception {
        Mockito.when(configClientMock.getName()).thenReturn("Test name");
        Mockito.when(configClientMock.getEmail()).thenReturn("Test email");
        Mockito.when(configClientMock.getIban()).thenReturn("Test iban");
        Mockito.when(configClientMock.getBic()).thenReturn("Test bic");

        Platform.runLater(() -> userSettingsCtrl.initialize(configClientMock));

        WaitForAsyncUtils.waitForFxEvents();

        Field nameField = UserSettingsCtrl.class.getDeclaredField("nameField");
        nameField.setAccessible(true);
        TextField nameTextField = (TextField) nameField.get(userSettingsCtrl);

        Field emailField = UserSettingsCtrl.class.getDeclaredField("emailField");
        emailField.setAccessible(true);
        TextField emailTextField = (TextField) emailField.get(userSettingsCtrl);

        Field ibanField = UserSettingsCtrl.class.getDeclaredField("ibanField");
        ibanField.setAccessible(true);
        TextField ibanTextField = (TextField) ibanField.get(userSettingsCtrl);

        Field bicField = UserSettingsCtrl.class.getDeclaredField("bicField");
        bicField.setAccessible(true);
        TextField bicTextField = (TextField) bicField.get(userSettingsCtrl);

        assertEquals("Test name", nameTextField.getText());
        assertEquals("Test email", emailTextField.getText());
        assertEquals("Test iban", ibanTextField.getText());
        assertEquals("Test bic", bicTextField.getText());
    }
}
