package jumpstart.web.pages.examples.styling;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class StylingLinksAndSubmits1 {

	// Screen fields

	@Property
	private String _firstName;
	
	@SuppressWarnings("unused")
	@Inject
	@Path("context:images/next-button-48.png")
	@Property
	private Asset _nextImage;

	// Other pages
	
	@InjectPage
	private StylingLinksAndSubmits2 _page2;

	// The code
	
	Object onSuccess() {
		_page2.set(_firstName);
		return _page2;
	}

	Object onActionFromAL1(String context) {
		_page2.set(context);
		return _page2;
	}

	Object onActionFromAL2(String context) {
		_page2.set(context);
		return _page2;
	}

	Object onNextPage(String context) {
		_page2.set(context);
		return _page2;
	}
	
}
