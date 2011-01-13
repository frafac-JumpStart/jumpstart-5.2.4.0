package jumpstart.web.pages.theapp.security;

import java.util.List;

import jumpstart.business.commons.exception.DoesNotExistException;
import jumpstart.business.domain.security.Role;
import jumpstart.business.domain.security.UserRole;
import jumpstart.business.domain.security.iface.ISecurityFinderServiceLocal;
import jumpstart.business.domain.security.iface.ISecurityManagerServiceLocal;
import jumpstart.web.annotation.ProtectedPage;
import jumpstart.web.base.theapp.SimpleBasePage;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;

@ProtectedPage
public class RoleEdit extends SimpleBasePage {

	// The Role
	
	@Property
	private Long _roleId;

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

	public void set(Long roleId) {
		_roleId = roleId;
	}
	
	Long onPassivate() {
		return _roleId;
	}

	void onActivate(Long roleId) {
		_roleId = roleId;
	}

	void setupRender() {
		_userRoles = getSecurityFinderService().findUserRolesShallowishByRole(_roleId);

		// Normally we'd find the role in onPrepare(), a method triggered during form render and submit. But this page's
		// template needs role earlier than form render, so we find role here and in onPrepareForSubmit() instead.

		_role = findRole(_roleId);
	}

	void onPrepareForSubmit() {
		// Normally we'd find the role in onPrepare(), a method triggered during form render and submit. But this page's
		// template needs role earlier than form render, so we find role here and in onPrepareForSubmit() instead.

		_role = findRole(_roleId);
	}

	private Role findRole(Long roleId) {
		try {
			return getSecurityFinderService().findRole(roleId);
		}
		catch (DoesNotExistException e) {
			// Handle null role in the template
			return null;
		}
	}

	void onValidateForm() {

		if (_form.getHasErrors()) {
			// We get here only if a validator detected an error and javascript is disabled in the browser.
			return;
		}

		try {
			getSecurityManagerService().changeRole(_role);
		}
		catch (Exception e) {
			_form.recordError(interpretBusinessServicesExceptionForChange(e));
		}
	}

	Object onSuccess() {
		return RoleSearch.class;
	}

	void onRefresh() {
	}

	Object onCancel() {
		return RoleSearch.class;
	}

	Object onAddUserRole() {
		_addPage.set(null, _roleId, createLinkToThisPage());
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

	private ISecurityFinderServiceLocal getSecurityFinderService() {
		return getBusinessServicesLocator().getSecurityFinderServiceLocal();
	}

	private ISecurityManagerServiceLocal getSecurityManagerService() {
		return getBusinessServicesLocator().getSecurityManagerServiceLocal();
	}
}
