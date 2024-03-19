package commons;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Objects;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
//import java.util.ArrayList;
import java.util.List;
import java.util.Random;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String title;
    private String inviteCode;

    @OneToMany(cascade=CascadeType.ALL)
    private List<Participant> participants;
    @OneToMany(cascade=CascadeType.ALL)
    private List<Expense> expenses;
    private String dateTime;


    // 1.   Participant and Expense Class appearing as an error is because they are placeholders,
    //      as I can't access the Person and Expense classes yet.
    // 2.   Not sure how to format the Invite Code yet,
    // but I'm guessing that will be done in the Event class.
    // 3.   Same as above for the ID.
    // 4.   Will later add a correct automatic dateTime update method,
    //      which will run when an "edit" method is used.

    /**
     * Default constructor for the class
     */
    public Event() {
        participants = new ArrayList<>();
        expenses = new ArrayList<>();
        updateDateTime();
    }

    /**
     * Constructor for the Event class
     * @param title Title used to differentiate the different Events
     * @param inviteCode Unique code used to join an Event
     * @param participants Participants who participate in the Event
     * @param expenses Expenses made during the Event
     */
    public Event(String title, String inviteCode,
                 List<Participant> participants, List<Expense> expenses) {
        this.title = title;
        this.inviteCode = inviteCode;
        this.participants = participants;
        this.expenses = expenses;
        // Placeholder ID
        this.id = 0;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        this.dateTime = dtf.format(now);
    }

    /**
     * Method to add an Empty Expense to the Event
     */
    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    /**
     * Method to remove an Expense from the Event
     * @param expense - expense to be removed
     */
    public void removeExpense(Expense expense) {
        for (int i = 0; i < expenses.size(); i++) {
            if (expenses.get(i).equals(expense)) {
                expenses.remove(i);
            }
        }
    }

    /**
     * Method to find the Last Activity added to the Event
     * @return the last Activity added to the Event
     */
    @JsonIgnore
    public Expense getLastActivity() {
        if (expenses.isEmpty()){
            return null;
        }
        else{
            int lastIndex = expenses.size() - 1;
            return expenses.get(lastIndex);
        }
    }

    /**
     * Method to calculate the total expenses of the Event
     * @return total sum of Expenses
     */
    @JsonIgnore
    public double getTotal() {
        double totalExpenses = 0;
        for (int i = 0; i < expenses.size(); i++) {
            totalExpenses = totalExpenses + expenses.get(i).getAmount();
        }
        return totalExpenses;
    }

    /**
     * Method to add a Participant to the Event
     */
    public void addParticipant(Participant participant) {
        participants.add(participant);
    }

    // Might be better to assign a number to every participant
    // when they get added, so it's easier to remove
    // the correct participant.

    /**
     * Method to remove a Participant from the Event
     * @param participant - participant to be removed
     */
    public void removeParticipant(Participant participant) {
        for (int i = 0; i < participants.size(); i++) {
            if (participants.get(i).equals(participant)) {
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
     * Method that creates a random 8-character invite code from the characters in 'characters'
     * it then sets the inviteCode for the event to be the randomly generated invite code
     * @return the randomly generated invite code
     */
    public String createInviteCode(){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++){
            int randomIndex = random.nextInt(characters.length());
            stringBuilder.append(characters.charAt(randomIndex));
        }
        this.setInviteCode(stringBuilder.toString());
        return stringBuilder.toString();
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
     * @param title - title to be set
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
     * @param inviteCode - Invite Code to be set
     */
    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

//    /**
//     * Getter for the Creator
//     * @return creator
//     */
//    public Participant getCreator() {
//        return creator;
//    }
//
//    /**
//     * Setter for the Creator
//     * @param creator - creator to be set
//     */
//    public void setCreator(Participant creator) {
//        this.creator = creator;
//    }

    /**
     * Getter for the Participants
     * @return participants
     */
    public List<Participant> getParticipants() {
        return participants;
    }

    /**
     * Setter for the Participants
     * @param participants - participants to be set
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
     * @param expenses - expenses to be set
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
     * @param dateTime - dateTime to be set
     */
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Getter for the ID
     * @return id
     */
    public long getId() {
        return id;
    }

    /**
     * Equals method for Event
     * @param o - object to check equality with
     * @return The boolean equality of this with Object o
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Event event)) {
            return false;
        }
        return getId() == event.getId() && Objects.equals(getTitle(), event.getTitle()) &&
                Objects.equals(getInviteCode(), event.getInviteCode()) &&
                /*Objects.equals(getCreator(), event.getCreator()) &&*/
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
        return Objects.hash(getTitle(), getInviteCode(), /*getCreator(),*/ getParticipants(),
                getExpenses(), getDateTime(), getId());
    }
}
