package jumpstart.web.pages.theapp.security;

import java.util.List;

import jumpstart.business.domain.security.Role;
import jumpstart.business.domain.security.iface.ISecurityFinderServiceLocal;
import jumpstart.business.domain.security.iface.ISecurityManagerServiceLocal;
import jumpstart.web.annotation.ProtectedPage;
import jumpstart.web.base.theapp.SimpleBasePage;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;

@ProtectedPage
public class RoleSearch extends SimpleBasePage {

	// The result: a list of roles.

	@SuppressWarnings("unused")
	@Property
	private List<Role> _roles;

	@SuppressWarnings("unused")
	@Property
	private Role _role;

	// Generally useful bits and pieces

	@Component(id = "form")
	private Form _form;

	void setupRender() {
		_roles = getSecurityFinderService().findRolesShallowish();
	}

	Object onNew() {
		return RoleCreate.class;
	}

	void onActionFromDelete(Long id, Integer version) {

		if (_form.isValid()) {

			// Delete the user from the database unless they've been modified elsewhere

			try {
				Role role = getSecurityFinderService().findRole(id);
				if (!role.getVersion().equals(version)) {
					_form
							.recordError("Cannot delete role because has been updated or deleted since last displayed.  Please refresh and try again.");
				}
				else {
					getSecurityManagerService().deleteRole(role);
				}
			}
			catch (Exception e) {
				_form.recordError(interpretBusinessServicesExceptionForDelete(e));
			}
		}
	}

	private ISecurityFinderServiceLocal getSecurityFinderService() {
		return getBusinessServicesLocator().getSecurityFinderServiceLocal();
	}

	private ISecurityManagerServiceLocal getSecurityManagerService() {
		return getBusinessServicesLocator().getSecurityManagerServiceLocal();
	}

}
