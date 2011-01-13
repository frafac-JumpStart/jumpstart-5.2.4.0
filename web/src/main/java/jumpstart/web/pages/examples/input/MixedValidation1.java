package jumpstart.web.pages.examples.input;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;

public class MixedValidation1 {

	// Screen fields

	@Property
	private String _firstName;

	@Property
	private String _lastName;

	// Other pages

	@InjectPage
	private MixedValidation2 _page2;

	// Generally useful bits and pieces

	@Component(id = "inputs")
	private Form _form;

	@Component(id = "firstName")
	private TextField _firstNameField;

	@Component(id = "lastName")
	private TextField _lastNameField;

	// The code

	void onValidateForm() {

		// Error if the names don't contain letters only

		if (_firstName != null) {
			if (!_firstName.matches("[A-Za-z]+")) {
				_form.recordError(_firstNameField, "First Name must contain letters only");
			}
		}

		if (_lastName != null) {
			if (!_lastName.matches("[A-Za-z]+")) {
				_form.recordError(_lastNameField, "Last Name must contain letters only");
			}
		}
	}

	Object onSuccess() {
		_page2.set(_firstName, _lastName);
		return _page2;
	}
}
