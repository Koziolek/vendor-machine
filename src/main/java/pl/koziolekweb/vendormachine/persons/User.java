package pl.koziolekweb.vendormachine.persons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class User {
	@Id
	private String username;
	@JsonIgnore
	private String password;
	@Enumerated(EnumType.STRING)
	private Role role;
	private long deposit;

	public long deposit(long amount) {
		this.deposit += amount;
		return this.deposit;
	}
}
