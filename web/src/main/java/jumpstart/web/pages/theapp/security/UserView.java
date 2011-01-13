package jumpstart.web.pages.theapp.security;

import java.util.List;

import jumpstart.business.commons.exception.BusinessException;
import jumpstart.business.commons.exception.DoesNotExistException;
import jumpstart.business.domain.security.User;
import jumpstart.business.domain.security.UserRole;
import jumpstart.business.domain.security.User.PageStyle;
import jumpstart.business.domain.security.iface.ISecurityFinderServiceLocal;
import jumpstart.web.annotation.ProtectedPage;
import jumpstart.web.base.theapp.SimpleBasePage;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.apache.tapestry5.util.EnumValueEncoder;

@SuppressWarnings("unchecked")
@ProtectedPage
public class UserView extends SimpleBasePage {

	// The User

	private Long _userId;

	@SuppressWarnings("unused")
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
	private UserSearch _userSearch;

	@InjectPage
	private UserRoleView _viewPage;

	// Generally useful bits and pieces

	@Inject
	private PageRenderLinkSource _pageRenderLinkSource;

	@SuppressWarnings("unused")
	@Property
	private EnumValueEncoder _pageStyleEncoder = new EnumValueEncoder(User.PageStyle.class);

	public void set(Long userId) {
		_userId = userId;
	}

	Long onPassivate() {
		return _userId;
	}

	void onActivate(Long id) {
		_userId = id;
	}

	void setupRender() throws BusinessException {
		try {
			_user = getSecurityFinderService().findUser(_userId);
		}
		catch (DoesNotExistException e) {
			// Handle null user in the template
		}

		_userRoles = getSecurityFinderService().findUserRolesShallowishByUser(_userId);
	}

	void onRefresh() {
	}

	Link onCancel() {
		return _userSearch.createLinkWithLastSearch();
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

	public PageStyle getBoxy() {
		return User.PageStyle.BOXY;
	}

	public PageStyle getWide() {
		return User.PageStyle.WIDE;
	}
}
