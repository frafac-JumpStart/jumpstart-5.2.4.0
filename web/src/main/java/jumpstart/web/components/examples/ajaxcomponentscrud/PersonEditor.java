package jumpstart.web.components.examples.ajaxcomponentscrud;

import java.text.Format;
import java.text.SimpleDateFormat;

import jumpstart.business.domain.examples.Person;
import jumpstart.business.domain.examples.Regions;
import jumpstart.business.domain.examples.iface.IPersonServiceLocal;
import jumpstart.client.IBusinessServicesLocator;
import jumpstart.util.ExceptionUtil;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentEventCallback;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * This component will trigger the following events on its container (which in this example is the page):
 * {@link jumpstart.web.components.examples.ajaxcomponentscrud.PersonEditor#CANCEL_CREATE},
 * {@link jumpstart.web.components.examples.ajaxcomponentscrud.PersonEditor#SUCCESSFUL_CREATE}(Long personId),
 * {@link jumpstart.web.components.examples.ajaxcomponentscrud.PersonEditor#FAILED_CREATE},
 * {@link jumpstart.web.components.examples.ajaxcomponentscrud.PersonEditor#TO_UPDATE}(Long personId),
 * {@link jumpstart.web.components.examples.ajaxcomponentscrud.PersonEditor#SUCCESSFUL_UPDATE}(Long personId),
 * {@link jumpstart.web.components.examples.ajaxcomponentscrud.PersonEditor#FAILED_UPDATE}(Long personId),
 * {@link jumpstart.web.components.examples.ajaxcomponentscrud.PersonEditor#SUCCESFUL_DELETE}(Long personId),
 * {@link jumpstart.web.components.examples.ajaxcomponentscrud.PersonEditor#FAILED_DELETE}(Long personId).
 */
// @Events is applied to a component solely to document what events it may trigger. It is not checked at runtime.
@Events( { PersonEditor.CANCEL_CREATE, PersonEditor.SUCCESSFUL_CREATE, PersonEditor.FAILED_CREATE,
		PersonEditor.TO_UPDATE, PersonEditor.CANCEL_UPDATE, PersonEditor.SUCCESSFUL_UPDATE, PersonEditor.FAILED_UPDATE,
		PersonEditor.SUCCESFUL_DELETE, PersonEditor.FAILED_DELETE })
public class PersonEditor {
	public static final String CANCEL_CREATE = "cancelCreate";
	public static final String SUCCESSFUL_CREATE = "successfulCreate";
	public static final String FAILED_CREATE = "failedCreate";
	public static final String TO_UPDATE = "toUpdate";
	public static final String CANCEL_UPDATE = "cancelUpdate";
	public static final String SUCCESSFUL_UPDATE = "successfulUpdate";
	public static final String FAILED_UPDATE = "failedUpdate";
	public static final String SUCCESFUL_DELETE = "successfulDelete";
	public static final String FAILED_DELETE = "failedDelete";

	private final String _demoModeStr = System.getProperty("jumpstart.demo-mode");

	public enum Mode {
		CREATE, REVIEW, UPDATE, DELETE;
	}

	// Parameters

	@Parameter
	@Property
	private Mode _mode;

	@Parameter
	@Property
	private Long _personId;

	@SuppressWarnings("unused")
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	@Property
	private String _zone;

	// Screen fields

	@Property
	private Person _person;

	@SuppressWarnings("unused")
	@Property
	@Persist(PersistenceConstants.FLASH)
	private String _deleteMessage;

	// Generally useful bits and pieces

	@Inject
	private IBusinessServicesLocator _businessServicesLocator;

	@Component(id = "createForm")
	private Form _createForm;

	@Component(id = "updateForm")
	private Form _updateForm;

	@Inject
	private ComponentResources _componentResources;

	@Inject
	private Messages _messages;

	private Object _zonesToUpdate;

	// The code

	// setupRender() is called by Tapestry right before it starts rendering the component.

	void setupRender() {

		if (_personId == null) {
			_person = null;
		}
		else {
			if (_person == null) {
				_person = getPersonService().findPerson(_personId);
				// Handle null person in the template.
			}
		}

	}

	// /////////////////////////////////////////////////////////////////////
	// CREATE
	// /////////////////////////////////////////////////////////////////////

	// Handle event "cancelCreate"

	boolean onCancelCreate() {
		// Return false, which means we haven't handled the event so bubble it up.
		// This method is here solely as documentation, because without this method the event would bubble up anyway.
		return false;
	}

	// Component "createForm" triggers the PREPARE event when it is rendered or submitted

	void onPrepareFromCreateForm() throws Exception {
		// Instantiate a Person for the form data to overlay.
		_person = new Person();
	}

	// Component "createForm" triggers the VALIDATE_FORM event when it is submitted

	void onValidateFormFromCreateForm() {

		if (_createForm.getHasErrors()) {
			// We get here only if a validator detected an error and javascript is disabled in the browser.
			return;
		}

		if (_demoModeStr != null && _demoModeStr.equals("true")) {
			_createForm.recordError("Sorry, but Create is not allowed in Demo mode.");
			return;
		}

		try {
			_person = getPersonService().createPerson(_person);
		}
		catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a user-friendly message.
			_createForm.recordError(ExceptionUtil.getRootCause(e));
		}
	}

	// Component "createForm" triggers SUCCESS or FAILURE when it is submitted, depending on whether VALIDATE_FORM
	// records an error

	Object onSuccessFromCreateForm() {
		// We want to tell our containing page explicitly what person we've created, so we trigger new event
		// "successfulCreate" with a parameter. It will bubble up because we don't have a handler method for it.
		// When the event has been handled, Tapestry will call my callback with the result.
		_componentResources.triggerEvent(SUCCESSFUL_CREATE, new Object[] { _person.getId() }, getMyCallback());
		// _zonesToUpdate will have been updated by my callback.
		return _zonesToUpdate;
	}

	Object onFailureFromCreateForm() {
		// Rather than letting "failure" bubble up which doesn't say what you were trying to do, we trigger new event
		// "failedCreate". It will bubble up because we don't have a handler method for it.
		// When the event has been handled, Tapestry will call my callback with the result.
		_componentResources.triggerEvent(FAILED_CREATE, null, getMyCallback());
		// _zonesToUpdate will have been updated by my callback.
		return _zonesToUpdate;
	}

	// /////////////////////////////////////////////////////////////////////
	// REVIEW
	// /////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////
	// UPDATE
	// /////////////////////////////////////////////////////////////////////

	// Handle event "toUpdate"

	boolean onToUpdate(Long personId) {
		// Return false, which means we haven't handled the event so bubble it up.
		// This method is here solely as documentation, because without this method the event would bubble up anyway.
		return false;
	}

	// Handle event "cancelUpdate"

	boolean onCancelUpdate(Long personId) {
		// Return false, which means we haven't handled the event so bubble it up.
		// This method is here solely as documentation, because without this method the event would bubble up anyway.
		return false;
	}

	// Component "updateForm" triggers the PREPARE event when it is rendered or submitted

	void onPrepareFromUpdateForm(Long personId) {
		_personId = personId;
		if (_person == null) {
			// Get objects for the form fields to overlay.
			_person = getPersonService().findPerson(_personId);
			// Handle null person in the template.
		}
	}

	// Component "updateForm" triggers the VALIDATE_FORM event when it is submitted

	void onValidateFormFromUpdateForm() {

		if (_updateForm.getHasErrors()) {
			// We get here only if a validator detected an error and javascript is disabled in the browser.
			return;
		}

		try {
			getPersonService().changePerson(_person);
		}
		catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a user-friendly message.
			_updateForm.recordError(ExceptionUtil.getRootCause(e));
		}
	}

	// Component "updateForm" triggers SUCCESS or FAILURE when it is submitted, depending on whether VALIDATE_FORM
	// records an error

	Object onSuccessFromUpdateForm() {
		// We want to tell our containing page explicitly what person we've updated, so we trigger new event
		// "successfulUpdate" with a parameter. It will bubble up because we don't have a handler method for it.
		// When the event has been handled, Tapestry will call my callback with the result.
		_componentResources.triggerEvent(SUCCESSFUL_UPDATE, new Object[] { _personId }, getMyCallback());
		// _zonesToUpdate will have been updated by my callback.
		return _zonesToUpdate;
	}

	Object onFailureFromUpdateForm() {
		// Rather than letting "failure" bubble up which doesn't say what you were trying to do, we trigger new event
		// "failedUpdate". It will bubble up because we don't have a handler method for it.
		// When the event has been handled, Tapestry will call my callback with the result.
		_componentResources.triggerEvent(FAILED_UPDATE, new Object[] { _personId }, getMyCallback());
		// _zonesToUpdate will have been updated by my callback.
		return _zonesToUpdate;
	}

	// /////////////////////////////////////////////////////////////////////
	// DELETE
	// /////////////////////////////////////////////////////////////////////

	// Handle event "delete"

	Object onDelete(Long personId, Integer personVersion) {
		_personId = personId;
		_person = getPersonService().findPerson(personId);
		// Handle null person in the template.

		boolean successfulDelete = false;

		if (_person != null) {

			if (_demoModeStr != null && _demoModeStr.equals("true")) {
				_deleteMessage = "Sorry, but Delete is not allowed in Demo mode.";
			}
			else {

				// Delete the person from the database unless they've been modified elsewhere

				try {
					if (!_person.getVersion().equals(personVersion)) {
						_deleteMessage = "Cannot delete person because has been updated or deleted since last displayed. Please refresh and try again.";
					}
					else {
						getPersonService().deletePerson(_person);
						successfulDelete = true;
					}
				}
				catch (Exception e) {
					// Display the cause. In a real system we would try harder to get a user-friendly message.
					_deleteMessage = ExceptionUtil.getRootCause(e);
				}
			}
		}

		if (successfulDelete) {
			// Trigger new event "successfulDelete" (which in this example will bubble up to the page).
			// When the event has been handled, Tapestry will call my callback with the result.
			_componentResources.triggerEvent(SUCCESFUL_DELETE, new Object[] { _personId }, getMyCallback());
		}
		else {
			// Trigger new event "failedDelete" (which in this example will bubble up to the page).
			// When the event has been handled, Tapestry will call my callback with the result.
			_componentResources.triggerEvent(FAILED_DELETE, new Object[] { _personId }, getMyCallback());
		}

		// _zonesToUpdate will have been updated by my callback.
		return _zonesToUpdate;
	}

	// /////////////////////////////////////////////////////////////////////
	// OTHER
	// /////////////////////////////////////////////////////////////////////

	// Getters

	public boolean isModeCreate() {
		return _mode == Mode.CREATE;
	}

	public boolean isModeReview() {
		return _mode == Mode.REVIEW;
	}

	public boolean isModeUpdate() {
		return _mode == Mode.UPDATE;
	}

	public boolean isModeNull() {
		return _mode == null;
	}

	public String getPersonRegion() {
		// Follow the same naming convention that the Select component uses
		return _messages.get(Regions.class.getSimpleName() + "." + _person.getRegion().name());
	}

	public String getDatePattern() {
		return "dd/MM/yyyy";
	}

	public Format getDateFormat() {
		return new SimpleDateFormat(getDatePattern());
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
