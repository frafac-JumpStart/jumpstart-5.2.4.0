package jumpstart.web.pages.examples.navigation;

import jumpstart.business.domain.examples.Person;
import jumpstart.business.domain.examples.iface.IPersonServiceLocal;
import jumpstart.client.IBusinessServicesLocator;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class ActivationContext2 {

	// The activation context

	private Long _personId;
	
	// Screen fields
	
	@Property
	private Person _person;

	// Generally useful bits and pieces

	@Inject
	private IBusinessServicesLocator _businessServicesLocator;
	
	// The code
	
	// onActivate() is called by Tapestry to pass in the activation context from the URL.

	void onActivate(Long personId) {
		_personId = personId;
	}
	
	// setupRender() is called by tapestry at the start of rendering - it's good for things that are display only.

	void setupRender() throws Exception {
		// Get person - ask business service to find it (from the database)
		_person = getPersonService().findPerson(_personId);

		if (_person == null) {
			throw new Exception("Database data has not been set up!");
		}
	}

	private IPersonServiceLocal getPersonService() {
		return _businessServicesLocator.getPersonServiceLocal();
	}
}
