package jumpstart.web.pages.examples.javascript;

import java.util.Date;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;

public class AJAXForm {

	// Screen fields

	@Property
	private String _firstName;

	@Property
	private String _lastName;

	// Generally useful bits and pieces

	@InjectComponent
	private Zone _nameZone;
	
	// The code
	
	void setupRender() {
		if (_firstName == null && _lastName == null) {
			_firstName = "Humpty";
			_lastName = "Dumpty";
		}
	}

	Object onSuccess() {
		return _nameZone.getBody();
	}

	public String getName() {
		return _firstName + " " + _lastName;
	}

	public Date getTime() {
		return new Date();
	}

}
