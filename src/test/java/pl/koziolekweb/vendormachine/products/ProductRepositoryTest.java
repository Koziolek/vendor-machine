package pl.koziolekweb.vendormachine.products;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.koziolekweb.vendormachine.persons.Role;
import pl.koziolekweb.vendormachine.persons.User;
import pl.koziolekweb.vendormachine.persons.UserRepository;

@SpringBootTest
class ProductRepositoryTest {

	@Autowired
	private ProductRepository repository;
	@Autowired
	private UserRepository userRepository;

	@Test
	void shouldFindProductByIdAndSeller() {
		var seller = userRepository.save(User.builder()
				.role(Role.SELLER)
				.username("Seller")
				.password("Seller")
				.build());

		var product = repository.save(Product.builder()
				.cost(1)
				.seller(seller)
				.name("test")
				.amount(1)
				.build());

		Assertions.assertThat(
				repository.findByIdAndSeller(product.getId(), seller)).isPresent();
	}

	@Test
	void shouldNotFindProductByIdAndSeller() {
		var seller = userRepository.save(User.builder()
				.role(Role.SELLER)
				.username("Seller")
				.password("Seller")
				.build());

		var seller2 = userRepository.save(User.builder()
				.role(Role.SELLER)
				.username("Seller2")
				.password("Seller2")
				.build());

		var product = repository.save(Product.builder()
				.cost(1)
				.seller(seller)
				.name("test")
				.amount(1)
				.build());

		Assertions.assertThat(
				repository.findByIdAndSeller(product.getId(), seller2)).isEmpty();
	}

}