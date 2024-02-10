package com.wallet.appwalletapi.entitries;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class Transaction {
    private Long id;
    private String label;
    private Double amount;
    public LocalDateTime dateTime;
    private TypeTransaction typeTransaction;
    private Account account;
    private Category category;
}
