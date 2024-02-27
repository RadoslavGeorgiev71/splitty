package server;

import java.util.List;
import java.util.Objects;

public class Expense {
    private String title;
    private Participant payingParticipant;
    private double amount;
    private List<Participant> participants;
    private String dateTime;

    public Expense(String title, Participant payingParticipant, double amount, List<Participant> participants) {
        this.title = title;
        this.payingParticipant = payingParticipant;
        this.amount = amount;
        this.participants = participants;
    }

    public Expense(String title, Participant payingParticipant, double amount, List<Participant> participants, String dateTime) {
        this.title = title;
        this.payingParticipant = payingParticipant;
        this.amount = amount;
        this.participants = participants;
        this.dateTime = dateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Participant getPayingParticipant() {
        return payingParticipant;
    }

    public void setPayingParticipant(Participant payingParticipant) {
        this.payingParticipant = payingParticipant;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getActivity() {
        return this.title + " payed by " + this.payingParticipant.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expense expense = (Expense) o;
        return Double.compare(expense.amount, amount) == 0 && title.equals(expense.title)
                && payingParticipant.equals(expense.payingParticipant) && participants.equals(expense.participants)
                && Objects.equals(dateTime, expense.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, payingParticipant, amount, participants, dateTime);
    }
}
