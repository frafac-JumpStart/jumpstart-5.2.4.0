package jumpstart.web.pages.examples.state;

/**
 * This page demonstrates using the activation context to remember data through the redirect.
 * The data will be tacked onto the end of the render request URL.
 */
public class PassingDataBetweenPages2 {

	// The activation context

	private String _firstName;

	private String _lastName;

	// The code
	
	// set() is public so that other pages can use it to set up this page.
	
	public void set(String firstName, String lastName) {
		_firstName = firstName;
		_lastName = lastName;
	}
	
	// onPassivate() is called by Tapestry to get the activation context to put in the URL.
	
	Object[] onPassivate() {
		return new String[] { _firstName, _lastName };
	}

	// onActivate() is called by Tapestry to pass in the activation context from the URL.

	void onActivate(String firstName, String lastName) {
		_firstName = firstName;
		_lastName = lastName;
	}

	public String getName() {
		return _firstName + " " + _lastName;
	}
}
