package services;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@ToString
public class BalanceHistory {
    private  LocalDateTime dateTime;
    private  double balance;
}
