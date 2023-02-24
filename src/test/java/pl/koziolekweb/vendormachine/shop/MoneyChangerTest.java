package pl.koziolekweb.vendormachine.shop;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MoneyChangerTest {
    MoneyChanger sut;

    @BeforeEach
    void setUp() {
        sut = new MoneyChanger();
    }

    @Test
    void shouldReturnValidCoins() {
        Assertions.assertThat(sut.charge(25)).contains(20, 5);
        Assertions.assertThat(sut.charge(205)).contains(100, 100, 5);
    }

    @Test
    void shouldReturnEmpty() {
        Assertions.assertThat(sut.charge(-1)).isEmpty();
        Assertions.assertThat(sut.charge(4)).isEmpty();
    }
}