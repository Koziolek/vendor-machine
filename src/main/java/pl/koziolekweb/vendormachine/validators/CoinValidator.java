package pl.koziolekweb.vendormachine.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class CoinValidator implements ConstraintValidator<Coin, Integer> {

    public static final List<Integer> valid = List.of(5, 10, 20, 50, 100);

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return valid.contains(value);
    }
}
