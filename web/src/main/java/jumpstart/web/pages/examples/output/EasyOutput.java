package jumpstart.web.pages.examples.output;

import jumpstart.business.domain.examples.Person;
import jumpstart.business.domain.examples.iface.IPersonServiceLocal;
import jumpstart.client.IBusinessServicesLocator;

import org.apache.tapestry5.ioc.annotations.Inject;

public class EasyOutput {

	private Person _person;
	
	@Inject
	private IBusinessServicesLocator _businessServicesLocator;
	
	void setupRender() {
		Long personId = 1L;
		// Get person - ask business service to find it (from the database)
		_person = getPersonService().findPerson(personId);
		
		if (_person == null) {
			throw new IllegalStateException("Database data has not been set up!");
		}
	}
	
	private IPersonServiceLocal getPersonService() {
		// Use our business services locator to get the EJB3 session bean called "PersonServiceLocal".
		return _businessServicesLocator.getPersonServiceLocal();
	}
	
	public Person getPerson() {
		return _person;
	}
}
