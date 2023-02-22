package pl.koziolekweb.vendormachine.persons;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@With
public class Buyer extends User {

    private long deposit;

}
