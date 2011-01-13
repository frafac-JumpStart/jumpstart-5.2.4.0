package jumpstart.web.pages.theapp.security;

import jumpstart.business.commons.exception.DoesNotExistException;
import jumpstart.business.domain.security.User;
import jumpstart.business.domain.security.iface.ISecurityFinderServiceLocal;
import jumpstart.business.domain.security.iface.ISecurityManagerServiceLocal;
import jumpstart.web.annotation.ProtectedPage;
import jumpstart.web.base.theapp.SimpleBasePage;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.PasswordField;

@ProtectedPage
public class UserPasswordChange extends SimpleBasePage {

	@Property
	private Long _userId;

	@Property
	private User _user;

	@Property
	private String _newPassword;

	@Property
	private String _confirmNewPassword;

	@Component(id = "newPassword")
	private PasswordField _newPasswordField;

	@InjectPage
	private UserEdit _userEdit;

	@Component(id = "form")
	private Form _form;
	
	public void set(Long userId) {
		_userId = userId;
	}

	Long onPassivate() {
		return _userId;
	}

	void onActivate(Long userId) {
		_userId = userId;
	}

	void onPrepare() throws DoesNotExistException {
		_user = getSecurityFinderService().findUser(_userId);
	}
	
	void onValidateForm() {

		if (_form.getHasErrors()) {
			// We get here only if a validator detected an error and javascript is disabled in the browser.
			return;
		}

		if (_newPassword != null && _confirmNewPassword != null && !_newPassword.equals(_confirmNewPassword)) {
			_form.recordError(_newPasswordField, getMessages().get("User_confirmnewpassword_does_not_match"));
			return;
		}

		try {
			getSecurityManagerService().changeUserPassword(_userId, _newPassword);
			_userEdit.set(_user.getId());
		}
		catch (Exception e) {
			_form.recordError(interpretBusinessServicesExceptionForChange(e));
		}
	}

	Object onSuccess() {
		return _userEdit;
	}
	
	Object onCancel() {
		_userEdit.set(_userId) ;
		return _userEdit;
	}

	private ISecurityFinderServiceLocal getSecurityFinderService() {
		return getBusinessServicesLocator().getSecurityFinderServiceLocal();
	}

	private ISecurityManagerServiceLocal getSecurityManagerService() {
		return getBusinessServicesLocator().getSecurityManagerServiceLocal();
	}

}
