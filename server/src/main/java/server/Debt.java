package server;

import java.time.LocalDateTime;
import java.util.Objects;

public class Debt {
    private int id;
    private Participant personPaying;
    private Participant personOwed;
    private double amount;
    private boolean paid;
    private LocalDateTime paidDateTime;

    public Debt(int id, Participant personPaying, Participant personOwed, double amount){
        this.personPaying = personPaying;
        this.personOwed = personOwed;
        this.amount = amount;
        this.id = id;
        this.paid = false;
        this.paidDateTime = null;
    }

    public int getId() {
        return id;
    }

    public Participant getPersonPaying() {
        return personPaying;
    }

    public Participant getPersonOwed() {
        return personOwed;
    }

    public double getAmount() {
        return amount;
    }

    public boolean isPaid() {
        return paid;
    }

    public LocalDateTime getPaidDateTime() {
        return paidDateTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPersonPaying(Participant personPaying) {
        this.personPaying = personPaying;
    }

    public void setPersonOwed(Participant personOwed) {
        this.personOwed = personOwed;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public void setPaidDateTime(LocalDateTime paidDateTime) {
        this.paidDateTime = paidDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Debt debt = (Debt) o;
        return id == debt.id && Double.compare(amount, debt.amount) == 0 && paid == debt.paid && Objects.equals(personPaying, debt.personPaying) && Objects.equals(personOwed, debt.personOwed) && Objects.equals(paidDateTime, debt.paidDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, personPaying, personOwed, amount, paid, paidDateTime);
    }
}