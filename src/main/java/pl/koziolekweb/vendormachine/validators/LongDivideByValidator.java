package pl.koziolekweb.vendormachine.validators;

class LongDivideByValidator extends AbstractDivideByValidator<Long> {
	@Override
	protected boolean isDivided(Long actual) {
		return actual % value == 0;
	}
}
