package pl.koziolekweb.vendormachine.persons;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Log
class Deposit {

    @PostMapping("/deposit")
    @PreAuthorize("hasAnyRole('ROLE_BUYER')")
    public ResponseEntity<?> deposit(){

        return ResponseEntity.ok("");
    }

    @GetMapping("/reset")
    @PreAuthorize("hasAnyRole('ROLE_BUYER')")
    public ResponseEntity<?> reset(){

        return ResponseEntity.ok("");
    }

}


record DepositRequest(List<Integer> coins){}