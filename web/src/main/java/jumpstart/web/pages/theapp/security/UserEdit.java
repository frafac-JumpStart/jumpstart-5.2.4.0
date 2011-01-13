package jumpstart.web.pages.theapp.security;

import java.util.List;

import jumpstart.business.commons.exception.DoesNotExistException;
import jumpstart.business.domain.security.User;
import jumpstart.business.domain.security.UserRole;
import jumpstart.business.domain.security.User.PageStyle;
import jumpstart.business.domain.security.iface.ISecurityFinderServiceLocal;
import jumpstart.business.domain.security.iface.ISecurityManagerServiceLocal;
import jumpstart.web.annotation.ProtectedPage;
import jumpstart.web.base.theapp.SimpleBasePage;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.apache.tapestry5.util.EnumValueEncoder;

@SuppressWarnings("unchecked")
@ProtectedPage
public class UserEdit extends SimpleBasePage {

	// The User

	@Property
	private Long _userId;

	@Property
	private User _user;

	// The User's Roles

	@SuppressWarnings("unused")
	@Property
	private List<UserRole> _userRoles;

	@SuppressWarnings("unused")
	@Property
	private UserRole _userRole;

	@SuppressWarnings("unused")
	@Property
	private final UserRoleKeyEncoder _encoder = new UserRoleKeyEncoder();

	// Other pages

	@InjectPage
	private UserSearch _userSearch;

	@InjectPage
	private UserPasswordChange _userPasswordChange;

	@InjectPage
	private UserRoleAdd _addPage;

	@InjectPage
	private UserRoleView _viewPage;

	@InjectPage
	private UserRoleEdit _editPage;

	// Generally useful bits and pieces

	@Component(id = "form")
	private Form _form;

	@Inject
	private PageRenderLinkSource _pageRenderLinkSource;

	@SuppressWarnings("unused")
	@Property
	private EnumValueEncoder _pageStyleEncoder = new EnumValueEncoder(User.PageStyle.class);

	public void set(Long id) {
		_userId = id;
	}

	Long onPassivate() {
		return _userId;
	}

	void onActivate(Long id) {
		_userId = id;
	}

	void setupRender() {
		_userRoles = getSecurityFinderService().findUserRolesShallowishByUser(_userId);

		// Normally we'd find the user in onPrepare(), a method triggered during form render and submit. But this page's
		// template needs user earlier than form render, so we find user here and in onPrepareForSubmit() instead.

		_user = findUser(_userId);
	}

	void onPrepareForSubmit() {
		// Normally we'd find the user in onPrepare(), a method triggered during form render and submit. But this page's
		// template needs user earlier than form render, so we find user here and in onPrepareForSubmit() instead.

		_user = findUser(_userId);
	}

	private User findUser(Long userId) {
		try {
			return getSecurityFinderService().findUser(userId);
		}
		catch (DoesNotExistException e) {
			// Handle null user in the template
			return null;
		}
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
		getVisit().noteChanges(_user);
		return _userSearch.createLinkWithLastSearch();
	}

	Object onChangePassword() {
		_userPasswordChange.set(_userId);
		return _userPasswordChange;
	}

	void onRefresh() {
	}

	Object onCancel() {
		return _userSearch.createLinkWithLastSearch();
	}

	Object onAddUserRole() {
		_addPage.set(_userId, null, createLinkToThisPage());
		return _addPage;
	}

	Object onActionFromViewUserRole(Long id) {
		_viewPage.set(id, createLinkToThisPage());
		return _viewPage;
	}

	Object onActionFromEditUserRole(Long id) {
		_editPage.set(id, createLinkToThisPage());
		return _editPage;
	}

	void onActionFromRemoveUserRole(Long id, Integer version) {

		if (_form.isValid()) {

			// Delete the user from the database unless they've been modified elsewhere

			try {
				UserRole userRole = getSecurityFinderService().findUserRole(id);
				if (!userRole.getVersion().equals(version)) {
					_form
							.recordError("Cannot remove user role because has been updated or deleted since last displayed.  Please refresh and try again.");
					return;
				}
				getSecurityManagerService().removeUserRole(userRole);
			}
			catch (Exception e) {
				_form.recordError(interpretBusinessServicesExceptionForRemove(e));
				return;
			}
		}
	}

	private Link createLinkToThisPage() {
		Link thisPageLink = _pageRenderLinkSource.createPageRenderLinkWithContext(this.getClass(), onPassivate());
		return thisPageLink;
	}

	private class UserRoleKeyEncoder implements ValueEncoder<UserRole> {

		public String toClient(UserRole value) {
			return value.getId().toString();
		}

		public UserRole toValue(String keyAsString) {
			Long id = new Long(keyAsString);
			try {
				return getSecurityFinderService().findUserRole(id);
			}
			catch (DoesNotExistException e) {
				return null;
			}
		}
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
