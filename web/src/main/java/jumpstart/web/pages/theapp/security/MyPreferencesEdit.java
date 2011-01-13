package jumpstart.web.pages.theapp.security;

import jumpstart.business.commons.exception.DoesNotExistException;
import jumpstart.business.domain.security.User;
import jumpstart.business.domain.security.User.PageStyle;
import jumpstart.business.domain.security.iface.ISecurityFinderServiceLocal;
import jumpstart.business.domain.security.iface.ISecurityManagerServiceLocal;
import jumpstart.web.annotation.ProtectedPage;
import jumpstart.web.base.theapp.SimpleBasePage;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.util.EnumValueEncoder;

@ProtectedPage
public class MyPreferencesEdit extends SimpleBasePage {

	// The User

	@Property
	private User _user;
	
	@SuppressWarnings("unused")
	@Property
	@Persist(PersistenceConstants.FLASH)
	private String _message; 

	// Generally useful bits and pieces

	@Component(id = "form")
	private Form _form;

	@SuppressWarnings({ "unused", "unchecked" })
	@Property
	private EnumValueEncoder _pageStyleEncoder = new EnumValueEncoder(User.PageStyle.class);

	void onPrepare() throws DoesNotExistException {
		Long userId = getVisit().getMyUserId();
		_user = getSecurityFinderService().findUser(userId);
	}
	
	void onValidateForm() {
		try {
			getSecurityManagerService().changeUser(_user);
		}
		catch (Exception e) {
			_form.recordError(interpretBusinessServicesExceptionForChange(e));
		}
	}

	void onSuccess() {
		getVisit().noteChanges(_user);
		_message = getMessages().get("User_preferences_saved");
	}

	void onRefresh() {
	}

	private ISecurityFinderServiceLocal getSecurityFinderService() {
		return getBusinessServicesLocator().getSecurityFinderServiceLocal();
	}

	private ISecurityManagerServiceLocal getSecurityManagerService() {
		return getBusinessServicesLocator().getSecurityManagerServiceLocal();
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
