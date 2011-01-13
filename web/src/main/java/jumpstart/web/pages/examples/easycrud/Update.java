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

public class Update {

	// The activation context

	@Property
	private Long _personId;

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

	// onPassivate() is called by Tapestry to get the activation context to put in the URL.

	Long onPassivate() {
		return _personId;
	}

	// onActivate() is called by Tapestry to pass in the activation context from the URL.

	void onActivate(Long personId) {
		_personId = personId;
	}

	// setupRender() is called by Tapestry right before it starts rendering the page.

	void setupRender() {

		// We wouldn't need the following code if, in the template, person wasn't used outside the form.

		if (_person == null) {
			_person = getPersonService().findPerson(_personId);
			// Handle null person in the template.
		}
	}

	// PersonForm triggers the PREPARE event when it is rendered or submitted

	void onPrepare() {
		if (_person == null) {
			// Get objects for the form fields to overlay.
			_person = getPersonService().findPerson(_personId);
			// Handle null person in the template.
		}
	}

	// PersonForm triggers the VALIDATE_FORM event when it is submitted

	void onValidateForm() {
		try {
			getPersonService().changePerson(_person);
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
