package jumpstart.web.components.examples.javascript;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

/**
 * TextEchoer contains a TextField and a Zone that "echoes" the value of the TextField.
 */
public class TextEchoer {

	@Parameter(required = true, principal = true, autoconnect = true)
	@Property
	private String _value;

	@InjectComponent
	private Zone _echoZone;

	@Inject
	private Request _request;

	Object onValueChanged() {
		_value = _request.getParameter("param");
		if (_value == null) {
			_value = "";
		}
		return _echoZone.getBody();
	}

	public String getEcho() {
		return _value;
	}
	
}
