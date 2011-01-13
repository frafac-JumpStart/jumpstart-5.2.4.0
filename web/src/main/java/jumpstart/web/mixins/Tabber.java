/**
 * A mixin that watches that watches every link (<a>) contained in the given element. 
 * When one is clicked it sets the class of the link to "active" and the class of the others to "".
 */

package jumpstart.web.mixins;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;

//This annotation tells Tapestry to declare the js file in the page so that the browser will pull it in.
@IncludeJavaScriptLibrary("tabber.js")
public class Tabber {

	@Parameter(defaultPrefix = BindingConstants.PROP)
	private Integer activeTabNum;

	@Inject
	private RenderSupport renderSupport;

	@InjectContainer
	private ClientElement element;

	@AfterRender
	public void afterRender() {
		// Tell Tapestry to add some javascript that instantiates a Tabber for the element we're mixing into.
		// Tapestry will put it at the end of the page in a section that runs once the DOM has been loaded.
		// The Tabber class it refers to is NOT THIS class - it is actually the one defined in tabber.js.
		renderSupport.addScript(String.format("new Tabber('%s', '%d');", element.getClientId(), activeTabNum));
	}

}
