package com.fonctionality.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@ToString

public class CurrencyValue {
    private Long id;
    private Currency sourceCurrency;
    private Currency destinationCurrency;
    private Double amount;
    private LocalDateTime effectiveDate;
}
