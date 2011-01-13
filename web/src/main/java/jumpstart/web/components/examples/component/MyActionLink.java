package jumpstart.web.components.examples.component;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * MyActionLink contains an ActionLink and a message that describes its parameters when clicked.
 */
public class MyActionLink {
	
	// Parameters

	@Parameter
	@Property
	private boolean _bubbleup = false;

	// Screen fields

	@SuppressWarnings("unused")
	@Property
	@Persist(PersistenceConstants.FLASH)
	private String _message;
	
	// Generally useful bits and pieces

	@SuppressWarnings("unused")
	@Inject
	private ComponentResources _componentResources;

	// The code
	
	boolean onActionFromComponentA() {
		boolean returnValue = !_bubbleup; 
		_message = "Event handled by MyActionLink method onActionFromComponentA().  Returning " + returnValue + ".";
		return returnValue;
	}
}
