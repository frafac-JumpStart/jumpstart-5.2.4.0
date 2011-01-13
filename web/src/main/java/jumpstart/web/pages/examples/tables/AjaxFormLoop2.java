package jumpstart.web.pages.examples.tables;

import java.text.DateFormat;
import java.text.Format;
import java.util.Collection;
import java.util.Locale;

import jumpstart.business.domain.examples.Person;
import jumpstart.business.domain.examples.Regions;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

public class AjaxFormLoop2 {

	// Screen fields
	
	@SuppressWarnings("unused")
	@Property
	@Persist(PersistenceConstants.FLASH)
	private Collection<Person> _persons;

	@Property
	private Person _person;

	// Generally useful bits and pieces

	@Inject
	private Messages _messages;

	@Inject
	private Locale _currentLocale;

	// The code
	
	public void set(Collection<Person> persons) {
		_persons = persons;
	}
	
	public String getPersonRegion() {
		// Follow the same naming convention that the Select component uses
		return _messages.get(Regions.class.getSimpleName() + "." + _person.getRegion().name());
	}

	public Format getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.MEDIUM, _currentLocale);
	}
}
