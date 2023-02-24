package pl.koziolekweb.vendormachine.security;

import io.vavr.control.Try;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.koziolekweb.vendormachine.persons.Role;
import pl.koziolekweb.vendormachine.persons.User;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final JwtUtils jwtUtils;
	private final JwtUserDetailsService userDetailsService;

	@PostMapping
	public ResponseEntity<?> auth(@RequestBody AuthRequest authRequest) {
		return Try.success(authRequest)
				.map(a -> new UsernamePasswordAuthenticationToken(a.username(), a.password()))
				.mapTry(authenticationManager::authenticate)
				.mapTry(unused -> userDetailsService.loadUserByUsername(authRequest.username()))
				.mapTry(jwtUtils::generateToken)
				.map(ResponseEntity::ok)
				.toEither()
				.getOrElseGet(
						t -> {
							t.printStackTrace();
							return ResponseEntity.status(HttpStatus.FORBIDDEN)
									.body(t.getMessage());
						});
	}

	@PostMapping(value = "/user", consumes = {"application/json"})
	public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterUser user) {
		register(user, user.role());
		return ResponseEntity.ok("OK");
	}

	@PostMapping(value = "/register-seller", consumes = {"application/json"})
	public ResponseEntity<?> registerSeller(@RequestBody @Valid RegisterUser user) {
		register(user, Role.SELLER);
		return ResponseEntity.ok("OK");
	}

	@PostMapping(value = "/register-buyer", consumes = {"application/json"})
	public ResponseEntity<?> registerBuyer(@RequestBody @Valid RegisterUser user) {
		register(user, Role.BUYER);
		return ResponseEntity.ok("OK");
	}

	private void register(RegisterUser user, Role role) {
		var newUser = User.builder()
				.username(user.username())
				.password(user.password())
				.role(role)
				.build();
		userDetailsService.registerUser(newUser);
	}
}

record AuthRequest(@NotNull String username, @NotNull String password) {
}

record RegisterUser(@NotNull String username, @NotNull String password, @Nullable Role role) {
}