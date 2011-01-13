package jumpstart.web.pages.examples.onepagecrud;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;

import jumpstart.business.domain.examples.Person;
import jumpstart.business.domain.examples.Regions;
import jumpstart.business.domain.examples.iface.IPersonServiceLocal;
import jumpstart.client.IBusinessServicesLocator;
import jumpstart.util.ExceptionUtil;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

public class Index {

	private final String _demoModeStr = System.getProperty("jumpstart.demo-mode");
	private final int MAX_RESULTS = 40;

	private enum Mode {
		CREATE, REVIEW, UPDATE;
	}

	// The activation context
	
	@Property
	private Mode _mode;
	
	@Property
	private Long _personId;

	// Screen fields

	@SuppressWarnings("unused")
	@Property
	private List<Person> _listPersons;

	@Property
	private Person _listPerson;

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

	@Inject
	private Messages _messages;
	
	// The code

	// onPassivate() is called by Tapestry to get the activation context to put in the URL.

	Object[] onPassivate() {
		if (_mode == null) {
			return null;
		}
		else if (_mode == Mode.CREATE) {
			return new Object[] { _mode };
		}
		else if (_mode == Mode.REVIEW || _mode == Mode.UPDATE) {
			return new Object[] { _mode, _personId };
		}
		else {
			throw new IllegalStateException(_mode.toString());
		}
	}

	// onActivate() is called by Tapestry to pass in the activation context from the URL.

	void onActivate(EventContext ec) {
		if (ec.getCount() == 0) {
			_mode = null;
			_personId = null;
		}
		else if (ec.getCount() == 1) {
			_mode = ec.get(Mode.class, 0);
			_personId = null;
		}
		else {
			_mode = ec.get(Mode.class, 0);
			_personId = ec.get(Long.class, 1);
		}
	}

	// setupRender() is called by Tapestry right before it starts rendering the page.

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

		_listPersons = getPersonService().findPersons(MAX_RESULTS);
	}

	///////////////////////////////////////////////////////////////////////
	// CREATE
	///////////////////////////////////////////////////////////////////////
	
	// Handle event "toCreate"

	void onToCreate() {
		_mode = Mode.CREATE;
		_personId = null;
	}

	// Handle event "cancelCreate"

	void onCancelCreate() {
		_mode = null;
		_personId = null;
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

	void onSuccessFromCreateForm() {
		_mode = Mode.REVIEW;
		_personId = _person.getId();
	}

	///////////////////////////////////////////////////////////////////////
	// REVIEW
	///////////////////////////////////////////////////////////////////////
	
	// Handle event "selected"

	void onSelected(Long personId) {
		_mode = Mode.REVIEW;
		_personId = personId;
	}

	///////////////////////////////////////////////////////////////////////
	// UPDATE
	///////////////////////////////////////////////////////////////////////
	
	// Handle event "toUpdate"

	void onToUpdate(Long personId) {
		_mode = Mode.UPDATE;
		_personId = personId;
	}

	// Handle event "cancelUpdate"

	void onCancelUpdate(Long personId) {
		_mode = Mode.REVIEW;
		_personId = personId;
	}

	// Component "updateForm" triggers the PREPARE event when it is rendered or submitted

	void onPrepareFromUpdateForm() {
		_mode = Mode.UPDATE;
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

	void onSuccessFromUpdateForm() {
		_mode = Mode.REVIEW;
		_personId = _person.getId();
		setupRender();
	}

	///////////////////////////////////////////////////////////////////////
	// DELETE
	///////////////////////////////////////////////////////////////////////
	
	// Handle event "delete"

	void onDelete(Long personId, Integer personVersion) {
		_mode = Mode.REVIEW;
		_personId = personId;
		_person = getPersonService().findPerson(personId);
		// Handle null person in the template.

		if (_demoModeStr != null && _demoModeStr.equals("true")) {
			_deleteMessage = "Sorry, but Delete is not allowed in Demo mode.";
			return;
		}

		// Delete the person from the database unless they've been modified elsewhere

		try {
			if (!_person.getVersion().equals(personVersion)) {
				_deleteMessage = "Cannot delete person because has been updated or deleted since last displayed. Please refresh and try again.";
			}
			else {
				getPersonService().deletePerson(_person);
				_mode = null;
				_personId = null;
			}
		}
		catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a user-friendly message.
			_deleteMessage = ExceptionUtil.getRootCause(e);
		}
	}

	///////////////////////////////////////////////////////////////////////
	// OTHER
	///////////////////////////////////////////////////////////////////////
	
	// Getters

	public String getLinkCSSClass() {
		if (_listPerson != null && _listPerson.equals(_person)) {
			return "active";
		}
		else {
			return "";
		}
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
