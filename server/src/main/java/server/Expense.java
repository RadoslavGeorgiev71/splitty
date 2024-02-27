package server;

import java.util.List;
import java.util.Objects;

public class Expense {
    private String title;
    private Participant payingParticipant;
    private double amount;
    private List<Participant> participants;
    private String dateTime;

    /**
     * Default constructor, no set Date
     * @param title of Expense
     * @param payingParticipant of Expense
     * @param amount of Expense
     * @param participants of Expense
     */
    public Expense(String title, Participant payingParticipant, double amount, List<Participant> participants) {
        this.title = title;
        this.payingParticipant = payingParticipant;
        this.amount = amount;
        this.participants = participants;
    }

    /**
     * Default constructor with Date
     * @param title of Expense
     * @param payingParticipant of Expense
     * @param amount of Expense
     * @param participants of Expense
     * @param dateTime of Expense
     */
    public Expense(String title, Participant payingParticipant, double amount, List<Participant> participants, String dateTime) {
        this.title = title;
        this.payingParticipant = payingParticipant;
        this.amount = amount;
        this.participants = participants;
        this.dateTime = dateTime;
    }

    /**
     * @return Expense title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Update Expense title
     * @param title new title of Expense
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return paying Participant
     */
    public Participant getPayingParticipant() {
        return payingParticipant;
    }

    /**
     * Update paying Participant
     * @param payingParticipant new payer of Expense
     */
    public void setPayingParticipant(Participant payingParticipant) {
        this.payingParticipant = payingParticipant;
    }

    /**
     * @return amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Update amount
     * @param amount new amount of Expense
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * @return participants
     */
    public List<Participant> getParticipants() {
        return participants;
    }

    /**
     * Update participants
     * @param participants new list of participants of Expense
     */
    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    /**
     * @return date
     */
    public String getDateTime() {
        return dateTime;
    }

    /**
     * Update date
     * @param dateTime new date of Expense
     */
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * @return activity
     */
    public String getActivity() {
        return this.title + " payed by " + this.payingParticipant.getName();
    }

    /**
     * Equals method
     * @param o object we check equality with
     * @return True if they are the same, otherwise False
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expense expense = (Expense) o;
        return Double.compare(expense.amount, amount) == 0 && title.equals(expense.title)
                && payingParticipant.equals(expense.payingParticipant) && participants.equals(expense.participants)
                && Objects.equals(dateTime, expense.dateTime);
    }

    /**
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(title, payingParticipant, amount, participants, dateTime);
    }
}
