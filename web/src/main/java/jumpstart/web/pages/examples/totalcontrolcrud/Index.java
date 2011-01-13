package jumpstart.web.pages.examples.totalcontrolcrud;

import java.text.DateFormat;
import java.text.Format;
import java.util.List;
import java.util.Locale;

import jumpstart.business.domain.examples.Person;
import jumpstart.business.domain.examples.Regions;
import jumpstart.business.domain.examples.iface.IPersonServiceLocal;
import jumpstart.client.IBusinessServicesLocator;
import jumpstart.util.ExceptionUtil;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

public class Index {

	private final String _demoModeStr = System.getProperty("jumpstart.demo-mode");
	private final int MAX_RESULTS = 30;

	// Screen fields

	@SuppressWarnings("unused")
	@Property
	private List<Person> _persons;

	@Property
	private Person _person;

	@SuppressWarnings("unused")
	@Property
	@Persist(PersistenceConstants.FLASH)
	private String _errorMessage;

	// Generally useful bits and pieces

	@Inject
	private IBusinessServicesLocator _businessServicesLocator;

	@Inject
	private Messages _messages;
	
	@Inject
	private Locale _currentLocale;
	
	// The code

	// setupRender() is called by Tapestry right before it starts rendering the page.

	void setupRender() {
		_persons = getPersonService().findPersons(MAX_RESULTS);
	}
	
	// Handle event "delete"

	void onDelete(Long id, Integer version) {
		if (_demoModeStr != null && _demoModeStr.equals("true")) {
			_errorMessage = "Sorry, but this function is not allowed in Demo mode.";
			return;
		}

		// Delete the person from the database unless they've been modified elsewhere

		try {
			Person person = getPersonService().findPerson(id);
			if (!person.getVersion().equals(version)) {
				_errorMessage = "Cannot delete person because has been updated or deleted since last displayed. Please refresh and try again.";
			}
			else {
				getPersonService().deletePerson(person);
			}
		}
		catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a user-friendly message.
			_errorMessage = ExceptionUtil.getRootCause(e);
		}
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
