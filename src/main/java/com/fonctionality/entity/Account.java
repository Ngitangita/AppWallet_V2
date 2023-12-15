package com.fonctionality.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@ToString
public class Account {
    private Long id;
    private AccountName name;
    private Double balance;
    private LocalDateTime lastUpdateDateTime;
    private List<Transaction> transactions = new ArrayList<>();
    private Currency currency;
    private TypeAccount account_type;
    private List<TransferHistory> debitHistory = new ArrayList<>();
    private List<TransferHistory> creditHistory = new ArrayList<>();
    public void addTransaction(Transaction ts) {
        if (!this.transactions.contains(ts)) {
            this.transactions.add(ts);
            ts.setAccount(this);
        }
    }

    public void removeTransaction(Transaction ts) {
        if (this.transactions.contains(ts)){
            this.transactions.remove(ts);
            ts.setAccount(null);
        }
    }

    public void credit (Account account, double amount) {
        this.balance -= amount;
        account.setBalance(account.getBalance() + amount);
    }

    public void debit(Account account, double amount) {
        this.balance += amount;
        account.setBalance(account.getBalance() - amount);
    }


}
