package jumpstart.web.pages.examples.input;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;

public class Forms1 {

	// Screen fields

	@Property
	private String _firstName;

	@Property
	private String _lastName;

	// Other pages
	
	@InjectPage
	private Forms2 _page2;
	
	// Generally useful bits and pieces

	@Component(id = "names")
	private Form _form;

	@Component(id = "firstName")
	private TextField _firstNameField;

	@Component(id = "lastName")
	private TextField _lastNameField;

	// The code

	void onValidateForm() {
		if (_firstName == null || _firstName.trim().equals("")) {
			_form.recordError(_firstNameField, "First Name is required.");
		}
		if (_lastName == null || _lastName.trim().equals("")) {
			_form.recordError(_lastNameField, "Last Name is required.");
		}
	}

	Object onSuccess() {
		_page2.set(_firstName, _lastName);
		return _page2;
	}
}
