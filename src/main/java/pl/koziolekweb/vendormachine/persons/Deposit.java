package pl.koziolekweb.vendormachine.persons;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.koziolekweb.vendormachine.validators.Coin;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Log
class Deposit {

    private final UserRepository userRepository;

    @PostMapping("/deposit")
    @PreAuthorize("hasAnyRole('ROLE_BUYER')")
    public ResponseEntity<?> deposit(@RequestBody @Valid DepositRequest request, Principal principal) {
        var currentUser = userRepository.findById(principal.getName()).get();
        return ResponseEntity.ok(currentUser);
    }

    @GetMapping("/reset")
    @PreAuthorize("hasAnyRole('ROLE_BUYER')")
    public ResponseEntity<?> reset() {

        return ResponseEntity.ok("");
    }

}


record DepositRequest(List<@Coin Integer> coins) {
}