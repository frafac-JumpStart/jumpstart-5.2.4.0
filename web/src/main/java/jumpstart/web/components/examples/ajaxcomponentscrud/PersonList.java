package jumpstart.web.components.examples.ajaxcomponentscrud;

import java.util.List;

import jumpstart.business.domain.examples.Person;
import jumpstart.business.domain.examples.iface.IPersonServiceLocal;
import jumpstart.client.IBusinessServicesLocator;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentEventCallback;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * This component will trigger the following events on its container (which in this example is the page):
 * {@link jumpstart.web.components.examples.ajaxcomponentscrud.PersonList#SELECTED}(Long personId).
 */
// @Events is applied to a component solely to document what events it may trigger. It is not checked at runtime.
@Events( { PersonList.SELECTED })
public class PersonList {
	public static final String SELECTED = "selected";

	private final int MAX_RESULTS = 40;

	// Parameters

	@Parameter
	@Property
	private Long _highlightPersonId;

	@SuppressWarnings("unused")
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	@Property
	private String _zone;

	// Screen fields

	@Property
	@Persist
	private String _partialName;

	@SuppressWarnings("unused")
	@Property
	// Persist the list to support inplace="true" on the Grid
	@Persist
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

	@Inject
	private ComponentResources _componentResources;

	private Object _zonesToUpdate;

	// The code

	// setupRender() is called by Tapestry right before it starts rendering the component.

	void setupRender() {
		_persons = getPersonService().findPersons(_partialName, MAX_RESULTS);
		if (_highlightPersonId == null) {
			// Null is a nuisance because it fails to match the signature of our onSuccess method.
			_highlightPersonId = -1L;
		}
	}

	Object onSuccessFromAjaxFilterForm(Long highlightPersonId) {
		// Trigger new event "filter" which will bubble up.
		// When the event has been handled, Tapestry will call my callback with the result.
		_componentResources.triggerEvent("filter", new Object[] { highlightPersonId }, getMyCallback());
		// _zonesToUpdate will have been updated by my callback.
		return _zonesToUpdate;
	}

	// Handle event "selected"

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

	// When you trigger an event you can also specify a callback. When the event has been handled Tapestry will call the
	// callback with the result. This component expects the event handlers will return the zone or zones to update.

	private ComponentEventCallback<Object> getMyCallback() {

		ComponentEventCallback<Object> callback = new ComponentEventCallback<Object>() {
			public boolean handleResult(Object result) {
				_zonesToUpdate = result;
				return false;
			}
		};

		return callback;
	}
}
