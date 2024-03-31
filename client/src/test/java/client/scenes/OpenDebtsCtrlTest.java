package client.scenes;

import client.utils.ServerUtils;
import commons.Debt;
import commons.Event;
import commons.Participant;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import jakarta.ws.rs.WebApplicationException;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class OpenDebtsCtrlTest extends ApplicationTest {

    ServerUtils serverMock;
    MainCtrl mainCtrlMock;

    Event mockEvent;

    List<Debt> debts;

    private OpenDebtsCtrl openDebtsCtrl;

    @Override
    public void start(Stage stage) throws Exception {
        serverMock = Mockito.mock(ServerUtils.class);
        mainCtrlMock = Mockito.mock(MainCtrl.class);
        mockEvent = Mockito.mock(Event.class);
        debts = Mockito.mock(List.class);

        openDebtsCtrl = new OpenDebtsCtrl(serverMock, mainCtrlMock);

        openDebtsCtrl.setEvent(mockEvent);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/scenes/OpenDebts.fxml"));
        loader.setControllerFactory(type -> {
            if (type == OpenDebtsCtrl.class) {
                return openDebtsCtrl;
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
    public void testGetPaymentInstructions(){
        openDebtsCtrl.getPaymentInstructions();
        Mockito.verify(serverMock, Mockito.times(2)).getPaymentInstructions(mockEvent);
    }

    @Test
    public void testGetPaymentInstructionsException() {
        Mockito.when(serverMock.getPaymentInstructions(mockEvent)).thenThrow(new WebApplicationException());

        Platform.runLater(() -> {
            openDebtsCtrl.getPaymentInstructions();
        });

        interact(() -> {
            DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);
            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            okButton.fire();
        });

        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    public void testBackButton() {
        clickOn("#backButton");
        Mockito.verify(mainCtrlMock, Mockito.times(1)).showEventOverview(mockEvent);
    }

    @Test
    public void testInitialize() {
        Debt mockDebt = Mockito.mock(Debt.class);
        Participant mockParticipant = Mockito.mock(Participant.class);
        Mockito.when(mockDebt.getPersonOwed()).thenReturn(mockParticipant);
        Mockito.when(mockDebt.getPersonPaying()).thenReturn(mockParticipant);
        Mockito.when(mockDebt.getAmount()).thenReturn(10.0);

        debts = Mockito.mock(List.class);
        Mockito.when(debts.get(0)).thenReturn(mockDebt);
        Mockito.when(debts.size()).thenReturn(1);

        Mockito.when(serverMock.getPaymentInstructions(mockEvent)).thenReturn(debts);

        Platform.runLater(() -> openDebtsCtrl.initialize());
        waitForFxEvents();

        try {
            Field gridPaneField = OpenDebtsCtrl.class.getDeclaredField("gridPane");
            gridPaneField.setAccessible(true);
            GridPane gridPane = (GridPane) gridPaneField.get(openDebtsCtrl);

            assertEquals(4, gridPane.getChildren().size());

            assertTrue(gridPane.getChildren().get(0) instanceof TitledPane);

            assertTrue(gridPane.getChildren().get(1) instanceof FontAwesomeIconView);

            assertTrue(gridPane.getChildren().get(2) instanceof FontAwesomeIconView);

            assertTrue(gridPane.getChildren().get(3) instanceof Button);

            Button buttonReceived = (Button) gridPane.getChildren().get(3);
            Platform.runLater(() -> buttonReceived.fire());
            waitForFxEvents();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }


    }
}
