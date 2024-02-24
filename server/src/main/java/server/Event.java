package server;

import java.util.Objects;

public class Event {

    private String title;
    private String inviteCode;
    private Person creator;
    private List<Person> participants;
    private List<Expense> expenses;
    private String dateTime;
    private int id;

    public Event(String title, String inviteCode, Person creator, List<Person> participants,
                 List<Expense> expenses, String dateTime, int id) {
        this.title = title;
        this.inviteCode = inviteCode;
        this.creator = creator;
        this.participants = participants;
        this.expenses = expenses;
        this.dateTime = dateTime;
        this.id = id;
    }

    public int getTotal() {
        int totalExpenses = 0;
        for(int i = 0; i < expenses.size(); i++) {
            totalExpenses = totalExpenses + expenses.get(i).getAmount();
        }
        return totalExpenses;
    }

    public String getTitle() {
        return title;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public Person getCreator() {
        return creator;
    }

    public List<Person> getParticipants() {
        return participants;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public String getDateTime() {
        return dateTime;
    }

    public int getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public void setCreator(Person creator) {
        this.creator = creator;
    }

    public void setParticipants(List<Person> participants) {
        this.participants = participants;
    }

    public void removeParticipant(Person participant) {
        for(int i = 0; i < participants.size(); i++) {
            if(participants.get(i).equals(participant)) {
                participants.remove(i);
            }
        }
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public void removeExpense(Expense expense) {
        for(int i = 0; i < expenses.size(); i++) {
            if(expenses.get(i).equals(expense)) {
                expenses.remove(i);
            }
        }
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastActivity() {
        int lastIndex = expenses.size() - 1;
        return expenses.get(lastIndex).getActivity();
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
