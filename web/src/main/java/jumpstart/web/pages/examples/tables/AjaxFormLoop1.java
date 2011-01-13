package jumpstart.web.pages.examples.tables;

import java.text.DateFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jumpstart.business.domain.examples.Person;
import jumpstart.business.domain.examples.iface.IPersonServiceLocal;
import jumpstart.client.IBusinessServicesLocator;
import jumpstart.util.ExceptionUtil;
import jumpstart.web.pages.Index;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

public class AjaxFormLoop1 {
	private final int MAX_RESULTS = 30;

	// Screen fields
	
	// We've used "session" persistence but it is risky. Better techniques include wrapping this field in a
	// Conversation (see the Wizard examples) or persisting it in the database.
	@Property
	@Persist
	private List<PersonHolder> _personHolders;

	@SuppressWarnings("unused")
	@Property
	private PersonHolder _personHolder;

	// Other pages
	
	@InjectPage
	private AjaxFormLoop2 _page2;

	// Generally useful bits and pieces
	
	@Component(id = "personsedit")
	private Form _form;

	@Inject
	private IBusinessServicesLocator _businessServicesLocator;

	@Inject
	private Locale _currentLocale;

	@Inject
	private ComponentResources _resources;

	// The code
	
	// Form triggers the PREPARE event during form render and form submission.

	void onPrepare() {
		if (_form.isValid()) {
			if (_personHolders == null) {
				// Get all persons - ask business service to find them (from the database)
				List<Person> persons = getPersonService().findPersons(MAX_RESULTS);

				_personHolders = new ArrayList<PersonHolder>();
				for (Person person : persons) {
					_personHolders.add(new PersonHolder(person, false, person.getId()));
				}
			}
		}
	}

	PersonHolder onAddRow() {
		// Create a skeleton Person and add it to the displayed list with a unique key
		Person newPerson = new Person();
		PersonHolder newPersonHolder = new PersonHolder(newPerson, true, 0 - System.nanoTime());
		_personHolders.add(newPersonHolder);

		return newPersonHolder;
	}

	void onRemoveRow(PersonHolder personPlus) {
		int index = _personHolders.indexOf(personPlus);
		PersonHolder holder = _personHolders.get(index);

		// If the person is new, remove them from the list. Else, flag them to be deleted from the database.
		if (holder.isNew()) {
			_personHolders.remove(personPlus);
		}
		else {
			holder.setDeleted(true);
		}
	}

	void onValidateForm() {

		if (_form.getHasErrors()) {
			// We get here only if a validator detected an error and javascript is disabled in the browser.
			return;
		}

		List<Person> personsToCreate = new ArrayList<Person>();
		List<Person> personsToChange = new ArrayList<Person>();
		List<Person> personsToDelete = new ArrayList<Person>();

		for (PersonHolder holder : _personHolders) {
			if (holder.isNew()) {
				personsToCreate.add(holder.getPerson());
			}
			else if (holder.isDeleted()) {
				personsToDelete.add(holder.getPerson());
			}
			else {
				personsToChange.add(holder.getPerson());
			}
		}

		System.out.println(">>> personsToCreate = " + personsToCreate);
		System.out.println(">>> personsToChange = " + personsToChange);
		System.out.println(">>> personsToDelete = " + personsToDelete);

		try {
			// In a real application you would persist them to the database instead of printing them
			// getPersonService().bulkEditPersons(personsToCreate, personsToChange, personsToDelete);
		}
		catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a user-friendly message.
			_form.recordError(ExceptionUtil.getRootCause(e));
		}
	}

	Object onSuccess() {
		List<Person> persons = new ArrayList<Person>();
		for (PersonHolder holder : _personHolders) {
			if (!holder.isDeleted()) {
				persons.add(holder.getPerson());
			}
		}
		_page2.set(persons);
		_resources.discardPersistentFieldChanges();
		return _page2;
	}

	void onRefresh() {
		_resources.discardPersistentFieldChanges();
		onPrepare();
	}

	Object onActionFromGoHome() {
		_resources.discardPersistentFieldChanges();
		return Index.class;
	}

	@SuppressWarnings("unchecked")
	public ValueEncoder getEncoder() {
		return new ValueEncoder<PersonHolder>() {

			public String toClient(PersonHolder value) {
				Long key = value.getKey();
				return key.toString();
			}

			public PersonHolder toValue(String keyAsString) {
				Long key = new Long(keyAsString);
				for (PersonHolder holder : _personHolders) {
					if (holder.getKey().equals(key)) {
						return holder;
					}
				}
				throw new IllegalArgumentException("Received key \"" + key
						+ "\" which has no counterpart in this collection: " + _personHolders);
			}
		};
	}

	public class PersonHolder {
		private Person _person;
		private Long _key;
		private boolean _new;
		private boolean _deleted;

		PersonHolder(Person person, boolean newPerson, Long key) {
			_person = person;
			_new = newPerson;
			_key = key;
		}

		public Person getPerson() {
			return _person;
		}

		public Long getKey() {
			return _key;
		}

		public boolean isNew() {
			return _new;
		}

		public boolean setDeleted(boolean deleted) {
			return _deleted = deleted;
		}

		public boolean isDeleted() {
			return _deleted;
		}
	}

	private IPersonServiceLocal getPersonService() {
		// Use our business services locator to get the EJB3 session bean called "PersonServiceLocal".
		return _businessServicesLocator.getPersonServiceLocal();
	}

	public Format getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.SHORT, _currentLocale);
	}

}
