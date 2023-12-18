package com.fonctionality.entity;

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
    private Account account;
    private Category category;
}
