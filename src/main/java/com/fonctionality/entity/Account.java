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
}
