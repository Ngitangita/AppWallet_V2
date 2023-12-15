package com.fonctionality.dto.response;

import java.time.LocalDateTime;

public record SoldWithDate(
        LocalDateTime dateTime,
        Double sold
) {
}
