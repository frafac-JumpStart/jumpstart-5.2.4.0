package jumpstart.web.pages.examples.select;

import java.util.List;

import jumpstart.business.domain.examples.Person;
import jumpstart.business.domain.examples.iface.IPersonServiceLocal;
import jumpstart.client.IBusinessServicesLocator;
import jumpstart.web.model.examples.select.PersonSelectModel;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class TotalControlObjectSelect {
	private final int MAX_RESULTS = 30;

	// The activation context

	private Long _personId;

	// Screen fields

	@Property
	private PersonSelectModel _persons;

	@Property
	private Person _person;

	// Generally useful bits and pieces

	@Inject
	private IBusinessServicesLocator _businessServicesLocator;

	// The code
	
	Long onPassivate() {
		return _person == null ? null : _person.getId();
	}

	void onActivate(EventContext context) {
		if (context.getCount() > 0) {
			_personId = context.get(Long.class, 0);
		}
	}

	// Form triggers the PREPARE event during form render and form submission.

	void onPrepare() {
		// Get all persons - ask business service to find them (from the database)
		List<Person> persons = getPersonService().findPersons(MAX_RESULTS);
		_persons = new PersonSelectModel(persons);

		_person = findPersonInModel(_personId);
	}

	void onValidateForm() {
		_personId = _person == null ? null : _person.getId();
	}

	Person findPersonInModel(Long personId) {
		for (Person person : _persons.getList()) {
			if (person.getId().equals(personId)) {
				return person;
			}
		}
		return null;
	}

	private IPersonServiceLocal getPersonService() {
		// Use our business services locator to get the EJB3 session bean called "PersonServiceLocal".
		return _businessServicesLocator.getPersonServiceLocal();
	}

}
