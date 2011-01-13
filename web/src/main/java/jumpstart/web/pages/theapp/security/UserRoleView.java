package jumpstart.web.pages.theapp.security;

import jumpstart.business.commons.exception.DoesNotExistException;
import jumpstart.business.domain.security.UserRole;
import jumpstart.business.domain.security.iface.ISecurityFinderServiceLocal;
import jumpstart.web.annotation.ProtectedPage;
import jumpstart.web.base.theapp.SimpleBasePage;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

@ProtectedPage
public class UserRoleView extends SimpleBasePage {

	@Property
	private Long _userRoleId;
	
	@Persist
	private Link _returnTo;

	@SuppressWarnings("unused")
	@Property
	private UserRole _userRole;
	
	public void set(Long id, Link returnTo) {
		_userRoleId = id;
		_returnTo = returnTo;
	}
	
	Long onPassivate() {
		return _userRoleId;
	}

	void onActivate(Long userRoleId) {
		_userRoleId = userRoleId;
	}

	void setupRender() throws DoesNotExistException {
		_userRole = getSecurityFinderService().findUserRoleShallowish(_userRoleId);
	}
	
	void cleanupRender() {
		_userRole = null;
	}

	Link onCancel() {
		return _returnTo;
	}

	private ISecurityFinderServiceLocal getSecurityFinderService() {
		return getBusinessServicesLocator().getSecurityFinderServiceLocal();
	}
}
