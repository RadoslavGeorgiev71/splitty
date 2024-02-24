package server;

import java.util.Objects;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Event {

    private String title;
    private String inviteCode;
    private Person creator;
    private List<Person> participants;
    private List<Expense> expenses;
    private String dateTime;
    private int id;


    // 1.   Person and Expense Class appearing as an error is because they are placeholders,
    //      as I can't access the Person and Expense classes yet.
    // 2.   Not sure how to format the Invite Code yet, but I'm guessing that will be done in the Event class.
    // 3.   Same as above for the ID.
    // 4.   Will later add a correct automatic dateTime update method, which will run when an "edit" method is used.
    public Event(String title, String inviteCode, Person creator, List<Person> participants,
                 List<Expense> expenses, String dateTime, int id) {
        this.title = title;
        this.inviteCode = inviteCode;
        this.creator = creator;
        this.participants = participants;
        this.expenses = expenses;
        this.id = id;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        this.dateTime = dtf.format(now);
    }


    // Not sure if this should be put in the Expense class or in the Event class, as an Expense has to be created with
    // all the supporting fields. I'm guessing this will just call the method to create an Expense.
    public void addExpense() {

    }

    public void removeExpense(Expense expense) {
        for(int i = 0; i < expenses.size(); i++) {
            if(expenses.get(i).equals(expense)) {
                expenses.remove(i);
            }
        }
    }

    public String getLastActivity() {
        int lastIndex = expenses.size() - 1;
        return expenses.get(lastIndex).getActivity();
    }

    public int getTotal() {
        int totalExpenses = 0;
        for(int i = 0; i < expenses.size(); i++) {
            totalExpenses = totalExpenses + expenses.get(i).getAmount();
        }
        return totalExpenses;
    }

    // Not sure if this should be put in the Participant class or in the Event class. Same issue as with the addExpense
    // method.
    public void addParticipant() {

    }

    // Might be better to assign a number to every participant when they get added, so it's easier to remove
    // the correct participant.
    public void removeParticipant(Person participant) {
        for(int i = 0; i < participants.size(); i++) {
            if(participants.get(i).equals(participant)) {
                participants.remove(i);
            }
        }
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public Person getCreator() {
        return creator;
    }

    public void setCreator(Person creator) {
        this.creator = creator;
    }

    public List<Person> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Person> participants) {
        this.participants = participants;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event event)) return false;
        return getId() == event.getId() && Objects.equals(getTitle(), event.getTitle()) &&
                Objects.equals(getInviteCode(), event.getInviteCode()) &&
                Objects.equals(getCreator(), event.getCreator()) &&
                Objects.equals(getParticipants(), event.getParticipants()) &&
                Objects.equals(getExpenses(), event.getExpenses()) &&
                Objects.equals(getDateTime(), event.getDateTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getInviteCode(), getCreator(), getParticipants(),
                getExpenses(), getDateTime(), getId());
    }
}
