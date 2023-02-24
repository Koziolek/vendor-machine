package pl.koziolekweb.vendormachine.persons;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void deposit() {
        var user = User.builder()
                .deposit(0l)
                .build();
                user.deposit(100);
        Assertions.assertThat(user.getDeposit()).isEqualTo(100l);
    }
}