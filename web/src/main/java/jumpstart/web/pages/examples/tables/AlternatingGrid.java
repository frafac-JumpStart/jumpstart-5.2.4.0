package jumpstart.web.pages.examples.tables;

import java.util.List;

import jumpstart.business.domain.examples.Person;
import jumpstart.business.domain.examples.iface.IPersonServiceLocal;
import jumpstart.client.IBusinessServicesLocator;
import jumpstart.web.commons.EvenOdd;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class AlternatingGrid {
	private final int MAX_RESULTS = 30;

	// Screen fields

	@SuppressWarnings("unused")
	@Property
	private List<Person> _persons;

	@SuppressWarnings("unused")
	@Property
	private EvenOdd _evenOdd;

	// Generally useful bits and pieces

	@Inject
	private IBusinessServicesLocator _businessServicesLocator;

	// The code

	void setupRender() {
		// Get all persons - ask business service to find them (from the database)
		_persons = getPersonService().findPersons(MAX_RESULTS);
		_evenOdd = new EvenOdd();
	}

	private IPersonServiceLocal getPersonService() {
		// Use our business services locator to get the EJB3 session bean called "PersonServiceLocal".
		return _businessServicesLocator.getPersonServiceLocal();
	}
}
