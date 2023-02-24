package pl.koziolekweb.vendormachine.persons;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class UserTest {

	@Test
	void deposit() {
		var user = User.builder()
				.deposit(0L)
				.build();
		user.deposit(100);
		Assertions.assertThat(user.getDeposit()).isEqualTo(100L);
	}
}