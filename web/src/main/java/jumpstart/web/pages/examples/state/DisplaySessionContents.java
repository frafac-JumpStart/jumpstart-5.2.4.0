package jumpstart.web.pages.examples.state;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Session;

/**
 *	This page is based on the portion of Tapestry's default ExceptionReport page that
 *	handles the session.
 */

public class DisplaySessionContents {

	// Screen fields
	
	@Property
	private String _attributeName;
	
	// Generally useful bits and pieces

	@Inject
	private Request _request;

	// The code
	
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