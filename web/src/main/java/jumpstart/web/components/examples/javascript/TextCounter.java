package jumpstart.web.components.examples.javascript;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

/**
 * MyActionLink contains an ActionLink and a message that describes its parameters when clicked.
 */
public class TextCounter {

	@Parameter(required = true, principal = true, autoconnect = true)
	private String _value;

	@InjectComponent
	private Zone _countZone;

	@Inject
	private Request _request;
	
	Object onTheTextChanged() {
		_value = _request.getParameter("param");
		if (_value == null) {
			_value = "";
		}
		return _countZone.getBody();
	}

	public int getCount() {
		return _value.length();
	}
	
	public String getTheText() {
		return _value;
	}
	
	public void setTheText(String theText) {
		_value = theText;
	}
}
