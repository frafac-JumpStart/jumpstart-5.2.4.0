package jumpstart.web.pages.theapp.security;

import java.util.List;

import jumpstart.business.commons.exception.BusinessException;
import jumpstart.business.domain.security.User;
import jumpstart.business.domain.security.UserRole;
import jumpstart.business.domain.security.iface.ISecurityFinderServiceLocal;
import jumpstart.web.annotation.ProtectedPage;
import jumpstart.web.base.theapp.SimpleBasePage;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;

@ProtectedPage
public class MyAccountView extends SimpleBasePage {

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

	// Other pages
	
	@InjectPage
	private UserRoleView _viewPage;

	// Generally useful bits and pieces
	
	@Inject
	private PageRenderLinkSource _pageRenderLinkSource;

	void setupRender() throws BusinessException {
		Long userId = getVisit().getMyUserId();
		_user = getSecurityFinderService().findUser(userId);
		_userRoles = getSecurityFinderService().findUserRolesShallowishByUser(_user.getId());
	}

	Object onEdit() {
		return MyAccountEdit.class;
	}

	Object onChangePassword() {
		return MyPasswordChange.class;
	}

	Object onActionFromViewUserRole(Long id) {
		_viewPage.set(id, createLinkToThisPage());
		return _viewPage;
	}

	private Link createLinkToThisPage() {
		Link thisPageLink = _pageRenderLinkSource.createPageRenderLink(this.getClass());
		return thisPageLink;
	}

	private ISecurityFinderServiceLocal getSecurityFinderService() {
		return getBusinessServicesLocator().getSecurityFinderServiceLocal();
	}
}
