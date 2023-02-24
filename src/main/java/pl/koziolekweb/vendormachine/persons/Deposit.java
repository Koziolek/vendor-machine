package pl.koziolekweb.vendormachine.persons;

import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.koziolekweb.vendormachine.validators.Coin;

@RestController
@RequiredArgsConstructor
@Log
class Deposit {

	private final UserRepository userRepository;

	@PostMapping("/deposit")
	@PreAuthorize("hasAnyRole('ROLE_BUYER')")
	public ResponseEntity<?> deposit(@RequestBody @Valid DepositRequest request, Principal principal) {
		var currentUser = userRepository.findById(principal.getName()).get();
		currentUser.setDeposit(request.sum());
		return ResponseEntity.ok(userRepository.save(currentUser));
	}

	@PostMapping("/reset")
	@PreAuthorize("hasAnyRole('ROLE_BUYER')")
	public ResponseEntity<?> reset(Principal principal) {
		var currentUser = userRepository.findById(principal.getName()).get();
		currentUser.setDeposit(0);
		return ResponseEntity.ok(userRepository.save(currentUser));
	}

}

record DepositRequest(List<@Coin Integer> coins) {
	long sum() {
		return coins.stream().reduce(Integer::sum).orElse(0);
	}
}