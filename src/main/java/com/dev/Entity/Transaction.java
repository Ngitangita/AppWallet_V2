package com.dev.Entity;

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
    private LocalDateTime dateTime;
    private TypeTransaction transactionType;
    private Account account;
}
