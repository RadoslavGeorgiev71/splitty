

package server.database;

import commons.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<String> getCalledMethods();
}
