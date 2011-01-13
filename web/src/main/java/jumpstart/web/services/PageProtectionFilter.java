// Based on http://tapestryjava.blogspot.com/2009/12/securing-tapestry-pages-with.html

package jumpstart.web.services;

import java.io.IOException;

import jumpstart.business.domain.security.User;
import jumpstart.business.domain.security.iface.ISecurityFinderServiceLocal;
import jumpstart.client.IBusinessServicesLocator2;
import jumpstart.web.annotation.ProtectedPage;
import jumpstart.web.pages.theapp.Login;
import jumpstart.web.state.theapp.Visit;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.runtime.Component;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.ComponentRequestFilter;
import org.apache.tapestry5.services.ComponentRequestHandler;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.apache.tapestry5.services.Response;
import org.slf4j.Logger;

/**
 * A service that protects pages annotated with {@link jumpstart.web.annotation.ProtectedPage}. It examines each
 * {@link org.apache.tapestry5.services.Request} and redirects it to the login page if the request is for a
 * ProtectedPage and the user is not logged in.
 * <p>
 * To use it, insert it as a service into Tapestry's dispatch chain as we do in AppModule.
 * 
 */
public class PageProtectionFilter implements ComponentRequestFilter {
	private final String _autoLoginStr = System.getProperty("jumpstart.auto-login");

	private final PageRenderLinkSource _pageRenderLinkSource;
	private final ComponentSource _componentSource;
	private final Response _response;
	private ApplicationStateManager _sessionStateManager;
	private final IBusinessServicesLocator2 _businessServicesLocator;
	private final Logger _logger;

	/**
	 * Receive all the services needed as constructor arguments. When we bind this service, T5 IoC will provide all the
	 * services!
	 */
	public PageProtectionFilter(PageRenderLinkSource pageRenderLinkSource, ComponentSource componentSource,
			Response response, ApplicationStateManager asm, IBusinessServicesLocator2 businessServicesLocator,
			Logger logger) {
		_pageRenderLinkSource = pageRenderLinkSource;
		_response = response;
		_componentSource = componentSource;
		_sessionStateManager = asm;
		_businessServicesLocator = businessServicesLocator;
		_logger = logger;
	}

	public void handlePageRender(PageRenderRequestParameters parameters, ComponentRequestHandler handler)
			throws IOException {

		if (isAuthorisedToPage(parameters.getLogicalPageName())) {
			handler.handlePageRender(parameters);
		}
		else {
			// The method will have redirected us to the login page
			return;
		}

	}

	public void handleComponentEvent(ComponentEventRequestParameters parameters, ComponentRequestHandler handler)
			throws IOException {

		if (isAuthorisedToPage(parameters.getActivePageName())) {
			handler.handleComponentEvent(parameters);
		}
		else {
			// The method will have redirected us to the login page
			return;
		}

	}

	public boolean isAuthorisedToPage(String requestedPageName) throws IOException {

		// If the requested page is annotated @ProtectedPage...

		Component page = _componentSource.getPage(requestedPageName);
		boolean protectedPage = page.getClass().getAnnotation(ProtectedPage.class) != null;

		if (protectedPage) {

			// If the session contains a Visit then you have already been authenticated

			if (_sessionStateManager.exists(Visit.class)) {
				// We could do some role checking where but we won't. You're authorised.
				return true;
			}

			// Else if "auto-login" is on, then automatically log in.
			// - this facility is for development environment only. It avoids getting you thrown out of the
			// app every time the session clears eg. when app is restarted.

			else if (isAutoLoginOn()) {
				autoLogin(1L);
				return true;
			}

			// Else go to the Login page

			else {
				Link loginPageLink = _pageRenderLinkSource.createPageRenderLink(Login.class);
				_response.sendRedirect(loginPageLink);
				return false;
			}

		}
		else {
			return true;
		}

	}

	/**
	 * Checks the value of system property jumpstart.auto-login. If "true" then returns true; if "false" then return
	 * false; if not set then returns false.
	 */
	private boolean isAutoLoginOn() {
		boolean autoLogin = false;
		if (_autoLoginStr == null) {
			autoLogin = false;
		}
		else if (_autoLoginStr.equalsIgnoreCase("true")) {
			autoLogin = true;
		}
		else if (_autoLoginStr.equalsIgnoreCase("false")) {
			autoLogin = false;
		}
		else {
			throw new IllegalStateException(
					"System property jumpstart.auto-login has been set to \""
							+ _autoLoginStr
							+ "\".  Please set it to \"true\" or \"false\".  If not specified at all then it will default to \"false\".");
		}
		return autoLogin;
	}

	/**
	 * Automatically logs you in as the given user. Its intention is to prevent you being thrown out of the application
	 */
	private void autoLogin(Long userId) {
		try {
			User user = getSecurityFinderService().findUser(userId);

			Visit visit = new Visit();
			visit.noteLogin(user);
			_logger.info(user.getLoginId() + " has been auto-logged-in.");

			_sessionStateManager.set(Visit.class, visit);
		}
		catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	private ISecurityFinderServiceLocal getSecurityFinderService() {
		return _businessServicesLocator.getSecurityFinderServiceLocal();
	}
}
