package jumpstart.web.pages.examples.tables;

import java.util.List;

import jumpstart.business.domain.examples.Person;
import jumpstart.business.domain.examples.iface.IPersonServiceLocal;
import jumpstart.client.IBusinessServicesLocator;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.ioc.annotations.Inject;

public class SortedGrid {
	private final int MAX_RESULTS = 30;

	// Screen fields

	@SuppressWarnings("unused")
	@Property
	private List<Person> _persons;

	// Generally useful bits and pieces

	@Inject
	private IBusinessServicesLocator _businessServicesLocator;

	@InjectComponent
	private Grid _grid;

	// The code
	
	void setupRender() {
		// Get all persons - ask business service to find them (from the database)
		_persons = getPersonService().findPersons(MAX_RESULTS);
		 
		if (_grid.getSortModel().getSortConstraints().isEmpty()) {
			_grid.getSortModel().updateSort("startDate");
		}
	}
	
	private IPersonServiceLocal getPersonService() {
		// Use our business services locator to get the EJB3 session bean called "PersonServiceLocal".
		return _businessServicesLocator.getPersonServiceLocal();
	}
}
