package com.fonctionality.dto.request;

import com.fonctionality.entity.Account;
import com.fonctionality.entity.TypeTransaction;

import java.time.LocalDateTime;

public record TransactionRequest(
         String label,
         Double amount,
         LocalDateTime dateTime,
         TypeTransaction transactionType,
         Account account
) {
}
