package pl.koziolekweb.vendormachine.products;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import pl.koziolekweb.vendormachine.persons.User;

import java.util.Collection;

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
    public Product updateProduct(UpdateProductRequest productRequest, User updater) {
        return null;
    }

    @Override
    public Product deleteProduct(DeleteProductRequest productRequest, User updater) {
        return null;
    }

    @Override
    public Collection<Product> all() {
        return repository.findAll();
    }
}
