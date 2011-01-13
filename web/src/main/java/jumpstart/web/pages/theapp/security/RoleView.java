package jumpstart.web.pages.theapp.security;

import java.util.List;

import jumpstart.business.commons.exception.BusinessException;
import jumpstart.business.commons.exception.DoesNotExistException;
import jumpstart.business.domain.security.Role;
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
public class RoleView extends SimpleBasePage {

	// The Role

	private Long _roleId;

	@SuppressWarnings("unused")
	@Property
	private Role _role;

	// The Role's Users

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

	public void set(Long roleId) {
		_roleId = roleId;
	}

	Long onPassivate() {
		return _roleId;
	}

	void onActivate(Long roleId) throws DoesNotExistException {
		_roleId = roleId;
	}

	void setupRender() throws BusinessException {
		try {
			_role = getSecurityFinderService().findRole(_roleId);
		}
		catch (DoesNotExistException e) {
			// Handle null role in the template
		}

		_userRoles = getSecurityFinderService().findUserRolesShallowishByRole(_roleId);
	}

	void onRefresh() {
	}

	Object onCancel() {
		return RoleSearch.class;
	}

	Object onActionFromViewUserRole(Long id) {
		_viewPage.set(id, createLinkToThisPage());
		return _viewPage;
	}

	private Link createLinkToThisPage() {
		Link thisPageLink = _pageRenderLinkSource.createPageRenderLinkWithContext(this.getClass(), onPassivate());
		return thisPageLink;
	}

	private ISecurityFinderServiceLocal getSecurityFinderService() {
		return getBusinessServicesLocator().getSecurityFinderServiceLocal();
	}
}
