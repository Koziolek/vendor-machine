package pl.koziolekweb.vendormachine.shop;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.koziolekweb.vendormachine.persons.UserRepository;
import pl.koziolekweb.vendormachine.utils.ApiHelper;

import java.security.Principal;
import java.util.List;

import static pl.koziolekweb.vendormachine.utils.ApiHelper.asResponse;

@RestController
@RequiredArgsConstructor
@Log
class VendorMachine {

    private final UserRepository userRepository;
    private final VendorMachineGuts guts;
    private final ProductResponseMapper mapper;

    @PostMapping("/buy")
    @PreAuthorize("hasAnyRole('ROLE_BUYER')")
    public ResponseEntity<?> buy(@RequestBody BuyProductRequest request, Principal principal) {
        var currentUser = userRepository.findById(principal.getName()).get();

        var result = guts.disposeProduct(request, currentUser)
                .bimap(
                        l -> ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(l),
                        r -> ResponseEntity.ok(mapper.fromLedger(r))
                );

        return asResponse(result);
    }

}

record BuyProductRequest(String productId, long amount) {
}
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
record BuyProductResponse(long totalCost, List<Integer> coins) {
}
