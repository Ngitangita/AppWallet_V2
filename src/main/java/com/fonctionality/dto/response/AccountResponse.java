package com.fonctionality.dto.response;

import com.fonctionality.entity.Account;
import com.fonctionality.entity.AccountName;
import com.fonctionality.entity.Currency;
import com.fonctionality.entity.TypeAccount;

import java.time.LocalDateTime;

public record AccountResponse(
        Long id,
        AccountName name,
        Double balance,
        LocalDateTime lastUpdateDateTime,

        Currency currency,
        TypeAccount account_type
) {

    public static AccountResponse toResponse(Account ac)  {
        return new AccountResponse(
                ac.getId(),
                ac.getName(),
                ac.getBalance(),
                ac.getLastUpdateDateTime(),
                ac.getCurrency(),
                ac.getAccount_type()
        );
    }
}
