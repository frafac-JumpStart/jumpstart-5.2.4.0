package jumpstart.web.pages.examples.lang;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.corelib.components.PageLink;

public class AtComponent {

	@SuppressWarnings("unused")
	// This provides the invisible instrumentation of the 3rd page link.
	@Component(id = "index", parameters = {"page=Index"})
	private PageLink _index;
	
}
