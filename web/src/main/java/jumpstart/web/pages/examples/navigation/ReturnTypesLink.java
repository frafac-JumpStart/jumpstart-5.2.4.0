package jumpstart.web.pages.examples.navigation;

public class ReturnTypesLink {

	private String _parameter;

	// set() is public so that other pages can use it to set up this page.
	
	public void set(String parameter) {
		_parameter = parameter;
	}
	
	// onPassivate() is called by Tapestry to get the activation context to put in the URL.
	
	String onPassivate() {
		return _parameter;
	}

	// onActivate() is called by Tapestry to pass in the activation context from the URL.

	void onActivate(String parameter) {
		_parameter = parameter;
	}

	public String getParameter() {
		return _parameter;
	}
}
