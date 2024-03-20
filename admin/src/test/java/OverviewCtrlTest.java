
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
         List<Event> events = List.of(new Event(1, "test", "Queze2TK",
                 List.of(new Participant(4,"George","g@gmail.com","ABCD","1234567"),
                         new Participant(5,"Maria","m@gmail.com","EFGH","7654321"))
                 , expenses , "2024/03/20 21:00:12"));
         OverviewCtrl controller = new OverviewCtrl(new Admin(), new MainCtrl());
         List<Event> result = controller.readEvents(new Scanner("{\"id\":1,\"title\":\"test\",\"inviteCode\":\"Queze2TK\"," +
                 "\"participants\":[{\"id\":4,\"name\":\"George\",\"email\":\"g@gmail.com\",\"bic\":\"ABCD\",\"iban\":\"1234567\"}," +
                 "{\"id\":5,\"name\":\"Maria\",\"email\":\"m@gmail.com\",\"bic\":\"EFGH\",\"iban\":\"7654321\"}]," +
                 "\"expenses\":[],\"dateTime\":\"2024/03/20 21:00:12\"}"));
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
