package jumpstart.web.pages.theapp.security;

import jumpstart.business.commons.exception.DoesNotExistException;
import jumpstart.business.domain.security.User;
import jumpstart.business.domain.security.iface.ISecurityFinderServiceLocal;
import jumpstart.business.domain.security.iface.ISecurityManagerServiceLocal;
import jumpstart.web.annotation.ProtectedPage;
import jumpstart.web.base.theapp.SimpleBasePage;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.PasswordField;

@ProtectedPage
public class MyPasswordChange extends SimpleBasePage {

	@SuppressWarnings("unused")
	@Property
	private User _user;

	@Property
	private String _currentPassword;

	@Property
	private String _newPassword;

	@Property
	private String _confirmNewPassword;

	@Component(id = "newPassword")
	private PasswordField _newPasswordField;

	@Component(id = "form")
	private Form _form;

	void onPrepare() throws DoesNotExistException {
		Long userId = getVisit().getMyUserId();
		_user = getSecurityFinderService().findUser(userId);
	}

	void onValidateForm() {

		if (_form.getHasErrors()) {
			// We get here only if a validator detected an error and javascript is disabled in the browser.
			return;
		}

		if (_newPassword != null && _newPassword.equals(_currentPassword)) {
			_form.recordError(_newPasswordField, getMessages().get("User_newpassword_same_as_current"));
			return;
		}

		if (_newPassword != null && _confirmNewPassword != null && !_newPassword.equals(_confirmNewPassword)) {
			_form.recordError(_newPasswordField, getMessages().get("User_confirmnewpassword_does_not_match"));
			return;
		}

		try {
			Long userId = getVisit().getMyUserId();
			getSecurityManagerService().changeUserPassword(userId, _currentPassword, _newPassword);
		}
		catch (Exception e) {
			_form.recordError(interpretBusinessServicesExceptionForChange(e));
		}
	}

	Object onSuccess() {
		return MyAccountView.class;
	}
	
	Object onCancel() {
		return MyAccountView.class;
	}

	private ISecurityFinderServiceLocal getSecurityFinderService() {
		return getBusinessServicesLocator().getSecurityFinderServiceLocal();
	}

	private ISecurityManagerServiceLocal getSecurityManagerService() {
		return getBusinessServicesLocator().getSecurityManagerServiceLocal();
	}

}
