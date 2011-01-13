package jumpstart.web.pages.examples.state;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.apache.tapestry5.services.Request;

public class PassingDataBetweenPages4 {
	
	// Request parameters

	private String _firstName;

	private String _lastName;
	
	// Generally useful bits and pieces

	@Inject
	private PageRenderLinkSource _pageRenderLinkSource;

	@Inject
	private Request _request;
	
	// The code

	// set() is public so that other pages can use it to set up this page.
	// Return a link with the non-null parameters in it.
	
	public Link set(String firstName, String lastName) {
		Link link = _pageRenderLinkSource.createPageRenderLink(this.getClass());

		if (firstName != null) {
			link.addParameter("firstname", firstName);
		}
		if (lastName != null) {
			link.addParameter("lastname", lastName);
		}
		
		return link;
	}

	// onActivate() usually extracts the activation context but here it extracts the parameters from the request.
	
	void onActivate() {
		_firstName = _request.getParameter("firstname");
		_lastName = _request.getParameter("lastname");
	}

	public String getName() {
		return _firstName + " " + _lastName;
	}
}
