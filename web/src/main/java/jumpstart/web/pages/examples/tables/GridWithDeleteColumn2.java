package jumpstart.web.pages.examples.tables;

import java.util.List;

import jumpstart.business.domain.examples.Person;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

public class GridWithDeleteColumn2 {

	// Screen fields
	
	@SuppressWarnings("unused")
	@Property
	@Persist(PersistenceConstants.FLASH)
	private List<Person> _persons;

	// The code
	
	public void set(List<Person> persons) {
		_persons = persons;
	}
}
