package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import commons.Expense;
import commons.Participant;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;
import org.junit.jupiter.api.BeforeAll;

public class AddExpenseCtrlTest extends ApplicationTest {

    ServerUtils serverUtilsMock;

    MainCtrl mainCtrlMock;

    Event eventMock;

    Expense expenseMock;

    private AddExpenseCtrl addExpenseCtrl;

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
        serverUtilsMock = Mockito.mock(ServerUtils.class);
        mainCtrlMock = Mockito.mock(MainCtrl.class);
        eventMock = new Event();
        eventMock.setTitle("Test");
        eventMock.setParticipants(new ArrayList<>());
        eventMock.setExpenses(new ArrayList<>());
        eventMock.setInviteCode("testInviteCode");
        expenseMock = new Expense();

        addExpenseCtrl = new AddExpenseCtrl(serverUtilsMock, mainCtrlMock);
        addExpenseCtrl.setEvent(eventMock);
        addExpenseCtrl.setExpense(expenseMock);

        Mockito.doNothing().when(mainCtrlMock).showEventOverview(eventMock);
        Mockito.when(serverUtilsMock.getEvent(Mockito.anyLong())).thenReturn(eventMock);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/scenes/AddEditExpense.fxml"));
        loader.setControllerFactory(type -> {
            if (type == AddExpenseCtrl.class) {
                return addExpenseCtrl;
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
    public void testOnAbortClick() {
        clickOn("#expenseAbortButton");
        Mockito.verify(mainCtrlMock).showEventOverview(eventMock);
    }

//    @Test
//    public void testOnAddClick() {
//        Participant participant = new Participant();
//        participant.setName("Test Participant");
//        eventMock.getParticipants().add(participant);
//
//        Platform.runLater(() -> {
//            addExpenseCtrl.initialize();
//        });
//        waitForFxEvents();
//
//        clickOn("#titleField").write("Test");
//        clickOn("#amountField").write("10");
//        clickOn("#payerChoiceBox").clickOn("Test Participant");
//        clickOn("#datePicker").write("01/03/2024");
//        clickOn("#tags").write("Test");
//        clickOn("#expenseAddButton");
//
//        assertEquals(1, eventMock.getExpenses().size());
//        assertEquals(10.0, eventMock.getExpenses().get(0).getAmount());
//        assertEquals("Test", eventMock.getExpenses().get(0).getTitle());
//        assertEquals("Test Participant", eventMock.getExpenses().get(0).getPayingParticipant().getName());
//        assertEquals("2024-03-01", eventMock.getExpenses().get(0).getDateTime());
//
//        Mockito.verify(serverUtilsMock).persistEvent(eventMock);
//        Mockito.verify(mainCtrlMock).showEventOverview(eventMock);
//    }

    @Test
    public void testKeyPressed() {

        Participant participant = new Participant();
        participant.setName("Test Participant");
        eventMock.getParticipants().add(participant);

        Platform.runLater(() -> {
            addExpenseCtrl.initialize();
        });
        waitForFxEvents();

        clickOn("#titleField").write("Test");
        clickOn("#amountField").write("10");
        clickOn("#payerChoiceBox").clickOn("Test Participant");
        clickOn("#datePicker").write("01/03/2024");
        clickOn("#tags").write("Test");

        Platform.runLater(() -> {
            addExpenseCtrl.keyPressed(new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.ENTER, false, false, false, false));
        });
        waitForFxEvents();
        Mockito.verify(mainCtrlMock).showEventOverview(Mockito.any(Event.class));

        participant.setName("Test Participant");

        Platform.runLater(() -> {
            addExpenseCtrl.initialize();
        });
        waitForFxEvents();

        clickOn("#titleField").write("Test");
        clickOn("#amountField").write("10");
        clickOn("#payerChoiceBox").clickOn("Test Participant");
        clickOn("#tags").write("Test");

        Platform.runLater(() -> {
            addExpenseCtrl.keyPressed(new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.ESCAPE, false, false, false, false));
        });
        waitForFxEvents();
        Mockito.verify(mainCtrlMock, Mockito.times(2)).showEventOverview(Mockito.any(Event.class));
    }

}
