package jumpstart.web.pages.examples.output;

import java.text.Format;
import java.text.SimpleDateFormat;

import jumpstart.business.domain.examples.Person;
import jumpstart.business.domain.examples.Regions;
import jumpstart.business.domain.examples.iface.IPersonServiceLocal;
import jumpstart.client.IBusinessServicesLocator;

import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

public class TotalControlOutput {

	private Person _person;
	
	@Inject
	private IBusinessServicesLocator _businessServicesLocator;

	@Inject
	private Messages _messages;
	
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
	
	public String getPersonRegion() {
		// Follow the same naming convention that the Select component uses
		return _messages.get(Regions.class.getSimpleName() + "." + _person.getRegion().name());
	}

	public Format getStartDateFormat() {
		final Format f = new SimpleDateFormat("dd MMMM yyyy G");
		return f;
	}
}
