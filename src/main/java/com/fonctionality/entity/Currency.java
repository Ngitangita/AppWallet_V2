package com.fonctionality.entity;

import lombok.*;


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
}
