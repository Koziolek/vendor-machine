package pl.koziolekweb.vendormachine.products;

import io.vavr.control.Either;
import pl.koziolekweb.vendormachine.persons.User;

import java.util.Collection;

public interface ProductManager {

    Product createProduct(CreateProductRequest productRequest, User creator);

    Either<String, Product> updateProduct(UpdateProductRequest productRequest, User updater);

    void deleteProduct(DeleteProductRequest productRequest, User updater);

    Collection<Product> all();
}
