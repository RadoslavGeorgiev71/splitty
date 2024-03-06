package commons;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;

    /**
     * A constructor for creating Participant object by a name
     *
     * @param name - the name of the Participant
     */
    public Participant(String name) {
        this.name = name;
    }

    /**
     * A constructor for creating Participant object by id and name
     *
     * @param id   - the id of the Participant
     * @param name - the id of the Participant
     */
    public Participant(long id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     *  Empty contructor
     */
    public Participant() {
        //empty
    }

    /**
     * Getter for the id of Participant
     *
     * @return the id of Participant
     */
    public long getId() {
        return id;
    }

    /**
     * Getter for name of the Participant
     *
     * @return the name of the Participant
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the id of the Participant
     *
     * @param id - the id to be set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Setter for the name of the Participant
     *
     * @param name - the name to be set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Equals method for Participant
     *
     * @param o - the object we compare with
     * @return true if the objects are equal,
     * false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Participant that = (Participant) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    /**
     * HashCode method for participant
     *
     * @return the hashCode of the Participant
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
