package com.fonctionality.entity;

import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class Currency {
    private Long id;
    private NameCurrency name;
    private CodeCurrency code;
    private List<CurrencyValue> sourceCurrencies = new ArrayList<> ();
    private List<CurrencyValue> destinationCurrencies = new ArrayList<> ();
}
