package jumpstart.web.pages.examples.input;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;

public class CreatingTranslators1 {

	// Screen fields

	@Property
	private Boolean _newToTapestry;

	@Property
	private Boolean _newToJava;

	// Other pages

	@InjectPage
	private CreatingTranslators2 _page2;
	
	// The code

	Object onSuccess() {
		_page2.set(_newToTapestry, _newToJava);
		return _page2;
	}
}
