package jumpstart.web.pages.examples.input;

import jumpstart.business.domain.examples.Person;
import jumpstart.business.domain.examples.iface.IPersonServiceLocal;
import jumpstart.client.IBusinessServicesLocator;
import jumpstart.util.ExceptionUtil;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.BeanEditForm;
import org.apache.tapestry5.ioc.annotations.Inject;

public class Create1 {

	private final String _demoModeStr = System.getProperty("jumpstart.demo-mode");

	// Screen fields
	
	@Property
	private Person _person;

	// Other pages
	
	@InjectPage
	private Create2 _page2;
	
	// Generally useful bits and pieces
	
	@Component(id = "person")
	private BeanEditForm _form;

	@Inject
	private IBusinessServicesLocator _businessServicesLocator;

	// The code
	
	// Form triggers the PREPARE event during form render and form submission.

	void onPrepare() throws Exception {
		_person = new Person();
	}
	
	void onValidateForm() {
		if (_demoModeStr != null && _demoModeStr.equals("true")) {
			_form.recordError("Sorry, but this function is not allowed in Demo mode.");
			return;
		}
		try {
			getPersonService().createPerson(_person);
		}
		catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a user-friendly message.
			_form.recordError(ExceptionUtil.getRootCause(e));
		}
	}

	Object onSuccess() {
		_page2.set(_person.getId());
		return _page2;
	}

	Object onActionFromRefresh() {
		return this;
	}
	
	private IPersonServiceLocal getPersonService() {
		// Use our business services locator to get the EJB3 session bean called "PersonServiceLocal".
		return _businessServicesLocator.getPersonServiceLocal();
	}
}
