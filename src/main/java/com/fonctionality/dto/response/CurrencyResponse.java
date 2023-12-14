package com.fonctionality.dto.response;

import com.fonctionality.entity.CodeCurrency;
import com.fonctionality.entity.Currency;
import com.fonctionality.entity.NameCurrency;

public record CurrencyResponse(
        Long id,
        NameCurrency name,
        CodeCurrency code
) {
    public static CurrencyResponse from(Currency currency) {
        return new CurrencyResponse(
                currency.getId(),
                currency.getName(),
                currency.getCode()
        );
    }
}
