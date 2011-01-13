/**
 * A simple mixin for attaching javascript that prevents more than one click before the next page
 */
package jumpstart.web.mixins;

import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.ioc.annotations.Inject;

// This annotation tells Tapestry to declare the js file in the page so that the browser will pull it in.
@IncludeJavaScriptLibrary("click_once.js")
public class ClickOnce {

	@Inject
	private RenderSupport renderSupport;

	@InjectContainer
	private ClientElement element;

	@AfterRender
	public void afterRender() { 
		// Tell Tapestry to add some javascript that instantiates a ClickOnce for the element we're mixing into.
		// Tapestry will put it at the end of the page in a section that runs once the DOM has been loaded.
		// The ClickOnce class it refers to is NOT THIS class - it is actually the one defined in click_once.js.
		renderSupport.addScript(String.format("new ClickOnce('%s');", element.getClientId()));
	}

}
