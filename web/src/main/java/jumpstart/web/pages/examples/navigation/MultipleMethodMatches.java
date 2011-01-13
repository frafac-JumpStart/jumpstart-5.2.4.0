package jumpstart.web.pages.examples.navigation;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

public class MultipleMethodMatches {
	
	// Screen fields

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String _message;
	
	// The code
	
	void onActivate() {
		if (_message == null) {
			_message = "";
		}
	}
	
	void onAction() {
		_message += "The event from the link was handled by onAction()";
	}
	
	Object onActionFromLink1() {
		_message += " and onActionFromLink1()";
		return null;
	}
	
	// This method is not called.
	Object onActionFromLink2() {
		_message += " and onActionFromLink2()";
		return null;
	}
	
	Object onActionFromLink2(String parameter) {
		_message += " and onActionFromLink2(parameter)";
		// To prevent onActionFromLink2() also being called, we'd return true or any other valid non-null value.
		return null;
	}
}