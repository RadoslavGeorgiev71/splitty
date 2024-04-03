package server.Repositories;

import commons.Participant;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.ParticipantRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestParticipantRepository implements ParticipantRepository {

    private List<Participant> participants = new ArrayList<>();
<<<<<<< HEAD
    private final List<String> calledMethods = new ArrayList<>();

    /**
     * Adds the name of the called method to the list of called methods.
     *
     * @param name The name of the called method.
     */
    private void call(String name) {
        calledMethods.add(name);
    }

    /**
     * Returns the list of participants.
     *
     * @return The list of participants
=======
    private List<String> calledMethods = new ArrayList<>();

    /**
     *
     * @return -
>>>>>>> f22dd26a22f878be5a4815c14cc5931d61b4c59f
     */
    public List<Participant> getParticipants() {
        return participants;
    }

    /**
<<<<<<< HEAD
     * Sets the list of participants.
     *
     * @param participants The list of participants to set.
=======
     *
     * @param participants -
>>>>>>> f22dd26a22f878be5a4815c14cc5931d61b4c59f
     */
    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    /**
<<<<<<< HEAD
     * Returns the list of called methods.
     *
     * @return The list of called methods.
=======
     *
     * @return -
>>>>>>> f22dd26a22f878be5a4815c14cc5931d61b4c59f
     */
    public List<String> getCalledMethods() {
        return calledMethods;
    }

    /**
     *
<<<<<<< HEAD
=======
     * @param calledMethods -
     */
    public void setCalledMethods(List<String> calledMethods) {
        this.calledMethods = calledMethods;
    }

    /**
     *
>>>>>>> f22dd26a22f878be5a4815c14cc5931d61b4c59f
     */
    @Override
    public void flush() {

    }

    /**
<<<<<<< HEAD
     * @param entity
     * @param <S>
     * @return
=======
     *
     * @param entity -
     * @return -
     * @param <S> -
>>>>>>> f22dd26a22f878be5a4815c14cc5931d61b4c59f
     */
    @Override
    public <S extends Participant> S saveAndFlush(S entity) {
        return null;
    }

    /**
<<<<<<< HEAD
     * @param entities
     * @param <S>
     * @return
     */
    @Override
    public <S extends Participant> java.util.List<S> saveAllAndFlush(Iterable<S> entities) {
=======
     *
     * @param entities -
     * @return -
     * @param <S> -
     */
    @Override
    public <S extends Participant> List<S> saveAllAndFlush(Iterable<S> entities) {
>>>>>>> f22dd26a22f878be5a4815c14cc5931d61b4c59f
        return null;
    }

    /**
<<<<<<< HEAD
     * @param entities
=======
     *
     * @param entities -
>>>>>>> f22dd26a22f878be5a4815c14cc5931d61b4c59f
     */
    @Override
    public void deleteAllInBatch(Iterable<Participant> entities) {

    }

    /**
<<<<<<< HEAD
     * @param longs
=======
     *
     * @param longs -
>>>>>>> f22dd26a22f878be5a4815c14cc5931d61b4c59f
     */
    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    /**
     *
     */
    @Override
    public void deleteAllInBatch() {

    }

    /**
<<<<<<< HEAD
     * @param aLong
     * @deprecated
=======
     *
     * @param aLong -
     * @return -
>>>>>>> f22dd26a22f878be5a4815c14cc5931d61b4c59f
     */
    @Override
    public Participant getOne(Long aLong) {
        return null;
    }

    /**
<<<<<<< HEAD
     * @param aLong
     * @deprecated
=======
     *
     * @param aLong -
     * @return -
>>>>>>> f22dd26a22f878be5a4815c14cc5931d61b4c59f
     */
    @Override
    public Participant getById(Long aLong) {
        return null;
    }

    /**
<<<<<<< HEAD
     * @param aLong
     * @return
=======
     *
     * @param aLong -
     * @return -
>>>>>>> f22dd26a22f878be5a4815c14cc5931d61b4c59f
     */
    @Override
    public Participant getReferenceById(Long aLong) {
        return null;
    }

    /**
<<<<<<< HEAD
     * @param example
     * @param <S>
     * @return
=======
     *
     * @param example -
     * @return -
     * @param <S> -
>>>>>>> f22dd26a22f878be5a4815c14cc5931d61b4c59f
     */
    @Override
    public <S extends Participant> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    /**
<<<<<<< HEAD
     * @param example
     * @param <S>
     * @return
     */
    @Override
    public <S extends Participant> java.util.List<S> findAll(Example<S> example) {
=======
     *
     * @param example -
     * @return -
     * @param <S> -
     */
    @Override
    public <S extends Participant> List<S> findAll(Example<S> example) {
>>>>>>> f22dd26a22f878be5a4815c14cc5931d61b4c59f
        return null;
    }

    /**
<<<<<<< HEAD
     * @param example
     * @param sort
     * @param <S>
     * @return
     */
    @Override
    public <S extends Participant> java.util.List<S> findAll(Example<S> example, Sort sort) {
=======
     *
     * @param example -
     * @param sort -
     * @return -
     * @param <S> -
     */
    @Override
    public <S extends Participant> List<S> findAll(Example<S> example, Sort sort) {
>>>>>>> f22dd26a22f878be5a4815c14cc5931d61b4c59f
        return null;
    }

    /**
<<<<<<< HEAD
     * @param example
     * @param pageable
     * @param <S>
     * @return
=======
     *
     * @param example -
     * @param pageable -
     * @return -
     * @param <S> -
>>>>>>> f22dd26a22f878be5a4815c14cc5931d61b4c59f
     */
    @Override
    public <S extends Participant> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    /**
<<<<<<< HEAD
     * @param example
     * @param <S>
     * @return
=======
     *
     * @param example -
     * @return -
     * @param <S> -
>>>>>>> f22dd26a22f878be5a4815c14cc5931d61b4c59f
     */
    @Override
    public <S extends Participant> long count(Example<S> example) {
        return 0;
    }

    /**
<<<<<<< HEAD
     * @param example
     * @param <S>
     * @return
=======
     *
     * @param example -
     * @return -
     * @param <S> -
>>>>>>> f22dd26a22f878be5a4815c14cc5931d61b4c59f
     */
    @Override
    public <S extends Participant> boolean exists(Example<S> example) {
        return false;
    }

    /**
<<<<<<< HEAD
     * @param example
     * @param queryFunction
     * @param <S>
     * @param <R>
     * @return
     */
    @Override
    public <S extends Participant, R> R findBy(Example<S> example,
                      Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
=======
     *
     * @param example -
     * @param queryFunction -
     * @return -
     * @param <S> -
     * @param <R> -
     */
    @Override
    public <S extends Participant, R> R findBy(Example<S> example,
                                               Function<FluentQuery.FetchableFluentQuery<S>,
                                                   R> queryFunction) {
>>>>>>> f22dd26a22f878be5a4815c14cc5931d61b4c59f
        return null;
    }

    /**
<<<<<<< HEAD
     * @param entity
     * @param <S>
     * @return
     */
    @Override
    public <S extends Participant> S save(S entity) {
        return null;
    }

    /**
     * @param entities
     * @param <S>
     * @return
     */
    @Override
    public <S extends Participant> java.util.List<S> saveAll(Iterable<S> entities) {
=======
     *
     * @param entity -
     * @return -
     * @param <S> -
     */
    @Override
    public <S extends Participant> S save(S entity) {
        calledMethods.add("save");
        participants.add(entity);
        return entity;
    }

    /**
     *
     * @param entities -
     * @return -
     * @param <S> -
     */
    @Override
    public <S extends Participant> List<S> saveAll(Iterable<S> entities) {
>>>>>>> f22dd26a22f878be5a4815c14cc5931d61b4c59f
        return null;
    }

    /**
<<<<<<< HEAD
     * @param aLong
     * @return
     */
    @Override
    public Optional<Participant> findById(Long aLong) {
        return Optional.empty();
    }

    /**
     * @param aLong
     * @return
     */
    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    /**
     * @return
     */
    @Override
    public java.util.List<Participant> findAll() {
=======
     *
     * @param aLong -
     * @return -
     */
    @Override
    public Optional<Participant> findById(Long aLong) {
        calledMethods.add("findById");
        List<Participant> rightParticipants =  participants.stream()
            .filter(x -> x.getId() == aLong).toList();
        if (rightParticipants.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(rightParticipants.getFirst());
    }

    /**
     *
     * @param aLong -
     * @return -
     */
    @Override
    public boolean existsById(Long aLong) {
        calledMethods.add("existsById");
        List<Participant> matchingParticipants = participants.stream()
            .filter(x -> x.getId() == aLong).toList();
        if(matchingParticipants.size() == 1) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     *
     * @return -
     */
    @Override
    public List<Participant> findAll() {
        calledMethods.add("findAll");
        return participants;
    }

    /**
     *
     * @param longs -
     * @return -
     */
    @Override
    public List<Participant> findAllById(Iterable<Long> longs) {
>>>>>>> f22dd26a22f878be5a4815c14cc5931d61b4c59f
        return null;
    }

    /**
<<<<<<< HEAD
     * @param longs
     * @return
     */
    @Override
    public java.util.List<Participant> findAllById(Iterable<Long> longs) {
        return null;
    }

    /**
     * @return
=======
     *
     * @return -
>>>>>>> f22dd26a22f878be5a4815c14cc5931d61b4c59f
     */
    @Override
    public long count() {
        return 0;
    }

    /**
<<<<<<< HEAD
     * @param aLong
     */
    @Override
    public void deleteById(Long aLong) {

    }

    /**
     * @param entity
=======
     *
     * @param aLong -
     */
    @Override
    public void deleteById(Long aLong) {
        calledMethods.add("deleteById");
        participants = participants.stream().filter(x -> x.getId() != aLong).toList();
    }

    /**
     *
     * @param entity -
>>>>>>> f22dd26a22f878be5a4815c14cc5931d61b4c59f
     */
    @Override
    public void delete(Participant entity) {

    }

    /**
<<<<<<< HEAD
     * @param longs
=======
     *
     * @param longs -
>>>>>>> f22dd26a22f878be5a4815c14cc5931d61b4c59f
     */
    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    /**
<<<<<<< HEAD
     * @param entities
=======
     *
     * @param entities -
>>>>>>> f22dd26a22f878be5a4815c14cc5931d61b4c59f
     */
    @Override
    public void deleteAll(Iterable<? extends Participant> entities) {

    }

    /**
     *
     */
    @Override
    public void deleteAll() {

    }

    /**
<<<<<<< HEAD
     * @param sort
     * @return
     */
    @Override
    public java.util.List<Participant> findAll(Sort sort) {
=======
     *
     * @param sort -
     * @return -
     */
    @Override
    public List<Participant> findAll(Sort sort) {
>>>>>>> f22dd26a22f878be5a4815c14cc5931d61b4c59f
        return null;
    }

    /**
<<<<<<< HEAD
     * @param pageable
     * @return
=======
     *
     * @param pageable -
     * @return -
>>>>>>> f22dd26a22f878be5a4815c14cc5931d61b4c59f
     */
    @Override
    public Page<Participant> findAll(Pageable pageable) {
        return null;
    }
<<<<<<< HEAD


=======
>>>>>>> f22dd26a22f878be5a4815c14cc5931d61b4c59f
}
