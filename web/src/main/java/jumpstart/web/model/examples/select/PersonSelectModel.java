package jumpstart.web.model.examples.select;

import java.util.ArrayList;
import java.util.List;

import jumpstart.business.domain.examples.Person;

import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.util.AbstractSelectModel;


public class PersonSelectModel extends AbstractSelectModel implements ValueEncoder<Person> {
	private List<Person> _persons;

	public PersonSelectModel(List<Person> persons) {
		_persons = persons;
	}
	
	public List<OptionGroupModel> getOptionGroups() {
		return null;
	}

	public List<OptionModel> getOptions() {
		List<OptionModel> options = new ArrayList<OptionModel>();
		for (Person person : _persons) {
			options.add(new OptionModelImpl(person.getFirstName() + " " + person.getLastName(), person));
		}
		return options;
	}
	
	public List<Person> getList() {
		return _persons;
	}

	// ValueEncoder methods
	
	public String toClient(Person obj) {
		Person p = (Person) obj;
		return p.getId().toString();
	}

	public Person toValue(String str) {
		for (Person p : _persons) {
			if (p.getId().toString().equals(str)) {
				return p;
			}
		}
		return null;
	}

}
