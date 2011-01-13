package jumpstart.web.pages.examples.infrastructure;

import jumpstart.business.domain.examples.Person;
import jumpstart.business.domain.examples.iface.IPersonServiceLocal;
import jumpstart.client.IBusinessServicesLocator;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class HandlingABadContext {

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

	Long onPassivate() {
		return _personId;
	}

	void onActivate(Long personId) {
		_personId = personId;
	}

	void setupRender() {
		// Get person - ask business service to find it (from the database)
		_person = getPersonService().findPerson(_personId);
		// Handle null person in the template (with an If component).
	}

	private IPersonServiceLocal getPersonService() {
		// Use our business services locator to get the EJB3 session bean called "PersonServiceLocal".
		return _businessServicesLocator.getPersonServiceLocal();
	}
}
