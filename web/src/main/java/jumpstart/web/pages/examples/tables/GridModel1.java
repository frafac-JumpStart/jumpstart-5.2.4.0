package jumpstart.web.pages.examples.tables;

import java.util.List;

import jumpstart.business.domain.examples.Person;
import jumpstart.business.domain.examples.iface.IPersonServiceLocal;
import jumpstart.client.IBusinessServicesLocator;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Retain;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;

public class GridModel1 {
	private final int MAX_RESULTS = 30;

	// Screen fields

	@SuppressWarnings("unused")
	@Property
	private List<Person> _persons;

	@SuppressWarnings("unused")
	@Property
	private Person _person;

	@SuppressWarnings("unchecked")
	@Property
	@Retain
	private BeanModel _myModel;

	// Generally useful bits and pieces

	@Inject
	private IBusinessServicesLocator _businessServicesLocator;

	@Inject
	private BeanModelSource _beanModelSource;

	@Inject
	private ComponentResources _componentResources;

	// The code
	
	void setupRender() {

		if (_myModel == null) {
			_myModel = _beanModelSource.createDisplayModel(Person.class, _componentResources.getMessages());
			_myModel.add("action", null);
			_myModel.include("id", "firstName", "lastName", "startDate", "action");
			_myModel.get("firstName").sortable(false);
			_myModel.get("lastName").label("Surname");
		}

		// Get all persons - ask business service to find them (from the database)
		_persons = getPersonService().findPersons(MAX_RESULTS);
	}

	private IPersonServiceLocal getPersonService() {
		// Use our business services locator to get the EJB3 session bean called "PersonServiceLocal".
		return _businessServicesLocator.getPersonServiceLocal();
	}
}
