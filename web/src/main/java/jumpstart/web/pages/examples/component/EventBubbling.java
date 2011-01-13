package jumpstart.web.pages.examples.component;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

public class EventBubbling {

	// Screen fields

	@SuppressWarnings("unused")
	@Property
	@Persist(PersistenceConstants.FLASH)
	private String _message;

	// The code
	
	void onActionFromComponent1() {
		_message = "Event handled by page's method onActionFromComponent1()";
	}

	void onActionFromComponent2() {
		throw new IllegalStateException("Cannot happen because we tell Component2 not to bubble up.");
	}

	void onActionFromComponentA() {
		throw new IllegalStateException("Cannot happen because ComponentA is not visible to the page.");
	}
}
