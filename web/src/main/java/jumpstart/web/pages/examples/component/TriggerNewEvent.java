package jumpstart.web.pages.examples.component;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

public class TriggerNewEvent {

	// Screen fields

	@SuppressWarnings("unused")
	@Property
	@Persist(PersistenceConstants.FLASH)
	private String _message;

	// The code
	
	void onFooFromComponentB() {
		_message = "componentb triggered \"foo\" in the page.";
	}
	
	void onActionFromComponentB() {
		throw new IllegalStateException("Not possible because ComponentB() aborts bubbling of event \"action\".");
	}
}
