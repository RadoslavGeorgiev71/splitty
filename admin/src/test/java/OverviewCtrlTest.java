
import commons.Event;
import commons.Expense;
import commons.Participant;
import org.junit.jupiter.api.Test;
import scenes.MainCtrl;
import scenes.OverviewCtrl;
import utils.Admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
 public class OverviewCtrlTest {

     @Test
     void readFromFile() {
         List<Expense> expenses = new ArrayList<>();
         List<Participant> participants = new ArrayList<>();
         List<Event> events = List.of(new Event(1, "test", "Queze2TK",
                 participants, expenses, "2024/03/20 21:00:12"));
         OverviewCtrl controller = new OverviewCtrl(new Admin(), new MainCtrl());
         List<Event> result = controller.readEvents(new Scanner("{\"id\":1,\"title\":\"test\",\"inviteCode\":\"Queze2TK\"," +
                 "\"participants\":[]," + "\"expenses\":[],\"dateTime\":\"2024/03/20 21:00:12\"}"));
         assertTrue(events.get(0).equals(result.get(0)));
     }

     @Test
     void readFromFileNull() {
         List<Event> events = List.of(new Event());
         OverviewCtrl controller = new OverviewCtrl(new Admin(), new MainCtrl());
         List<Event> result = controller.readEvents(new Scanner(""));
         assertEquals(0, result.size());
     }
}
