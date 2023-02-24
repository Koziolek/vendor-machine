package pl.koziolekweb.vendormachine.validators;

class IntegerDivideByValidator extends AbstractDivideByValidator<Integer> {
	@Override
	protected boolean isDivided(Integer actual) {
		return actual % value == 0;
	}
}
