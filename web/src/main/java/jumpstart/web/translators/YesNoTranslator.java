package jumpstart.web.translators;

import org.apache.tapestry5.Field;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.internal.translator.AbstractTranslator;
import org.apache.tapestry5.services.FormSupport;

public class YesNoTranslator extends AbstractTranslator<Boolean> {

	public YesNoTranslator() {
		super("yesno", Boolean.class, "yesno-format-exception");
	}

	public Boolean parseClient(Field field, String clientValue, String message) throws ValidationException {
		if (clientValue == null) {
			return null;
		}
		else {
			String s = clientValue.toLowerCase();
			if (s.equals("y") || s.equals("yes")) {
				return Boolean.TRUE;
			}
			else if (s.equals("n") || s.equals("no")) {
				return Boolean.FALSE;
			}
			else {
				throw new ValidationException(message);
			}
		}
	}

	public String toClient(Boolean value) {
		return (value == null ? null : value == true ? "yes" : "no");
	}

	public void render(Field field, String message, MarkupWriter writer, FormSupport formSupport) {
		// Do nothing; we don't yet support client-side validation.
		// formSupport.addValidation(field, "yesno", message, null);
	}
}
