package com.dev.Entity;

import java.time.LocalDateTime;
import java.util.List;

public class Account {
    private Long id;
    private AccountName name;
    private Double balance;
    private LocalDateTime lastUpdateDateTime;
    private List<Transaction> transactions;
    private Currency currency;
    private TypeAccount account_type;

    public Account(Long id, AccountName name, Double balance, LocalDateTime lastUpdateDateTime, List<Transaction> transactions, Currency currency, TypeAccount account_type){
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.lastUpdateDateTime = lastUpdateDateTime;
        this.transactions = transactions;
        this.currency = currency;
        this.account_type = account_type;
    }
}
