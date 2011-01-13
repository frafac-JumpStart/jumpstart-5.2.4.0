package jumpstart.web.pages.examples.input;


public class Forms2 {

	private String _firstName;

	private String _lastName;

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
