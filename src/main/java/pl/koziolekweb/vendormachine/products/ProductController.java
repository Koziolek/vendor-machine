package pl.koziolekweb.vendormachine.products;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.koziolekweb.vendormachine.persons.User;
import pl.koziolekweb.vendormachine.persons.UserRepository;

import java.security.Principal;
import java.util.Collection;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Log
class ProductController {

    private final ProductManager manager;
    private final UserRepository userRepository;

    @PreAuthorize("hasAnyRole('ROLE_SELLER', 'ROLE_BUYER')")
    @GetMapping
    public Collection<Product> all() {
        return manager.all();
    }

    @PreAuthorize("hasAnyRole('ROLE_SELLER')")
    @PostMapping
    public ResponseEntity<?> save(@RequestBody CreateProductRequest request, Principal principal) {
        final User currentUser = userRepository.findById(principal.getName()).get();
        final Product product = manager.createProduct(request, currentUser);
        return ResponseEntity.status(CREATED).body(product);
    }

    @PreAuthorize("hasAnyRole('ROLE_SELLER')")
    @PutMapping
    public ResponseEntity<?> update(@RequestBody UpdateProductRequest request, Principal principal) {
        final User currentUser = userRepository.findById(principal.getName()).get();
        var result = manager.updateProduct(request, currentUser)
                .bimap(
                        l -> ResponseEntity.status(NOT_FOUND).build(),
                        r -> ResponseEntity.status(CREATED).body(r)
                );
        if (result.isRight())
            return result.get();

        return result.getLeft();
    }

    @PreAuthorize("hasAnyRole('ROLE_SELLER')")
    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody DeleteProductRequest request, Principal principal) {
        final User currentUser = userRepository.findById(principal.getName()).get();
        manager.deleteProduct(request, currentUser);
        return ResponseEntity.noContent().build();
    }

}

record CreateProductRequest(long amount, int cost, String name) {
}

record UpdateProductRequest(UUID id, long amount, int cost, String name) {
}

record DeleteProductRequest(UUID id) {
}

record ChangeOwnerProductRequest(UUID id, String newSellerId) {
}
