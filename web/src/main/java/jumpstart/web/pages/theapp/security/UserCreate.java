package jumpstart.web.pages.theapp.security;

import jumpstart.business.domain.security.User;
import jumpstart.business.domain.security.User.PageStyle;
import jumpstart.business.domain.security.iface.ISecurityManagerServiceLocal;
import jumpstart.web.annotation.ProtectedPage;
import jumpstart.web.base.theapp.SimpleBasePage;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.PasswordField;
import org.apache.tapestry5.util.EnumValueEncoder;

@SuppressWarnings("unchecked")
@ProtectedPage
public class UserCreate extends SimpleBasePage {

	// The User

	@Property
	private User _user;

	@Property
	private String _password;

	@Property
	private String _confirmPassword;

	@Component(id = "password")
	private PasswordField _passwordField;

	// Other pages

	@InjectPage
	private UserSearch _userSearch;

	// Generally useful bits and pieces

	@Component(id = "form")
	private Form _form;
	
	@SuppressWarnings( { "unused"} )
	@Property
	private EnumValueEncoder _pageStyleEncoder = new EnumValueEncoder(User.PageStyle.class);
	
	void onPrepare() {
		// Instantiate a User for the form fields to overlay.
		_user = new User();
		_user.setActive(true);
	}
	
	void onValidateForm() {

		if (_form.getHasErrors()) {
			// We get here only if a validator detected an error and javascript is disabled in the browser.
			return;
		}

		if (_password != null && _confirmPassword != null && !_password.equals(_confirmPassword)) {
			_form.recordError(_passwordField, getMessages().get("User_confirmpassword_does_not_match"));
			return;
		}

		try {
			getSecurityManagerService().createUser(_user, _password);
		}
		catch (Exception e) {
			_form.recordError(interpretBusinessServicesExceptionForCreate(e));
			return;
		}
	}

	Object onSuccess() {
		return _userSearch.createLinkWithLastSearch();
	}
	
	void onReset() {
	}

	Object onCancel() {
		return _userSearch.createLinkWithLastSearch();
	}

	private ISecurityManagerServiceLocal getSecurityManagerService() {
		return getBusinessServicesLocator().getSecurityManagerServiceLocal();
	}

	public String[] getSalutations() {
		return User.SALUTATIONS;
	}

	public PageStyle getBoxy() {
		return User.PageStyle.BOXY;
	}

	public PageStyle getWide() {
		return User.PageStyle.WIDE;
	}

	public String[] getDatePatterns() {
		return User.DATE_PATTERNS;
	}
}
