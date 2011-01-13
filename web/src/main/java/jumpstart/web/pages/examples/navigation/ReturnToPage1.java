package jumpstart.web.pages.examples.navigation;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;

public class ReturnToPage1 {

	// The activation context

	@Property
	private String _arg1;
	
	// Other pages
	
	@InjectPage
	private ReturnToPage2 _page2;

	// Generally useful bits and pieces

	@Inject
	private PageRenderLinkSource _pageRenderLinkSource;

	// The code
	
	// onPassivate() is called by Tapestry to get the activation context to put
	// in the URL.

	String onPassivate() {
		return _arg1;
	}

	// onActivate() is called by Tapestry to pass in the activation context from
	// the URL.

	void onActivate(String arg1) throws Exception {
		_arg1 = arg1;
	}

	Object onSuccess() {
		Link thisPage = _pageRenderLinkSource.createPageRenderLinkWithContext(this.getClass(), onPassivate());
		_page2.set("Hello", thisPage);
		return _page2;
	}

}
