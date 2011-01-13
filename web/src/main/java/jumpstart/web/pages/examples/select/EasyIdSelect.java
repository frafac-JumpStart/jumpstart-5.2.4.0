package jumpstart.web.pages.examples.select;

import java.util.List;

import jumpstart.business.domain.examples.Person;
import jumpstart.business.domain.examples.iface.IPersonServiceLocal;
import jumpstart.client.IBusinessServicesLocator;
import jumpstart.web.commons.IdSelectModel;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.PropertyAccess;

public class EasyIdSelect {
	private final int MAX_RESULTS = 30;

	// The activation context

	@Property
	private Long _personId;

	// Screen fields

	@SuppressWarnings("unused")
	@Property
	private IdSelectModel<Person> _personIds;

	// Generally useful bits and pieces

	@Inject
	private IBusinessServicesLocator _businessServicesLocator;

	@Inject
	private PropertyAccess _propertyAccess;

	// The code
	
	Long onPassivate() {
		return _personId;
	}

	void onActivate(Long personId) {
		_personId = personId;
	}

	// Form triggers the PREPARE event during form render and form submission.

	void onPrepare() {
		// Get all persons - ask business service to find them (from the database)
		List<Person> persons = getPersonService().findPersons(MAX_RESULTS);
		_personIds = new IdSelectModel<Person>(persons, Person.class, "firstName", "id", _propertyAccess);
	}

	private IPersonServiceLocal getPersonService() {
		// Use our business services locator to get the EJB3 session bean called "PersonServiceLocal".
		return _businessServicesLocator.getPersonServiceLocal();
	}

}
