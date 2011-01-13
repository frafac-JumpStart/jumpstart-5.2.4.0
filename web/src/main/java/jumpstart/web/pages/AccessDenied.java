package jumpstart.web.pages;

import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.RequestGlobals;

/**
 * Intended for use with AssetProtectionFilter.
 */
public class AccessDenied {

	// Screen fields

	@SuppressWarnings("unused")
	@Inject
	@Symbol(SymbolConstants.PRODUCTION_MODE)
	@Property
	private boolean _productionMode;

	// Other useful bits and pieces

	@Inject
	private RequestGlobals _requestGlobals;

	// The code

	public void setupRender() {
		_requestGlobals.getHTTPServletResponse().setStatus(HttpServletResponse.SC_NOT_FOUND);
	}

}
