package pl.koziolekweb.vendormachine.validators;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class CoinValidatorTest {

	private final CoinValidator sut = new CoinValidator();

	static List<Integer> correctValues() {
		return CoinValidator.valid;
	}

	static List<Integer> incorrectValues() {
		return List.of(-1, 0, 1);
	}

	@ParameterizedTest
	@MethodSource("correctValues")
	void shouldValidateCorrectValues(Integer val) {
		Assertions.assertThat(sut.isValid(val, null)).isTrue();
	}

	@ParameterizedTest
	@MethodSource("incorrectValues")
	void shouldDropIncorrectValues(Integer val) {
		Assertions.assertThat(sut.isValid(val, null)).isFalse();
	}

}