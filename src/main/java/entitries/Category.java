package entitries;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@ToString
public class Category {
    private Long id;
    private String name;
    private TypeTransaction typeTransaction;
    private List<Transaction> transactions = new ArrayList<> (  );
}
