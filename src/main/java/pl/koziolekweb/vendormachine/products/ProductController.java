package pl.koziolekweb.vendormachine.products;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static pl.koziolekweb.vendormachine.utils.ApiHelper.asResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.security.Principal;
import java.util.Collection;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.koziolekweb.vendormachine.persons.User;
import pl.koziolekweb.vendormachine.persons.UserRepository;
import pl.koziolekweb.vendormachine.validators.DividedBy;

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
	public ResponseEntity<?> save(@RequestBody @Valid CreateProductRequest request, Principal principal) {
		var currentUser = userRepository.findById(principal.getName()).get();
		var product = manager.createProduct(request, currentUser);
		return ResponseEntity.status(CREATED).body(product);
	}

	@PreAuthorize("hasAnyRole('ROLE_SELLER')")
	@PutMapping
	public ResponseEntity<?> update(@RequestBody UpdateProductRequest request, Principal principal) {
		final User currentUser = userRepository.findById(principal.getName()).get();
		var result = manager.updateProduct(request, currentUser)
				.bimap(
						l -> ResponseEntity.status(NOT_FOUND).build(),
						r -> ResponseEntity.status(CREATED).body(r));
		return asResponse(result);
	}

	@PreAuthorize("hasAnyRole('ROLE_SELLER')")
	@DeleteMapping
	public ResponseEntity<?> delete(@RequestBody DeleteProductRequest request, Principal principal) {
		final User currentUser = userRepository.findById(principal.getName()).get();
		manager.deleteProduct(request, currentUser);
		return ResponseEntity.noContent().build();
	}

}

record CreateProductRequest(@NotNull @Min(0) long amount, @NotNull @Min(0) @DividedBy(5) int cost,
		@NotNull @NotBlank String name) {
}

record UpdateProductRequest(@NotNull UUID id, @NotNull @Min(0) long amount, @NotNull @Min(0) @DividedBy(5) int cost,
		@NotNull @NotBlank String name) {
}

record DeleteProductRequest(@NotNull UUID id) {
}

record ChangeOwnerProductRequest(UUID id, String newSellerId) {
}
