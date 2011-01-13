package jumpstart.web.pages.examples.ajaxfiltercrud;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;

import jumpstart.business.domain.examples.Person;
import jumpstart.business.domain.examples.Regions;
import jumpstart.business.domain.examples.iface.IPersonServiceLocal;
import jumpstart.client.IBusinessServicesLocator;
import jumpstart.util.ExceptionUtil;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.ajax.MultiZoneUpdate;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

public class Index {

	private final String _demoModeStr = System.getProperty("jumpstart.demo-mode");
	private final int MAX_RESULTS = 40;

	private enum Mode {
		CREATE, REVIEW, UPDATE;
	}

	// Screen fields

	@Property
	@Persist
	private boolean _highlightZoneUpdates;

	@Property
	@Persist
	private String _partialName;

	@SuppressWarnings("unused")
	@Property
	@Persist
	private List<Person> _listPersons;

	@Property
	private Person _listPerson;

	@Property
	private Long _highlightPersonId;

	@Property
	private Mode _mode;

	@Property
	private Long _personId;

	@Property
	private Person _person;

	@SuppressWarnings("unused")
	@Property
	@Persist(PersistenceConstants.FLASH)
	private String _deleteMessage;

	@SuppressWarnings("unused")
	@Component(id = "list")
	@Property
	private Grid _list;

	// Generally useful bits and pieces

	@Inject
	private IBusinessServicesLocator _businessServicesLocator;

	@Component(id = "createForm")
	private Form _createForm;

	@Component(id = "updateForm")
	private Form _updateForm;

	@InjectComponent
	private Zone _listZone;

	@InjectComponent
	private Zone _editorZone;

	@Inject
	private Messages _messages;

	// The code

	// setupRender() is called by Tapestry right before it starts rendering the page.

	void setupRender() {
		setupList();
		setupEditor();
	}

	void setupList() {
		_listPersons = getPersonService().findPersons(_partialName, MAX_RESULTS);
		_highlightPersonId = _personId;
		if (_highlightPersonId == null) {
			// Null is a nuisance because it fails to match the signature of our onSuccess method.
			_highlightPersonId = -1L;
		}
	}

	void setupEditor() {
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
	// FILTER
	// /////////////////////////////////////////////////////////////////////

	Object onSuccessFromFilterForm(Long highlightPersonId) {
		_personId = highlightPersonId;
		setupList();
		return _listZone.getBody();
	}

	// /////////////////////////////////////////////////////////////////////
	// CREATE
	// /////////////////////////////////////////////////////////////////////

	// Handle event "toCreate"

	Object onToCreate() {
		_mode = Mode.CREATE;
		_personId = null;
		setupList();
		setupEditor();
		return getListAndEditorZones();
	}

	// Handle event "cancelCreate"

	Object onCancelCreate() {
		_mode = null;
		_personId = null;
		setupEditor();
		return _editorZone.getBody();
	}

	// Component "createForm" triggers the PREPARE event when it is rendered or submitted

	void onPrepareFromCreateForm() throws Exception {
		_mode = Mode.CREATE;
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
		_mode = Mode.REVIEW;
		_personId = _person.getId();
		setupList();
		setupEditor();
		return getListAndEditorZones();
	}

	Object onFailureFromCreateForm() {
		setupEditor();
		return _editorZone.getBody();
	}

	// /////////////////////////////////////////////////////////////////////
	// REVIEW
	// /////////////////////////////////////////////////////////////////////

	// Handle event "selected"

	Object onSelected(Long personId) {
		_mode = Mode.REVIEW;
		_personId = personId;
		setupList();
		setupEditor();
		return getListAndEditorZones();
	}

	// /////////////////////////////////////////////////////////////////////
	// UPDATE
	// /////////////////////////////////////////////////////////////////////

	// Handle event "toUpdate"

	Object onToUpdate(Long personId) {
		_mode = Mode.UPDATE;
		_personId = personId;
		setupEditor();
		return _editorZone.getBody();
	}

	// Handle event "cancelUpdate"

	Object onCancelUpdate(Long personId) {
		_mode = Mode.REVIEW;
		_personId = personId;
		setupEditor();
		return _editorZone.getBody();
	}

	// Component "updateForm" triggers the PREPARE event when it is rendered or submitted

	void onPrepareFromUpdateForm(Long personId) {
		_personId = personId;
		if (_person == null) {
			// Get objects for the form fields to overlay.
			_person = getPersonService().findPerson(_personId);
			// Handle null person in the template.
		}
		_mode = Mode.UPDATE;
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
		_mode = Mode.REVIEW;
		_personId = _person.getId();
		setupList();
		setupEditor();
		return getListAndEditorZones();
	}

	Object onFailedUpdate(Long personId) {
		setupEditor();
		return _editorZone.getBody();
	}

	// /////////////////////////////////////////////////////////////////////
	// DELETE
	// /////////////////////////////////////////////////////////////////////

	// Handle event "delete"

	Object onDelete(Long personId, Integer personVersion) {
		boolean successfulDelete = false;

		_person = getPersonService().findPerson(personId);
		// Handle null person in the template.

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
			_mode = null;
			_personId = null;
			setupList();
			setupEditor();
			return getListAndEditorZones();
		}
		else {
			_mode = Mode.REVIEW;
			_personId = personId;
			setupEditor();
			return _editorZone.getBody();
		}
	}

	// /////////////////////////////////////////////////////////////////////
	// OTHER
	// /////////////////////////////////////////////////////////////////////

	public String getLinkCSSClass() {
		if (_listPerson != null && _listPerson.getId().equals(_highlightPersonId)) {
			return "active";
		}
		else {
			return "";
		}
	}

	private MultiZoneUpdate getListAndEditorZones() {
		return new MultiZoneUpdate("listZone", _listZone.getBody()).add("editorZone", _editorZone.getBody());
	}

	public String getZoneUpdateFunction() {
		return _highlightZoneUpdates ? "highlight" : "show";
	}

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
}
