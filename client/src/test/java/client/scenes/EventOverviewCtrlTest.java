package client.scenes;

import client.utils.ConfigClient;
import client.utils.ServerUtils;
import commons.Event;
import commons.Expense;
import commons.Participant;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventOverviewCtrlTest extends ApplicationTest {
    ServerUtils mockServer;
    ConfigClient mockConfig;
    MainCtrl mockMainCtrl;
    Event mockEvent;
    Expense mockExpense;

    Participant mockParticipant;

    private EventOverviewCtrl eventOverviewCtrl;

    @Override
    public void start(Stage stage) throws Exception{
        mockServer = Mockito.mock(ServerUtils.class);
        mockConfig = Mockito.mock(ConfigClient.class);
        mockMainCtrl = Mockito.mock(MainCtrl.class);

        mockEvent = new Event();
        mockEvent.setTitle("Test");
        mockEvent.setParticipants(new ArrayList<>());

        mockParticipant = new Participant();
        mockParticipant.setName("testParticipant");
        mockParticipant.setBic("testBic");
        mockParticipant.setIban("testIban");
        mockParticipant.setEmail("testEmail");

        mockEvent.addParticipant(mockParticipant);
        mockEvent.setExpenses(new ArrayList<>());
        mockEvent.setInviteCode("testInviteCode");

        mockExpense = new Expense();
        mockExpense.setAmount(100);
        mockExpense.setParticipants(mockEvent.getParticipants());
        mockExpense.setTitle("testExpense");
        mockExpense.setDateTime("2024-03-31");
        mockExpense.setPayingParticipant(mockParticipant);
        mockEvent.addExpense(mockExpense);

        Mockito.doNothing().when(mockMainCtrl).showInvitation(mockEvent);
        Mockito.doNothing().when(mockMainCtrl).showEditParticipant(mockEvent, mockParticipant);
        Mockito.doNothing().when(mockMainCtrl).showAddParticipant(mockEvent);
        Mockito.doNothing().when(mockMainCtrl).showAddExpense(mockEvent);
        Mockito.doNothing().when(mockMainCtrl).showOpenDebts(mockEvent);
        Mockito.doNothing().when(mockMainCtrl).showStartScreen();


        eventOverviewCtrl = new EventOverviewCtrl(mockServer, mockMainCtrl);
        eventOverviewCtrl.setEvent(mockEvent);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/scenes/EventOverview.fxml"));
        loader.setControllerFactory(type -> {
            if (type == EventOverviewCtrl.class) {
                return eventOverviewCtrl;
            } else {
                throw new RuntimeException("Requested unknown controller type");
            }
        });

        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();

    }

    @Test
    public void testInitialize(){
        Platform.runLater(() -> {
            eventOverviewCtrl.initialize();
        });
    }

    @Test
    public void testOnSendInvites() {
        Platform.runLater(() -> {
            eventOverviewCtrl.onSendInvites(new ActionEvent());
        });
        WaitForAsyncUtils.waitForFxEvents();
        Mockito.verify(mockMainCtrl).showInvitation(mockEvent);
    }

    @Test
    public void testOnEditParticipantsClick() {
        Platform.runLater(() -> {
            eventOverviewCtrl.onEditParticipantsClick();
        });
        WaitForAsyncUtils.waitForFxEvents();
        Mockito.verify(mockMainCtrl).showEditParticipant(mockEvent, mockParticipant);
    }

    @Test
    public void testOnAddParticipantClick() {
        Platform.runLater(() -> {
            eventOverviewCtrl.onAddParticipantClick();
        });
        WaitForAsyncUtils.waitForFxEvents();
        Mockito.verify(mockMainCtrl).showAddParticipant(mockEvent);
    }

    @Test
    public void testOnAddExpenseClick() {
        Platform.runLater(() -> {
            eventOverviewCtrl.onAddExpenseClick();
        });
        WaitForAsyncUtils.waitForFxEvents();
        Mockito.verify(mockMainCtrl).showAddExpense(mockEvent);
    }

    @Test
    public void testOnSettleDebtsClick() {
        Platform.runLater(() -> {
            eventOverviewCtrl.onSettleDebtsClick();
        });
        WaitForAsyncUtils.waitForFxEvents();
        Mockito.verify(mockMainCtrl).showOpenDebts(mockEvent);
    }

    @Test
    public void testOnBackClick() {
        Platform.runLater(() -> {
            eventOverviewCtrl.onBackClick();
        });
        interact(() -> {
            DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);
            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            okButton.fire();
        });
        WaitForAsyncUtils.waitForFxEvents();
        Mockito.verify(mockMainCtrl).showStartScreen();
    }

    @Test
    public void testTabPaneFromPersonClick() throws Exception {
        Field participantsMenuField = EventOverviewCtrl.class.getDeclaredField("participantsMenu");
        Field tabPaneFromGridPaneField = EventOverviewCtrl.class.getDeclaredField("tabPaneFromGridPane");

        participantsMenuField.setAccessible(true);
        tabPaneFromGridPaneField.setAccessible(true);

        ChoiceBox participantsMenu = (ChoiceBox) participantsMenuField.get(eventOverviewCtrl);
        GridPane tabPaneFromGridPane = (GridPane) tabPaneFromGridPaneField.get(eventOverviewCtrl);

        Participant selectedParticipant = mockEvent.getParticipants().get(0);
        Platform.runLater(() -> {
            participantsMenu.getSelectionModel().select(selectedParticipant);
        });
        WaitForAsyncUtils.waitForFxEvents();

        Platform.runLater(() -> {
            eventOverviewCtrl.tabPaneFromPersonClick();
        });
        WaitForAsyncUtils.waitForFxEvents();

        int numExpenses = mockEvent.getExpenses().size();
        assertEquals(numExpenses, tabPaneFromGridPane.getChildren().size() / 3);
    }

    @Test
    public void testTabPaneAllClick() throws Exception {
        Field tabPaneAllGridPaneField = EventOverviewCtrl.class.getDeclaredField("tabPaneAllGridPane");

        tabPaneAllGridPaneField.setAccessible(true);

        GridPane tabPaneAllGridPane = (GridPane) tabPaneAllGridPaneField.get(eventOverviewCtrl);

        Platform.runLater(() -> {
            eventOverviewCtrl.tabPaneAllClick();
        });
        WaitForAsyncUtils.waitForFxEvents();

        int numExpenses = mockEvent.getExpenses().size();
        assertEquals(numExpenses, tabPaneAllGridPane.getChildren().size() / 3);
    }

    @Test
    public void testEditParticipantEmpty(){
        mockEvent.setParticipants(new ArrayList<>());
        Platform.runLater(() -> {
            eventOverviewCtrl.onEditParticipantsClick();
        });
        WaitForAsyncUtils.waitForFxEvents();

        interact(() -> {
            DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);
            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            okButton.fire();
        });

        WaitForAsyncUtils.waitForFxEvents();
        Mockito.verify(mockMainCtrl).showEventOverview(mockEvent);
    }

    @Test
    public void fromPersonTabNameEmpty(){
        mockEvent.setParticipants(new ArrayList<>());
        Platform.runLater(() -> {
            eventOverviewCtrl.fromPersonTabName();
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    public void includingPersonTabNameEmpty(){
        mockEvent.setParticipants(new ArrayList<>());
        Platform.runLater(() -> {
            eventOverviewCtrl.tabPaneAllClick();
        });
        WaitForAsyncUtils.waitForFxEvents();
    }
}
