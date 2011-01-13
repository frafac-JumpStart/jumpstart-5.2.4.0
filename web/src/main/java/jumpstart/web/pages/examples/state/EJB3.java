package jumpstart.web.pages.examples.state;

import jumpstart.business.domain.examples.Person;
import jumpstart.business.domain.examples.iface.IPersonServiceLocal;
import jumpstart.client.IBusinessServicesLocator;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class EJB3 {

	// Screen fields

	@Property
	private Person _person;

	// Generally useful bits and pieces

	@Inject
	private IBusinessServicesLocator _businessServicesLocator;

	// The code
	
	void setupRender() throws Exception {
		_person = getPersonService().findPerson(1L);

		if (_person == null) {
			throw new IllegalStateException("Database data has not been set up!");
		}
	}

	private IPersonServiceLocal getPersonService() {
		// Use our business services locator to get an EJB3 session bean of type "IPersonServiceLocal".
		return _businessServicesLocator.getPersonServiceLocal();
	}
}
