package commons;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity(name = "Debt")
@Table(name = "Debt")
public class Debt {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Participant personPaying;
    @ManyToOne(cascade = CascadeType.ALL)
    private Participant personOwed;
    private double amount;
    private boolean paid;
    private LocalDateTime paidDateTime;

    /**
     * Empty constructor for the class
     */
    public Debt() {}

    /**
     * Constructor for the Debt class
     * @param personPaying The participant who has to pay the debt
     * @param personOwed The participant who will receive the debt
     * @param amount The amount of debt
     */
    public Debt(Participant personPaying, Participant personOwed, double amount){
        this.personPaying = personPaying;
        this.personOwed = personOwed;
        this.amount = amount;
        this.paid = false;
        this.paidDateTime = null;
    }

    /**
     * Constructor for the Debt class
     * @param id Unique identifier for the Debt instance
     * @param personPaying The participant who has to pay the debt
     * @param personOwed The participant who will receive the debt
     * @param amount The amount of debt
     */
    public Debt(long id, Participant personPaying, Participant personOwed, double amount){
        this.personPaying = personPaying;
        this.personOwed = personOwed;
        this.amount = amount;
        this.id = id;
        this.paid = false;
        this.paidDateTime = null;
    }


    /**
     * Getter for id
     * @return id
     */
    public long getId() {
        return id;
    }

    /**
     * Getter for personPaying
     * @return The person who must pay
     */
    public Participant getPersonPaying() {
        return personPaying;
    }

    /**
     * Getter for personOwed
     * @return The person who is owed the money
     */
    public Participant getPersonOwed() {
        return personOwed;
    }

    /**
     * Getter for amount
     * @return The amount of money of debt
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Getter for isPaid
     * @return Whether the debt has been paid
     */
    public boolean isPaid() {
        return paid;
    }

    /**
     * Getter for paidDateTime
     * @return returns the time at which the debt has been paid
     * If the paidDateTime is null, aka it has not been set yet,
     * The getter will instead return LocalDateTime.MIN to avoid
     * NullPointerException
     */
    public LocalDateTime getPaidDateTime() {
        if (paidDateTime == null) {
            return LocalDateTime.MIN;
        }
        return paidDateTime;
    }

    /**
     * Setter for id
     * @param id New id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Setter for personPaying
     * @param personPaying The new Participant who is in debt
     */
    public void setPersonPaying(Participant personPaying) {
        this.personPaying = personPaying;
    }

    /**
     * Setter for personOwed
     * @param personOwed The new Participant who is owed money
     */
    public void setPersonOwed(Participant personOwed) {
        this.personOwed = personOwed;
    }

    /**
     * Setter for amount
     * @param amount The new amount to be set
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Setter for paid
     * @param paid Set whether the debt has been paid
     */
    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    /**
     * Setter for paidDateTime
     * @param paidDateTime Set the time at which the debt was paid
     */
    public void setPaidDateTime(LocalDateTime paidDateTime) {
        this.paidDateTime = paidDateTime;
    }

    /**
     * Equals method for Debt
     * @param o
     * @return The boolean equality of this with Object o
     */
    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        Debt debt = (Debt) o;
        return id == debt.id && Double.compare(amount, debt.amount) == 0
                && paid == debt.paid && Objects.equals(personPaying, debt.personPaying)
                && Objects.equals(personOwed, debt.personOwed)
                && Objects.equals(paidDateTime, debt.paidDateTime);
    }

    /**
     * HashCode method for Debt
     * @return The hashCode of this
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, personPaying, personOwed, amount, paid, paidDateTime);
    }
}