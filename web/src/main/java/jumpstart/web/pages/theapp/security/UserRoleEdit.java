package jumpstart.web.pages.theapp.security;

import jumpstart.business.commons.exception.DoesNotExistException;
import jumpstart.business.domain.security.UserRole;
import jumpstart.business.domain.security.iface.ISecurityFinderServiceLocal;
import jumpstart.business.domain.security.iface.ISecurityManagerServiceLocal;
import jumpstart.web.annotation.ProtectedPage;
import jumpstart.web.base.theapp.SimpleBasePage;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;

@ProtectedPage
public class UserRoleEdit extends SimpleBasePage {

	@Property
	private Long _userRoleId;
	
	@Persist
	private Link _returnTo;

	@Property
	private UserRole _userRole;

	@Component(id = "form")
	private Form _form;
	
	public void set(Long userRoleId, Link returnTo) {
		_userRoleId = userRoleId;
		_returnTo = returnTo;
	}
	
	Long onPassivate() {
		return _userRoleId;
	}

	void onActivate(Long userRoleId) {
		_userRoleId = userRoleId;
	}

	void onPrepare() throws DoesNotExistException {
		_userRole = getSecurityFinderService().findUserRoleShallowish(_userRoleId);
	}
	
	void onValidateForm() {
		try {
			getSecurityManagerService().changeUserRole(_userRole);
		}
		catch (Exception e) {
			_form.recordError(interpretBusinessServicesExceptionForChange(e));
		}
	}

	Link onSuccess() {
		return _returnTo;
	}

	void onRefresh() {
	}

	Link onCancel() {
		return _returnTo;
	}

	private ISecurityFinderServiceLocal getSecurityFinderService() {
		return getBusinessServicesLocator().getSecurityFinderServiceLocal();
	}

	private ISecurityManagerServiceLocal getSecurityManagerService() {
		return getBusinessServicesLocator().getSecurityManagerServiceLocal();
	}
}
