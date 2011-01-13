package jumpstart.web.pages.theapp;

import jumpstart.business.commons.exception.BusinessException;
import jumpstart.business.domain.security.User;
import jumpstart.business.domain.security.iface.ISecurityFinderServiceLocal;
import jumpstart.web.base.theapp.SimpleBasePage;
import jumpstart.web.pages.theapp.general.Welcome;
import jumpstart.web.state.theapp.Visit;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;

// To make this page accessible only by HTTPS, annotate it with @Secure and ensure your web server can deliver HTTPS.
// See http://tapestry.apache.org/tapestry5/guide/secure.html .
// @Secure
public class Login extends SimpleBasePage {

	// The activation context

	@Property
	private String _loginId;

	// Screen fields

	@Property
	private String _password;

	// Generally useful bits and pieces

	@Component(id = "login")
	private Form _form;

	@Component(id = "loginId")
	private TextField _loginIdField;

	@Inject
	private Logger _logger;

	// The code

	String onPassivate() {
		return _loginId;
	}

	void onActivate(String loginId) {
		_loginId = loginId;
	}

	void onValidateForm() {

		if (_form.getHasErrors()) {
			// We get here only if a validator detected an error and javascript is disabled in the browser.
			return;
		}

		try {
			// Authenticate the user

			User user = getSecurityFinderService().authenticateUser(_loginId, _password);

			// Store the user in the Visit

			Visit visit = getVisit();
			visit.noteLogin(user);
			_logger.info(user.getLoginId() + " has logged in.");
		}
		catch (BusinessException e) {
			_form.recordError(_loginIdField, e.getLocalizedMessage());
		}
		catch (Exception e) {
			_logger.info("Could not log in.  Stack trace follows...");
			e.printStackTrace();
			_form.recordError(getMessages().get("login_problem"));
		}
	}

	Object onSuccess() {
		return Welcome.class;
	}

	private ISecurityFinderServiceLocal getSecurityFinderService() {
		return getBusinessServicesLocator().getSecurityFinderServiceLocal();
	}

}
