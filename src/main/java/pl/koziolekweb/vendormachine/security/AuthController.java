package pl.koziolekweb.vendormachine.security;

import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
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
                        }
                );
    }

    @PostMapping(value = "/register-seller", consumes = {"application/json"})
    public ResponseEntity<?> registerSeller(@RequestBody RegisterUser user) {
        register(user, Role.SELLER);
        return ResponseEntity.ok("OK");
    }

    @PostMapping(value = "/register-buyer", consumes = {"application/json"})
    public ResponseEntity<?> registerBuyer(@RequestBody RegisterUser user) {
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

record AuthRequest(String username, String password) {
}

record RegisterUser(String username, String password) {
}