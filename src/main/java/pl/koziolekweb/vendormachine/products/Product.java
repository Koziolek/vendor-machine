package pl.koziolekweb.vendormachine.products;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.koziolekweb.vendormachine.persons.User;

import java.util.UUID;

@NoArgsConstructor
@Data
@Entity
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private long amount;
    private int cost;
    private String name;
    @ManyToOne
    private User seller;
}
