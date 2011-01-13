package jumpstart.web.pages.examples.tables;

import java.text.DateFormat;
import java.text.Format;
import java.util.List;
import java.util.Locale;

import jumpstart.business.domain.examples.Person;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class LoopWithDeleteColumn2 {

	// Screen fields

	@SuppressWarnings("unused")
	@Property
	@Persist(PersistenceConstants.FLASH)
	private List<Person> _persons;

	@SuppressWarnings("unused")
	@Property
	private Person _person;

	// Generally useful bits and pieces

	@Inject
	private Locale _currentLocale;
	
	// The code

	public void set(List<Person> persons) {
		_persons = persons;
	}

	public Format getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.MEDIUM, _currentLocale);
	}
}
