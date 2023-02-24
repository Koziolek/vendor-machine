package pl.koziolekweb.vendormachine.products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.koziolekweb.vendormachine.persons.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    Optional<Product> findByIdAndSeller(@Param("id") UUID id,@Param("user") User seller);
}
