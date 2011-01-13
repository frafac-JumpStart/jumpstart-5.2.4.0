package jumpstart.web.pages.examples.javascript;

import java.util.Date;

import jumpstart.web.pages.Index;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

public class AJAXOnEvent {

	// Screen fields

	@Property
	@Persist
	private String _firstName;

	@Property
	@Persist
	private String _lastName;

	// Generally useful bits and pieces

	@InjectComponent
	private Zone _nameZone;

	@Inject
	private Request _request;

	@Inject
	private ComponentResources _componentResources;

	// The code
	
	/* Life-cycle stuff. Fields that are marked @Persist MUST be initialized here rather than where they are declared. */

	void setupRender() {
		if (_firstName == null && _lastName == null) {
			_firstName = "Humpty";
			_lastName = "Dumpty";
		}
	}

	Object onFirstNameChanged() {
		_firstName = _request.getParameter("param");
		if (_firstName == null) {
			_firstName = "";
		}
		return _nameZone.getBody();
	}

	Object onLastNameChanged() {
		_lastName = _request.getParameter("param");
		if (_lastName == null) {
			_lastName = "";
		}
		return _nameZone.getBody();
	}

	public String getName() {
		return _firstName + " " + _lastName;
	}

	public Date getTime() {
		return new Date();
	}

	Object onActionFromGoHome() {
		_componentResources.discardPersistentFieldChanges();
		return Index.class;
	}
}
