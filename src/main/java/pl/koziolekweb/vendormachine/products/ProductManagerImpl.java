package pl.koziolekweb.vendormachine.products;

import io.vavr.control.Either;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import pl.koziolekweb.vendormachine.persons.User;

@Component
@RequiredArgsConstructor
@Log
class ProductManagerImpl implements ProductManager {
	private final ProductRepository repository;
	private final ProductMappers productMappers;

	@Override
	public Product createProduct(CreateProductRequest productRequest, User creator) {
		var product = productMappers.fromCreate(productRequest, creator);
		return repository.save(product);
	}

	@Override
	public Either<String, Product> updateProduct(UpdateProductRequest productRequest, User updater) {
		return repository.findByIdAndSeller(productRequest.id(), updater)
				.map(p -> productMappers.fromUpdate(productRequest, p.getSeller()))
				.map(repository::save)
				.map(Either::<String, Product>right)
				.orElse(Either.left("Product does not exists"));
	}

	@Override
	public void deleteProduct(DeleteProductRequest productRequest, User updater) {
		repository.findByIdAndSeller(productRequest.id(), updater)
				.ifPresent(repository::delete);
	}

	@Override
	public Collection<Product> all() {
		return repository.findAll();
	}
}
