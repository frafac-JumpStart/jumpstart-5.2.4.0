package jumpstart.web.components;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Session;

/**
 *	This component is identical to the portion of Tapestry's default ExceptionReport page that
 *	handles productionMode=false.
 */

public class ExceptionAndSessionDisplay {

	@Parameter(required = true)
	@Property
	@SuppressWarnings("unused")
	private Throwable _exception;

	@Inject
	@Property
	private Request _request;

	@Property
	private String _attributeName;

	public boolean getHasSession() {
		return _request.getSession(false) != null;
	}

	public Session getSession() {
		return _request.getSession(false);
	}

	public Object getAttributeValue() {
		return getSession().getAttribute(_attributeName);
	}

}