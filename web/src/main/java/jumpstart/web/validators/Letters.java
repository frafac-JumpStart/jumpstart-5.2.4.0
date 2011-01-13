package jumpstart.web.validators;

import org.apache.tapestry5.Field;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.ioc.MessageFormatter;
import org.apache.tapestry5.services.FormSupport;
import org.apache.tapestry5.validator.AbstractValidator;

/**
 *  A validator that enforces that a string contains alphabetic letters only.
 */
public class Letters extends AbstractValidator<Void, String> {

	public Letters() {
		super(null, String.class, "validate-letters");
	}

	/**
	 * Adds client-side validation by writing javascript into the page as it is rendered.
	 */
	@Override
	public void render(Field field, Void constraintValue, MessageFormatter formatter, MarkupWriter writer,
			FormSupport formSupport) {
		formSupport.addValidation(field, "letters", buildMessage(formatter, field), null);
	}

	/**
	 * Server-side validation.
	 */
	@Override
	public void validate(Field field, Void constraintValue, MessageFormatter formatter, String value)
			throws ValidationException {
		// This does the server-side validation
		if (value != null) {
			if (!value.matches("[A-Za-z]+")) {
				throw new ValidationException(buildMessage(formatter, field));
			}
		}

	}

	private String buildMessage(MessageFormatter formatter, Field field) {
		return formatter.format(field.getLabel());
	}

	public boolean isLetters() {
		return true;
	}

}
