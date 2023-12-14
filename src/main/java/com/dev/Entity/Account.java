package com.dev.Entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
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
}
