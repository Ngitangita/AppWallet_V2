package com.fonctionality.dto.response;

import com.fonctionality.entity.Account;
import com.fonctionality.entity.Transaction;
import com.fonctionality.entity.TypeTransaction;

import java.time.LocalDateTime;

public record TransactionResponse(
        Long id,
        String label,
        Double amount,
        LocalDateTime dateTime,
        AccountResponse account
) {

    public static TransactionResponse toResponse(Transaction transaction) {
        AccountResponse accountResponse = AccountResponse.toResponse(transaction.getAccount());
        return new TransactionResponse(
                transaction.getId(),
                transaction.getLabel(),
                transaction.getAmount(),
                transaction.getDateTime(),
                accountResponse
        );
    }
}
