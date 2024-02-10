package com.wallet.appwalletapi.entitries;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class TransferHistory {
    private Long id;
    private Account debitTransaction;
    private Account creditTransaction;
    private LocalDateTime transferDate;
}
