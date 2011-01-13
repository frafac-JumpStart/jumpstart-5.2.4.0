package jumpstart.web.pages.examples.component;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

public class EmbeddedEventBubbling {
	
	@SuppressWarnings("unused")
	@Property
	@Persist(PersistenceConstants.FLASH)
	private String _message;
	
	void onActionFromBubbleUp() {
		_message = "The page handled the event with onActionFromBubbleUp()";
	}

	// This method will not be called.
	void onActionFromNoBubbleUp() {
		_message = "The page handled the event with onActionFromNoBubbleUp().";
	}
}