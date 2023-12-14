package com.fonctionality.dto.request;

import com.fonctionality.entity.AccountName;
import com.fonctionality.entity.Currency;
import com.fonctionality.entity.TypeAccount;

import java.time.LocalDateTime;
public record AccountRequest(
         AccountName name,
         Double balance,
         LocalDateTime lastUpdateDateTime,

         Currency currency,
        TypeAccount account_type
) {
}
