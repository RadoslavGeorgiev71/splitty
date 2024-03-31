package client.scenes;

import client.MyFXML;
import client.MyModule;
import client.utils.ConfigClient;
import client.utils.LanguageButtonUtils;
import client.utils.ServerUtils;
import com.google.inject.Injector;
import commons.Event;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.ArrayList;

import static com.google.inject.Guice.createInjector;

public class StartScreenCtrlTest extends ApplicationTest {

    ServerUtils mockServer;
    ConfigClient mockConfig;
    MainCtrl mockMainCtrl;

    private StartScreenCtrl startScreenCtrl;

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);
    @Override
    public void start(Stage stage) throws Exception{
        mockServer = Mockito.mock(ServerUtils.class);
        mockConfig = Mockito.mock(ConfigClient.class);
        mockMainCtrl = Mockito.mock(MainCtrl.class);
        Event mockEvent = new Event();
        mockEvent.setTitle("Test");
        mockEvent.setParticipants(new ArrayList<>());
        mockEvent.setExpenses(new ArrayList<>());
        mockEvent.setInviteCode("testInviteCode");


        startScreenCtrl = new StartScreenCtrl(mockServer, mockMainCtrl);
        startScreenCtrl.setConfig(mockConfig);
        Mockito.when(mockConfig.getLanguage()).thenReturn("en");
        Mockito.when(mockConfig.readFromFile(Mockito.anyString())).thenReturn(mockConfig);

        Mockito.doNothing().when(mockMainCtrl).showInvitation(Mockito.any(Event.class));
        Mockito.doNothing().when(mockMainCtrl).showEventOverview(Mockito.any(Event.class));
        Mockito.when(mockServer.addEvent(Mockito.any(Event.class))).thenReturn(mockEvent);
        Mockito.when(mockServer.getEventByCode("testInviteCode")).thenReturn(mockEvent);
        Mockito.when(mockConfig.getRecentEvents()).thenReturn("testInviteCode");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/scenes/StartScreen.fxml"));

        loader.setControllerFactory(type -> {
            if (type == StartScreenCtrl.class) {
                return startScreenCtrl;
            } else {
                throw new RuntimeException("Requested unknown controller type");
            }
        });

        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    public void testClearFields(){
        WaitForAsyncUtils.waitForFxEvents();

        clickOn("#newEventText");
        write("Test Event");
        Platform.runLater(() -> {
            startScreenCtrl.clearFields();
        });
    }
    @Test
    public void testCreateEvent() {
        WaitForAsyncUtils.waitForFxEvents();

        String eventTitle = "Test Event";
        clickOn("#newEventText");
        write(eventTitle);
        clickOn("#createEventButton");
        Mockito.verify(mockServer).addEvent(Mockito.any(Event.class));
        Mockito.verify(mockMainCtrl).showInvitation(Mockito.any(Event.class));
    }

    @Test
    public void testJoinEvent() {
        String inviteCode = "testInviteCode";
        clickOn("#joinEventText");
        write(inviteCode);
        clickOn("#joinEventButton");
        Mockito.verify(mockServer, Mockito.times(2)).getEventByCode(inviteCode);
        Mockito.verify(mockMainCtrl).showEventOverview(Mockito.any(Event.class));
    }

    @Test
    public void testOnSettingsClick() {
        WaitForAsyncUtils.waitForFxEvents();

        clickOn("#settingsButton");
        Mockito.verify(mockMainCtrl).showUserSettings(Mockito.any(ConfigClient.class));
    }

    @Test
    public void testInitialize() {
        WaitForAsyncUtils.waitForFxEvents();
        Platform.runLater(() -> {
            startScreenCtrl.initialize();
        });
    }

    @Test
    public void testDeleteEventFromConfig() {
        String inviteCode = "testInviteCode";
        String recentEvents = "testInviteCode, anotherInviteCode";
        Mockito.when(mockConfig.getRecentEvents()).thenReturn(recentEvents);

        startScreenCtrl.deleteEventFromConfig(inviteCode);

        Mockito.verify(mockConfig).setRecentEvents("anotherInviteCode");
        Mockito.verify(mockConfig).writeToFile(Mockito.anyString(), Mockito.any(String[].class), Mockito.any(String[].class));

        Mockito.when(mockConfig.getRecentEvents()).thenReturn("");
        Mockito.verify(mockConfig).setRecentEvents(Mockito.anyString());

        Mockito.when(mockConfig.getRecentEvents()).thenReturn("");
        Mockito.verify(mockConfig).setRecentEvents(Mockito.anyString());
    }

    @Test
    public void testKeyPressedCreateEvent() {
        clickOn("#newEventText").write("Test Event");
        clickOn("#newEventText").push(KeyCode.ENTER);
    }

    @Test
    public void testKeyPressedJoinEvent() {
        clickOn("#joinEventText").write("testInviteCode");
        clickOn("#joinEventText").push(KeyCode.ENTER);
    }


}
