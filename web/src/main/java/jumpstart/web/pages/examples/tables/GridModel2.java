package jumpstart.web.pages.examples.tables;

import org.apache.tapestry5.annotations.Property;

public class GridModel2 {

	// Screen fields

	@Property
	private String _firstName;
	
	// The code
	
	String onPassivate() {
		return _firstName;
	}

	void onActivate(String firstName) {
		_firstName = firstName;
	}
}