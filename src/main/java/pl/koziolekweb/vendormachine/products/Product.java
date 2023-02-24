package pl.koziolekweb.vendormachine.products;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.koziolekweb.vendormachine.persons.User;

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
