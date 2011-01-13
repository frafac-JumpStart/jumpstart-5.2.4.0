package jumpstart.web.components;

import jumpstart.web.commons.CustomValidationDecorator;

import org.apache.tapestry5.Field;
import org.apache.tapestry5.FormValidationControl;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ValidationDecorator;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Environment;

/**
 * CustomForm simply wraps a Form so it can introduce our own custom validation decorator in place of the default one.
 */
@SupportsInformalParameters
public class CustomForm implements FormValidationControl {

	@Inject
	private Environment _environment;

	@Component(id = "form")
	private Form _form;

	/**
	 * This beginRender() will execute before our inner form's beginRender(). It gives us the chance to change the
	 * environment first - let's push our custom validation decorator onto the environment stack.
	 */
	void beginRender(MarkupWriter writer) {
		_environment.push(ValidationDecorator.class, new CustomValidationDecorator(_environment, writer));
	}

	/**
	 * This afterRender() will execute after our inner form's beginRender(). Let's undo what we did in beforeRender().
	 */
	void afterRender() {
		_environment.pop(ValidationDecorator.class);
	}

	@Override
	public void clearErrors() {
		_form.clearErrors();
	}

	@Override
	public boolean getHasErrors() {
		return _form.getHasErrors();
	}

	@Override
	public boolean isValid() {
		return _form.isValid();
	}

	@Override
	public void recordError(String errorMessage) {
		_form.recordError(errorMessage);
	}

	@Override
	public void recordError(Field field, String errorMessage) {
		_form.recordError(field, errorMessage);
	}
}
