/**
 * A simple mixin for attaching javascript that invokes a listener in the component via AJAX.
 * Based on http://tinybits.blogspot.com/2010/03/new-and-better-zoneupdater.html
 * and http://tinybits.blogspot.com/2009/05/simple-onevent-mixin.html.
 */
package jumpstart.web.mixins;

import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;

// This annotation tells Tapestry to declare the js file in the page so that the browser will pull it in.
@IncludeJavaScriptLibrary("ajax_validator.js")
public class AjaxValidator {

	// Useful bits and pieces

	@Inject
	private ComponentResources resources;

	@Environmental
	private RenderSupport renderSupport;

	/**
	 * The element we attach ourselves to
	 */
	@InjectContainer
	private ClientElement element;

	// The code

	void afterRender() {
		String listenerURI = resources.createEventLink("ajaxValidate").toAbsoluteURI();
		String elementId = element.getClientId();
		
		JSONObject spec = new JSONObject();
		spec.put("listenerURI", listenerURI);
		spec.put("elementId", elementId);

		// Tell Tapestry to add some javascript that instantiates an AjaxValidator for the element we're mixing into.
		// Tapestry will put it at the end of the page in a section that runs once the DOM has been loaded.
		// The AjaxValidator class it refers to is NOT THIS class - it is actually the one defined in ajax_validator.js.
		renderSupport.addScript("new AjaxValidator(%s)", spec.toString());
	}
}
