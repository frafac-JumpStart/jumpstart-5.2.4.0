package jumpstart.web.pages.examples.input;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;

/* This annotation tells Tapestry to declare the js file in the page so that the browser will pull it in. */
@Import(library={"context:js/validators.js", "context:js/custom_error.js"})
public class NoValidationBubbles1 {

	// Screen fields

	@Property
	private String _firstName;

	@Property
	private String _lastName;

	// Other pages

	@InjectPage
	private NoValidationBubbles2 _page2;

	// The code

	Object onSuccess() {
		_page2.set(_firstName, _lastName);
		return _page2;
	}
}
