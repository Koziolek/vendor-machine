package pl.koziolekweb.vendormachine.validators;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

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

    // SLOW!
//    static Stream<Integer> incorrectValues(){
//        return IntStream.rangeClosed(Integer.MIN_VALUE, Integer.MAX_VALUE)
//                .filter(i -> !CoinValidator.valid.contains(i))
//                .boxed();
//    }

}