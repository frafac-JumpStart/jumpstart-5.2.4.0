package jumpstart.web.pages.theapp.security;

import jumpstart.business.domain.security.Role;
import jumpstart.business.domain.security.iface.ISecurityManagerServiceLocal;
import jumpstart.web.annotation.ProtectedPage;
import jumpstart.web.base.theapp.SimpleBasePage;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;

@ProtectedPage
public class RoleCreate extends SimpleBasePage {

	@Property
	private Role _role;

	@Component(id = "form")
	private Form _form;
	
	void onPrepare() {
		// Instantiate a Role for the form data to overlay.
		_role = new Role();
	}

	void onValidateForm() {

		if (_form.getHasErrors()) {
			// We get here only if a validator detected an error and javascript is disabled in the browser.
			return;
		}

		try {
			getSecurityManagerService().createRole(_role);
		}
		catch (Exception e) {
			_form.recordError(interpretBusinessServicesExceptionForCreate(e));
			return;
		}
	}

	Object onSuccess() {
		return RoleSearch.class;
	}
	
	void onReset() {
	}

	Object onCancel() {
		return RoleSearch.class;
	}

	private ISecurityManagerServiceLocal getSecurityManagerService() {
		return getBusinessServicesLocator().getSecurityManagerServiceLocal();
	}
}
