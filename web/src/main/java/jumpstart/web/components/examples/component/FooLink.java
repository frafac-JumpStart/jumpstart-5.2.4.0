package jumpstart.web.components.examples.component;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * MyActionLink contains an ActionLink and a message that describes its parameters when clicked.
 */
public class FooLink {

	// Screen fields

	@SuppressWarnings("unused")
	@Property
	@Persist(PersistenceConstants.FLASH)
	private String _message;

	// Generally useful bits and pieces

	@Inject
	private ComponentResources _componentResources;

	// The code
	
	boolean onActionFromComponentA() {
		_message = "componenta triggered \"action\" in " + _componentResources.getId()
				+ ", a FooLink.  FooLink aborts \"action\" and triggers \"foo\".";

		// Trigger the event "foo" on myself which will then bubble up.

		_componentResources.triggerEvent("foo", null, null);

		// Abort the bubbling of event "action" by returning true.

		return true;
	}

	public String getLinkText() {
		return "A link that results in \"foo\" instead of \"action\"";
	}
}
