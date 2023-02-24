package pl.koziolekweb.vendormachine.products;

import io.vavr.control.Either;
import java.util.Collection;
import pl.koziolekweb.vendormachine.persons.User;

public interface ProductManager {

	Product createProduct(CreateProductRequest productRequest, User creator);

	Either<String, Product> updateProduct(UpdateProductRequest productRequest, User updater);

	void deleteProduct(DeleteProductRequest productRequest, User updater);

	Collection<Product> all();
}
