package server;

import java.time.LocalDateTime;

public class Debt {
    private String id;
    private Participant personOwed;
    private Participant personPaying;
    private double amount;
    private boolean paid;
    private LocalDateTime paidDateTime;
}
