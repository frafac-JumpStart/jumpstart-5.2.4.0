package jumpstart.web.pages.examples.input;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;

public class Validators1 {

	// Screen fields

	@Property
	private String _firstName;
	
	@Property
	private String _lastName;

	// Other pages

	@InjectPage
	private Validators2 _page2;

	// The code

	Object onSuccess() {
		_page2.set(_firstName, _lastName);
		return _page2;
	}
}
