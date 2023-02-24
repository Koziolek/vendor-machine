package pl.koziolekweb.vendormachine.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public abstract class AbstractDivideByValidator<T> implements ConstraintValidator<DividedBy, T> {

    protected long value;
    @Override
    public void initialize(DividedBy constraintAnnotation) {
        this.value = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(T actual, ConstraintValidatorContext context) {
        return isDivided(actual);
    }

    protected abstract boolean isDivided(T actual);
}
