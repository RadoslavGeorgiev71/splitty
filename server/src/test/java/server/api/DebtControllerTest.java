package server.api;

import org.junit.jupiter.api.BeforeEach;
import server.database.DebtRepository;

public class DebtControllerTest {
    private TestDebtRepository repo;
    private DebtController sut;

    @BeforeEach
    public void setup() {
        repo = new TestDebtRepository();
        sut = new DebtController(repo);
    }
}
