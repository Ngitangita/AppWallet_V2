package services;

import entitries.Account;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@ToString
public class TransferHistoryWithAmountTransfer {
    private Account debitTransaction;
    private Account creditTransaction;
    private LocalDateTime transferDate;
    private double amountTransfer;
}
