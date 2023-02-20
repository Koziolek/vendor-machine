package pl.koziolekweb.vendormachine.products;

import pl.koziolekweb.vendormachine.persons.User;

import java.util.Collection;

public interface ProductManager {

    Product createProduct(CreateProductRequest productRequest, User creator);

    Product updateProduct(UpdateProductRequest productRequest, User updater);

    Product deleteProduct(DeleteProductRequest productRequest, User updater);

    Collection<Product> all();
}
