package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import commons.Participant;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.ArrayList;


public class AddParticipantCtrlTest extends ApplicationTest {

    ServerUtils serverMock;

    MainCtrl mainCtrlMock;

    private AddParticipantCtrl addParticipantCtrl;

    @BeforeAll
    public static void setupSpec() throws Exception {
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
        System.setProperty("java.awt.headless", "true");
    }

    @Override
    public void start(Stage stage) throws Exception{
        serverMock = Mockito.mock(ServerUtils.class);
        mainCtrlMock = Mockito.mock(MainCtrl.class);
        Event mockEvent = new Event();
        mockEvent.setTitle("Test");
        mockEvent.setParticipants(new ArrayList<>());
        mockEvent.setExpenses(new ArrayList<>());
        mockEvent.setInviteCode("testInviteCode");
        addParticipantCtrl = new AddParticipantCtrl(serverMock, mainCtrlMock);
        addParticipantCtrl.setEvent(mockEvent);

        Mockito.doNothing().when(mainCtrlMock).showEventOverview(mockEvent);
        Mockito.when(serverMock.getEvent(Mockito.anyLong())).thenReturn(mockEvent);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/scenes/AddParticipant.fxml"));
        loader.setControllerFactory(type -> {
            if (type == AddParticipantCtrl.class) {
                return addParticipantCtrl;
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
        clickOn("#participantCancelButton");
        Mockito.verify(mainCtrlMock).showEventOverview(Mockito.any(Event.class));
    }

    @Test
    public void testOk() {

        clickOn("#nameField").write("Test");
        clickOn("#emailField").write("Test");
        clickOn("#ibanField").write("Test");
        clickOn("#bicField").write("Test");

        clickOn("#participantOkButton");
        Mockito.verify(serverMock).persistEvent(Mockito.any(Event.class));
        Mockito.verify(mainCtrlMock).showEventOverview(Mockito.any(Event.class));
    }

//    @Test
//    public void testKeyPressed(){
//        clickOn("#nameField").push(javafx.scene.input.KeyCode.ENTER);
//        Mockito.verify(serverMock).persistEvent(Mockito.any(Event.class));
//        Mockito.verify(mainCtrlMock).showEventOverview(Mockito.any(Event.class));
//
//        clickOn("#nameField").push(javafx.scene.input.KeyCode.ESCAPE);
//        Mockito.verify(mainCtrlMock, Mockito.times(2)).showEventOverview(Mockito.any(Event.class));
//
//    }

    @Test
    public void testInitialize(){
        addParticipantCtrl.initialize();
    }
}
