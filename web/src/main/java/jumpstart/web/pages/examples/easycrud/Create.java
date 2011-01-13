package jumpstart.web.pages.examples.easycrud;

import jumpstart.business.domain.examples.Person;
import jumpstart.business.domain.examples.iface.IPersonServiceLocal;
import jumpstart.client.IBusinessServicesLocator;
import jumpstart.util.ExceptionUtil;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.BeanEditForm;
import org.apache.tapestry5.ioc.annotations.Inject;

public class Create {

	private final String _demoModeStr = System.getProperty("jumpstart.demo-mode");

	// Screen fields
	
	@Property
	private Person _person;

	// Other pages
	
	@InjectPage
	private Index _indexPage;
	
	// Generally useful bits and pieces
	
	@Component(id = "personForm")
	private BeanEditForm _personForm;

	@Inject
	private IBusinessServicesLocator _businessServicesLocator;

	// The code
	
	// PersonForm triggers the PREPARE event when it is rendered or submitted

	void onPrepare() throws Exception {
		// Instantiate a Person for the form data to overlay.
		_person = new Person();
	}
	
	// PersonForm triggers the VALIDATE_FORM event when it is submitted

	void onValidateForm() {
		if (_demoModeStr != null && _demoModeStr.equals("true")) {
			_personForm.recordError("Sorry, but this function is not allowed in Demo mode.");
			return;
		}
		
		try {
			getPersonService().createPerson(_person);
		}
		catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a user-friendly message.
			_personForm.recordError(ExceptionUtil.getRootCause(e));
		}
	}

	// PersonForm triggers SUCCESS or FAILURE when it is submitted, depending on whether VALIDATE_FORM records an error

	Object onSuccess() {
		return _indexPage;
	}

	private IPersonServiceLocal getPersonService() {
		// Use our business services locator to get the EJB3 session bean called "PersonServiceLocal".
		return _businessServicesLocator.getPersonServiceLocal();
	}
}
