package jumpstart.web.pages.examples.easycrud;

import jumpstart.business.domain.examples.Person;
import jumpstart.business.domain.examples.iface.IPersonServiceLocal;
import jumpstart.client.IBusinessServicesLocator;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class Review {

	// The activation context

	@Property
	private Long _personId;

	// Screen fields

	@SuppressWarnings("unused")
	@Property
	private Person _person;

	// Generally useful bits and pieces

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
		_person = getPersonService().findPerson(_personId);
		// Handle null person in the template.
	}

	private IPersonServiceLocal getPersonService() {
		// Use our business services locator to get the EJB3 session bean called "PersonServiceLocal".
		return _businessServicesLocator.getPersonServiceLocal();
	}

}
