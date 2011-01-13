package jumpstart.web.pages.theapp.security;

import java.util.List;

import jumpstart.business.commons.exception.DoesNotExistException;
import jumpstart.business.domain.security.Role;
import jumpstart.business.domain.security.User;
import jumpstart.business.domain.security.UserRole;
import jumpstart.business.domain.security.iface.ISecurityFinderServiceLocal;
import jumpstart.business.domain.security.iface.ISecurityManagerServiceLocal;
import jumpstart.web.annotation.ProtectedPage;
import jumpstart.web.base.theapp.SimpleBasePage;
import jumpstart.web.commons.IdSelectModel;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.PropertyAccess;

@ProtectedPage
public class UserRoleAdd extends SimpleBasePage {

	// The activation context

	private Long _userId;

	private Long _roleId;

	// A link back to the caller - there are many possible callers so each caller must provide this.

	@Persist
	private Link _returnTo;

	// The User and/or Role specified by the caller

	@SuppressWarnings("unused")
	@Property
	private User _user;

	@SuppressWarnings("unused")
	@Property
	private Role _role;

	// If the User and/or Role were not specified by the caller then we'll need them in lists for selection.

	@SuppressWarnings("unused")
	@Property
	private IdSelectModel<User> _userIds;

	@Property
	private Long _selectedUserId;

	@SuppressWarnings("unused")
	@Property
	private IdSelectModel<Role> _roleIds;

	@Property
	private Long _selectedRoleId;

	// The UserRole we're trying to add

	@Property
	private UserRole _userRole;

	// Useful bits and pieces

	@Component(id = "form")
	private Form _form;

	@Inject
	private PropertyAccess _propertyAccess;

	public void set(Long userId, Long roleId, Link returnTo) {
		_userId = userId;
		_roleId = roleId;
		_returnTo = returnTo;
	}

	Long[] onPassivate() {
		return new Long[] { _userId, _roleId };
	}

	void onActivate(Long userId, Long roleId) throws DoesNotExistException {
		_userId = userId;
		_roleId = roleId;
	}

	void setupRender() throws DoesNotExistException {

		// If user id not given, put a selection list of users on the page

		if (_userId == null) {
			List<User> users = getSecurityFinderService().findUsersShallowish();
			_userIds = new IdSelectModel<User>(users, User.class, "loginId", "id", _propertyAccess);
		}

		// If role id not given, put a selection list of roles on the page

		if (_roleId == null) {
			List<Role> roles = getSecurityFinderService().findRolesShallowish();
			_roleIds = new IdSelectModel<Role>(roles, Role.class, "name", "id", _propertyAccess);
		}
	}

	void onPrepare() throws DoesNotExistException {

		// Instantiate a UserRole for the form data to overlay.

		_userRole = new UserRole();
		_userRole.setActive(true);

		// If user and/or role specified then get them.

		if (_userId != null) {
			_user = getSecurityFinderService().findUser(_userId);
		}

		if (_roleId != null) {
			_role = getSecurityFinderService().findRole(_roleId);
		}
	}

	void onValidateForm() {

		if (_form.getHasErrors()) {
			// We get here only if a validator detected an error and javascript is disabled in the browser.
			return;
		}

		try {
			Long userId = _userId != null ? _userId : _selectedUserId;
			Long roleId = _roleId != null ? _roleId : _selectedRoleId;

			getSecurityManagerService().addUserRole(userId, roleId, _userRole);
		}
		catch (Exception e) {
			_form.recordError(interpretBusinessServicesExceptionForCreate(e));
			return;
		}
	}

	Object onSuccess() {
		return _returnTo;
	}

	void onReset() {
	}

	Object onCancel() {
		return _returnTo;
	}

	private ISecurityFinderServiceLocal getSecurityFinderService() {
		return getBusinessServicesLocator().getSecurityFinderServiceLocal();
	}

	private ISecurityManagerServiceLocal getSecurityManagerService() {
		return getBusinessServicesLocator().getSecurityManagerServiceLocal();
	}
}
