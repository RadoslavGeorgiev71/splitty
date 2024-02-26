package server;

import java.util.Objects;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Event {

    private String title;
    private String inviteCode;
    private Participant creator;
    private List<Participant> participants;
    private List<Expense> expenses;
    private String dateTime;
    private int id;


    // 1.   Participant and Expense Class appearing as an error is because they are placeholders,
    //      as I can't access the Person and Expense classes yet.
    // 2.   Not sure how to format the Invite Code yet, but I'm guessing that will be done in the Event class.
    // 3.   Same as above for the ID.
    // 4.   Will later add a correct automatic dateTime update method, which will run when an "edit" method is used.
    /**
     * Constructor for the Event class
     * @param title Title used to differentiate the different Events
     * @param inviteCode Unique code used to join an Event
     * @param creator Participant who created the Event
     * @param participants Participants who participate in the Event
     * @param expenses Expenses made during the Event
     */
    public Event(String title, String inviteCode, Participant creator, List<Participant> participants,
                 List<Expense> expenses) {
        this.title = title;
        this.inviteCode = inviteCode;
        this.creator = creator;
        this.participants = participants;
        this.expenses = expenses;
        this.id = 0;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        this.dateTime = dtf.format(now);
    }

    // Not sure if this should be put in the Expense class or in the Event class, as an Expense has to be created with
    // all the supporting fields. I'm guessing this will just call the method to create an Expense.

    /**
     * Method to add an Expense to the Event
     */
    public void addExpense() {

    }

    /**
     * Method to remove an Expense from the Event
     * @param expense
     */
    public void removeExpense(Expense expense) {
        for(int i = 0; i < expenses.size(); i++) {
            if(expenses.get(i).equals(expense)) {
                expenses.remove(i);
            }
        }
    }

    /**
     * Method to find the Last Activity added to the Event
     * @return the last Activity added to the Event
     */
    public String getLastActivity() {
        int lastIndex = expenses.size() - 1;
        return expenses.get(lastIndex).getActivity();
    }

    /**
     * Method to calculate the total expenses of the Event
     * @return total sum of Expenses
     */
    public int getTotal() {
        int totalExpenses = 0;
        for(int i = 0; i < expenses.size(); i++) {
            totalExpenses = totalExpenses + expenses.get(i).getAmount();
        }
        return totalExpenses;
    }

    // Not sure if this should be put in the Participant class or in the Event class. Same issue as with the addExpense
    // method.

    /**
     * Method to add a Participant to the Event
     */
    public void addParticipant() {

    }

    // Might be better to assign a number to every participant when they get added, so it's easier to remove
    // the correct participant.

    /**
     * Method to remove a Participant from the Event
     * @param participant
     */
    public void removeParticipant(Participant participant) {
        for(int i = 0; i < participants.size(); i++) {
            if(participants.get(i).equals(participant)) {
                participants.remove(i);
            }
        }
    }

    /**
     * Method to manually update the time when the Event was last updated
     */
    public void updateDateTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        dateTime = dtf.format(now);
    }

    /**
     * Getter for the Title
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for the Title
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for the Invite Code
     * @return inviteCode
     */
    public String getInviteCode() {
        return inviteCode;
    }

    /**
     * Setter for the Invite Code
     * @param inviteCode
     */
    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    /**
     * Getter for the Creator
     * @return creator
     */
    public Participant getCreator() {
        return creator;
    }

    /**
     * Setter for the Creator
     * @param creator
     */
    public void setCreator(Participant creator) {
        this.creator = creator;
    }

    /**
     * Getter for the Participants
     * @return participants
     */
    public List<Participant> getParticipants() {
        return participants;
    }

    /**
     * Setter for the Participants
     * @param participants
     */
    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    /**
     * Getter for the Expenses
     * @return expenses
     */
    public List<Expense> getExpenses() {
        return expenses;
    }

    /**
     * Setter for the Expenses
     * @param expenses
     */
    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    /**
     * Getter for the dateTime
     * @return dateTime
     */
    public String getDateTime() {
        return dateTime;
    }

    /**
     * Setter for the dateTime
     * @param dateTime
     */
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Getter for the ID
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for the ID
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Equals method for Event
     * @param o
     * @return The boolean equality of this with Object o
     */
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

    /**
     * HashCode method for Event
     * @return the hashCode of this
     */
    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getInviteCode(), getCreator(), getParticipants(),
                getExpenses(), getDateTime(), getId());
    }
}
