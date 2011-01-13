package jumpstart.web.pages.examples.javascript;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;

public class AJAXComponents1 {

	// Screen fields

	@Property
	private String _firstName;

	@Property
	private String _lastName;

	// Other pages

	@InjectPage
	private AJAXComponents2 _page2;

	// The code
	
	/* Life-cycle stuff. Fields that are marked @Persist MUST be initialized here rather than where they are declared. */

	void setupRender() {
		if (_firstName == null && _lastName == null) {
			_firstName = "Humpty";
			_lastName = "Dumpty";
		}
	}

	Object onSuccess() {
		_page2.set(_firstName, _lastName);
		return _page2;
	}

}
