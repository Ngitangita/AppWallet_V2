package com.fonctionality.entity;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@ToString
public class TransactionCategory {
    private Long id;
    private String name;
    private List<Transaction> transactions;
}
