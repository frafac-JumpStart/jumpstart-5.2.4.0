package jumpstart.web.pages.examples.input;

import org.apache.tapestry5.annotations.Property;

public class CreatingTranslators2 {

	// Screen fields

	@Property(write = false)
	private Boolean _newToTapestry;

	@Property(write = false)
	private Boolean _newToJava;
	
	// The code
	
	// set() is public so that other pages can use it to set up this page.
	
	public void set(Boolean newToTapestry, Boolean newToJava) {
		_newToTapestry = newToTapestry;
		_newToJava = newToJava;
	}
	
	// onPassivate() is called by Tapestry to get the activation context to put in the URL.
	
	Object[] onPassivate() {
		return new Object[] {_newToTapestry, _newToJava};
	}
	
	// onActivate() is called by Tapestry to pass in the activation context from the URL.

	void onActivate(Boolean newToTapestry, Boolean newToJava) {
		_newToTapestry = newToTapestry;
		_newToJava = newToJava;
	}
}
