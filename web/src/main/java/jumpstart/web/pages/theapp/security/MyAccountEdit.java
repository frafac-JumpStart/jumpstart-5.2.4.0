package jumpstart.web.pages.theapp.security;

import java.util.List;

import jumpstart.business.commons.exception.DoesNotExistException;
import jumpstart.business.domain.security.User;
import jumpstart.business.domain.security.UserRole;
import jumpstart.business.domain.security.iface.ISecurityFinderServiceLocal;
import jumpstart.business.domain.security.iface.ISecurityManagerServiceLocal;
import jumpstart.web.annotation.ProtectedPage;
import jumpstart.web.base.theapp.SimpleBasePage;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;

@ProtectedPage
public class MyAccountEdit extends SimpleBasePage {

	// The User

	@Property
	private User _user;

	// The User's Roles

	@SuppressWarnings("unused")
	@Property
	private List<UserRole> _userRoles;

	@SuppressWarnings("unused")
	@Property
	private UserRole _userRole;

	// Generally useful bits and pieces

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

		try {
			getSecurityManagerService().changeUser(_user);
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

	public String[] getSalutations() {
		return User.SALUTATIONS;
	}
}
