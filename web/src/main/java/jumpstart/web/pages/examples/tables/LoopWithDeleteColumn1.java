package jumpstart.web.pages.examples.tables;

import java.text.DateFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jumpstart.business.domain.examples.Person;
import jumpstart.business.domain.examples.iface.IPersonServiceLocal;
import jumpstart.client.IBusinessServicesLocator;
import jumpstart.web.pages.Index;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class LoopWithDeleteColumn1 {
	private final int MAX_RESULTS = 30;

	// Screen fields

	@Property
	// Oddly, in this example we have to persist the list, else on submit we get java.util.NoSuchElementException. But
	// persisting a field in the session is not ideal because the field will be
	// overwritten if user opens new window with same session and visits the same page.
	@Persist
	private List<Person> _persons;

	@Property
	private Person _person;

	// Work fields

	private boolean _delete;

	private List<Person> _personsToDelete;

	// Other pages

	@InjectPage
	private LoopWithDeleteColumn2 _page2;

	// Generally useful bits and pieces

	@Inject
	private IBusinessServicesLocator _businessServicesLocator;

	@Inject
	private ComponentResources _resources;

	@Inject
	private Locale _currentLocale;

	// The code

	void setupRender() {
		// Get all persons - ask business service to find them (from the database)
		_persons = getPersonService().findPersons(MAX_RESULTS);
	}

	public void onPrepareForSubmit() {
		if (_persons == null) {
			_persons = new ArrayList<Person>();
		}
	}

	Object onSuccess() {
		_page2.set(getPersonsToDelete());
		_resources.discardPersistentFieldChanges();
		return _page2;
	}

	Object onActionFromGoHome() {
		_resources.discardPersistentFieldChanges();
		return Index.class;
	}

	private IPersonServiceLocal getPersonService() {
		// Use our business services locator to get the EJB3 session bean called "PersonServiceLocal".
		return _businessServicesLocator.getPersonServiceLocal();
	}

	public List<Person> getPersonsToDelete() {
		if (_personsToDelete == null) {
			_personsToDelete = new ArrayList<Person>();
		}
		return _personsToDelete;
	}

	// The Loop component will automatically call this for every row as it is rendered.
	public boolean isDelete() {
		return _delete;
	}

	// The Loop component will automatically call this for every row on submit.
	public void setDelete(boolean delete) {
		if (delete) {
			getPersonsToDelete().add(_person);
		}
	}

	public Format getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.MEDIUM, _currentLocale);
	}
}
