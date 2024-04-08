package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import commons.Expense;
import commons.Participant;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EditExpenseCtrlTest extends ApplicationTest {

    ServerUtils serverMock;
    MainCtrl mainCtrlMock;
    Event mockEvent;
    Participant mockParticipant;
    Participant mockParticipant2;

    Expense mockExpense;
    private EditExpenseCtrl editExpenseCtrl;

    @BeforeAll
    public static void setupSpec() throws Exception {
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
        System.setProperty("java.awt.headless", "true");
    }

    @Override
    public void start(Stage stage) throws Exception {
        serverMock = Mockito.mock(ServerUtils.class);
        mainCtrlMock = Mockito.mock(MainCtrl.class);

        mockParticipant = new Participant();
        mockParticipant.setName("Test");
        mockParticipant.setEmail("yes");
        mockParticipant.setIban("Test");
        mockParticipant.setBic("Test");

        mockParticipant2 = new Participant();
        mockParticipant2.setName("Test2");
        mockParticipant2.setEmail("yes2");
        mockParticipant2.setIban("Test2");
        mockParticipant2.setBic("Test2");

        mockExpense = new Expense();
        mockExpense.setTitle("Test");
        mockExpense.setPayingParticipant(mockParticipant);
        mockExpense.setParticipants(new ArrayList<>());
        mockExpense.addParticipant(mockParticipant);
        mockExpense.addParticipant(mockParticipant2);
        mockExpense.setAmount(10);
        mockExpense.setCurrency("EUR");
        mockExpense.setDateTime("2020-04-01");


        mockEvent = new Event();
        mockEvent.setTitle("Test");
        mockEvent.setParticipants(new ArrayList<>());
        mockEvent.addParticipant(mockParticipant);
        mockEvent.addParticipant(mockParticipant2);
        mockEvent.setExpenses(new ArrayList<>());
        mockEvent.addExpense(mockExpense);
        mockEvent.setInviteCode("testInviteCode");

        editExpenseCtrl = new EditExpenseCtrl(serverMock, mainCtrlMock);
        editExpenseCtrl.setEvent(mockEvent);
        editExpenseCtrl.setExpense(mockExpense);

        Mockito.doNothing().when(mainCtrlMock).showEventOverview(Mockito.any(Event.class));
        Mockito.when(serverMock.persistEvent(Mockito.any(Event.class))).thenReturn(mockEvent);
        Mockito.when(serverMock.getEvent(Mockito.anyLong())).thenReturn(mockEvent);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/scenes/EditExpense.fxml"));
        loader.setControllerFactory(type -> {
            if (type == EditExpenseCtrl.class) {
                return editExpenseCtrl;
            } else {
                return null;
            }
        });

        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();

    }

    @Test
    public void testOnAbortClick() {
        clickOn("#expenseAbortButton");
        Mockito.verify(mainCtrlMock).showEventOverview(Mockito.any(Event.class));
    }

    @Test
    public void testOnSaveClick() {
        interact(() -> {
            TextField titleField = lookup("#titleField").queryAs(TextField.class);
            titleField.setText("Test");

            TextField amountField = lookup("#amountField").queryAs(TextField.class);
            amountField.setText("10");

            ChoiceBox payerChoiceBox = lookup("#currChoiceBox").queryAs(ChoiceBox.class);
            payerChoiceBox.getSelectionModel().select(0);

            DatePicker datePicker = lookup("#datePicker").queryAs(DatePicker.class);
            datePicker.setValue(LocalDate.of(2020, 4, 1));

            RadioButton equally = lookup("#equally").queryAs(RadioButton.class);
            equally.setSelected(true);

            TextField tags = lookup("#tags").queryAs(TextField.class);
            tags.setText("Test");

            Button expenseSaveButton = lookup("#expenseSaveButton").queryAs(Button.class);
            expenseSaveButton.fire();

            Mockito.verify(serverMock).persistEvent(Mockito.any(Event.class));
            Mockito.verify(mainCtrlMock).showEventOverview(Mockito.any(Event.class));
        });
    }

    @Test
    public void testOnSaveClickOnlySome() {
        interact(() -> {
            TextField titleField = lookup("#titleField").queryAs(TextField.class);
            titleField.setText("Test");

            TextField amountField = lookup("#amountField").queryAs(TextField.class);
            amountField.setText("10");

            ChoiceBox payerChoiceBox = lookup("#currChoiceBox").queryAs(ChoiceBox.class);
            payerChoiceBox.getSelectionModel().select(0);

            DatePicker datePicker = lookup("#datePicker").queryAs(DatePicker.class);
            datePicker.setValue(LocalDate.of(2020, 4, 1));

            RadioButton onlySome = lookup("#onlySome").queryAs(RadioButton.class);
            onlySome.setSelected(true);
            onlySome.getOnAction().handle(new ActionEvent());

            TextField tags = lookup("#tags").queryAs(TextField.class);
            tags.setText("Test");

            Button expenseSaveButton = lookup("#expenseSaveButton").queryAs(Button.class);
            expenseSaveButton.fire();

            Mockito.verify(serverMock).persistEvent(Mockito.any(Event.class));
            Mockito.verify(mainCtrlMock).showEventOverview(Mockito.any(Event.class));
        });
    }

    @Test
    public void testOnDeleteClick() {
        Platform.runLater(() -> {
            editExpenseCtrl.onDeleteClick(new ActionEvent());
        });
        WaitForAsyncUtils.waitForFxEvents();
        interact(() -> {
            DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);
            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            okButton.fire();
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    public void testKeyPressed() {
        KeyEvent keyEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.W, false, true, false, false);
        interact(() -> editExpenseCtrl.keyPressed(keyEvent));
        Mockito.verify(mainCtrlMock).closeWindow();
        WaitForAsyncUtils.waitForFxEvents();

        KeyEvent keyEvent2 = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.ESCAPE, false, false, false, false);
        interact(() -> editExpenseCtrl.keyPressed(keyEvent2));
        Mockito.verify(mainCtrlMock).showEventOverview(Mockito.any(Event.class));
        WaitForAsyncUtils.waitForFxEvents();

        KeyEvent keyEvent3 = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.ESCAPE, false, false, false, false);
        interact(() -> editExpenseCtrl.keyPressed(keyEvent3));
        Mockito.verify(mainCtrlMock, Mockito.times(2)).showEventOverview(Mockito.any(Event.class));
        WaitForAsyncUtils.waitForFxEvents();
        Mockito.reset(mainCtrlMock);



        TextField titleField = lookup("#titleField").queryAs(TextField.class);
        clickOn(titleField);

        KeyEvent keyEvent4 = new KeyEvent(titleField, titleField, KeyEvent.KEY_PRESSED, "", "", KeyCode.TAB, false, false, false, false);
        WaitForAsyncUtils.asyncFx(() -> editExpenseCtrl.keyPressed(keyEvent4));
        WaitForAsyncUtils.waitForFxEvents();
        Mockito.reset(mainCtrlMock);


    }

    @Test
    public void testAddRemoveParticipant() {
        Participant participant = new Participant();
        participant.setName("Test Participant");
        List<Participant> list = new ArrayList<>();
        list.add(participant);

        editExpenseCtrl.setParticipants(list);

        interact(() -> editExpenseCtrl.addRemoveParticipant(participant));

        assertFalse(editExpenseCtrl.getParticipants().contains(participant));

        interact(() -> editExpenseCtrl.addRemoveParticipant(participant));

        assertTrue(editExpenseCtrl.getParticipants().contains(participant));

        editExpenseCtrl.setParticipants(list);
        editExpenseCtrl.setCurrency("EUR");
        editExpenseCtrl.setExpense(new Expense());
    }
}