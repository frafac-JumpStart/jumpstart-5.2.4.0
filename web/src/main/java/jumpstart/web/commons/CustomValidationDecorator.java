//
// Based on solutions by Piero Sartini at http://article.gmane.org/gmane.comp.java.tapestry.user/81532
// and Thiago H. de Paula Fiqueredo at http://old.nabble.com/Example-of-overriding-the-default-ValidationDecorator-td26906669.html. 
//

package jumpstart.web.commons;

import org.apache.tapestry5.BaseValidationDecorator;
import org.apache.tapestry5.Field;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.services.Environment;

/**
 * Like all ValidationDecorators, this decorator is invoked for every field being rendered server-side.
 * 
 * If the field is "required" then this adds the css class "required" to the enclosing element of the field and the
 * enclosing element of the label.
 * 
 * If the field has a validation error detected server-side then this adds the css class "error" to the enclosing
 * element of the field and the enclosing element of the label.
 */
public class CustomValidationDecorator extends BaseValidationDecorator {

	private final Environment environment;
	private final MarkupWriter markupWriter;

	public CustomValidationDecorator(Environment environment, MarkupWriter markupWriter) {
		this.environment = environment;
		this.markupWriter = markupWriter;
	}

	@Override
	public void insideLabel(Field field, Element element) {
		if (field == null) {
			return;
		}

		if (field.isRequired()) {
			element.addClassName("required-label");
			element.getContainer().addClassName("required-label-c");
		}

		if (inError(field)) {
			element.addClassName("error-label");
			element.getContainer().addClassName("error-label-c");
		}
	}

	@Override
	public void insideField(Field field) {

		if (field.isRequired()) {
			getElement().addClassName("required-field");
			getElement().getContainer().addClassName("required-field-c");
		}

		if (inError(field)) {
			getElement().addClassName("error-field");
			getElement().getContainer().addClassName("error-field-c");
		}
	}

	private boolean inError(Field field) {
		ValidationTracker tracker = getTracker();
		return tracker.inError(field);
	}

	private ValidationTracker getTracker() {
		return environment.peekRequired(ValidationTracker.class);
	}
	
	private Element getElement() {
		return markupWriter.getElement();
	}
}
