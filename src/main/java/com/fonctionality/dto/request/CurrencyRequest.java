package com.fonctionality.dto.request;

import com.fonctionality.entity.CodeCurrency;
import com.fonctionality.entity.NameCurrency;

public record CurrencyRequest(
        NameCurrency name,
        CodeCurrency code
) {
}
