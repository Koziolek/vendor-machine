package pl.koziolekweb.vendormachine.products;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.koziolekweb.vendormachine.persons.User;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

	Optional<Product> findByIdAndSeller(@Param("id") UUID id, @Param("user") User seller);
}
