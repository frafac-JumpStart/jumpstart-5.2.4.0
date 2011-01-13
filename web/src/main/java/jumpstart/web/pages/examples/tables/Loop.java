package jumpstart.web.pages.examples.tables;

import java.text.DateFormat;
import java.text.Format;
import java.util.List;
import java.util.Locale;

import jumpstart.business.domain.examples.Person;
import jumpstart.business.domain.examples.Regions;
import jumpstart.business.domain.examples.iface.IPersonServiceLocal;
import jumpstart.client.IBusinessServicesLocator;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

public class Loop {
	private final int MAX_RESULTS = 30;

	// Screen fields

	@SuppressWarnings("unused")
	@Property
	private List<Person> _persons;

	@Property
	private Person _person;

	// Generally useful bits and pieces

	@Inject
	private IBusinessServicesLocator _businessServicesLocator;

	@Inject
	private Messages _messages;

	@Inject
	private Locale _currentLocale;
	
	// The code

	void setupRender() {
		// Get all persons - ask business service to find them (from the database)
		_persons = getPersonService().findPersons(MAX_RESULTS);
	}

	private IPersonServiceLocal getPersonService() {
		// Use our business services locator to get the EJB3 session bean called "PersonServiceLocal".
		return _businessServicesLocator.getPersonServiceLocal();
	}
	
	public String getPersonRegion() {
		// Follow the same naming convention that the Select component uses
		return _messages.get(Regions.class.getSimpleName() + "." + _person.getRegion().name());
	}

	public Format getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.MEDIUM, _currentLocale);
	}
}
