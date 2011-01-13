package jumpstart.web.components.examples.componentscrud;

import java.util.List;

import jumpstart.business.domain.examples.Person;
import jumpstart.business.domain.examples.iface.IPersonServiceLocal;
import jumpstart.client.IBusinessServicesLocator;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * This component will trigger the following events on its container (which in this example is the page):
 * {@link jumpstart.web.components.examples.componentscrud.PersonList#SELECTED}(Long personId).
 */
// @Events is applied to a component solely to document what events it may trigger. It is not checked at runtime.
@Events( { PersonList.SELECTED })
public class PersonList {
	public static final String SELECTED = "selected";

	private final int MAX_RESULTS = 30;

	// Parameters

	@Parameter
	@Property
	private String _partialName;

	@Parameter
	@Property
	private Long _highlightPersonId;

	// Screen fields

	@SuppressWarnings("unused")
	@Property
	private List<Person> _persons;

	@Property
	private Person _person;

	@SuppressWarnings("unused")
	@Component(id = "list")
	@Property
	private Grid _list;

	// Generally useful bits and pieces

	@Inject
	private IBusinessServicesLocator _businessServicesLocator;

	// The code

	// setupRender() is called by Tapestry right before it starts rendering the component.

	void setupRender() {
		_persons = getPersonService().findPersons(_partialName, MAX_RESULTS);
	}

	boolean onSelected(Long personId) {
		// Return false, which means we haven't handled the event so bubble it up.
		// This method is here solely as documentation, because without this method the event would bubble up anyway.
		return false;
	}

	// Getters

	public String getLinkCSSClass() {
		if (_person != null && _person.getId().equals(_highlightPersonId)) {
			return "active";
		}
		else {
			return "";
		}
	}

	private IPersonServiceLocal getPersonService() {
		// Use our business services locator to get the EJB3 session bean called "PersonServiceLocal".
		return _businessServicesLocator.getPersonServiceLocal();
	}
}
