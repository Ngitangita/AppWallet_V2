package com.dev.Entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class Transaction {
    private Long id;
    private String label;
    private Double amount;
    private LocalDateTime dateTime;
    private TypeTransaction transactionType;
    private Account account;

}
