package jumpstart.web.pages.examples.tables;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jumpstart.business.domain.examples.Person;
import jumpstart.business.domain.examples.iface.IPersonServiceLocal;
import jumpstart.client.IBusinessServicesLocator;
import jumpstart.util.ExceptionUtil;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Retain;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.services.BeanModelSource;

public class EditableGridUsingEncoder1 {
	private final int MAX_RESULTS = 30;

	// Screen fields

	@Property
	private List<Person> _persons;

	@SuppressWarnings("unused")
	@Property
	private Person _person;

	@Property
	private PersonPrimaryKeyEncoder _encoder;

	// We have to provide a model to avoid an exception during form submission
	// related to _persons being empty.
	@SuppressWarnings("unchecked")
	@Property
	@Retain
	private BeanModel _myModel;

	// Other pages
	
	@InjectPage
	private EditableGridUsingEncoder2 _page2;

	// Generally useful bits and pieces

	@Component(id = "personsedit")
	private Form _form;

	@Inject
	private IBusinessServicesLocator _businessServicesLocator;

	@Inject
	private BeanModelSource _beanModelSource;

	@Inject
	private ComponentResources _componentResources;

	// The code
	
	void setupRender() {
		_persons = getPersonService().findPersons(MAX_RESULTS);

		if (_myModel == null) {
			_myModel = _beanModelSource.createDisplayModel(Person.class,
					_componentResources.getMessages());
		}
	}

	// Form triggers the PREPARE event during form render and form submission.
	
	void onPrepare() {
		// Get all persons - ask business service to find them (from the
		// database)
		_encoder = new PersonPrimaryKeyEncoder();
	}

	void onValidateForm() {

		if (_form.getHasErrors()) {
			// We get here only if a validator detected an error and javascript is disabled in the browser.
			return;
		}

		_persons = _encoder.getAllValues();

		try {
			System.out.println(">>> persons = " + _persons);
			// In a real application you would persist them to the database
			// instead of printing them
			// getPersonService().changePersons(_persons);
		} catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a
			// user-friendly message.
			_form.recordError(ExceptionUtil.getRootCause(e));
		}
	}

	Object onSuccess() {
		_page2.set(_persons);
		return _page2;
	}

	private class PersonPrimaryKeyEncoder implements ValueEncoder<Person> {
		private final Map<Long, Person> keyToValue = new LinkedHashMap<Long, Person>();

		public String toClient(Person value) {
			return value.getId().toString();
		}

		public Person toValue(String keyAsString) {
			Long key = new Long(keyAsString);
			Person person = getPersonService().findPerson(key);
			keyToValue.put(key, person);
			return person;
		}

		public final List<Person> getAllValues() {
			List<Person> result = CollectionFactory.newList();

			for (Map.Entry<Long, Person> entry : keyToValue.entrySet()) {
				result.add(entry.getValue());
			}

			return result;
		}
	};

	private IPersonServiceLocal getPersonService() {
		// Use our business services locator to get the EJB3 session bean called
		// "PersonServiceLocal".
		return _businessServicesLocator.getPersonServiceLocal();
	}
}
