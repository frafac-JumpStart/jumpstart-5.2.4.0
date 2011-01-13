package jumpstart.web.pages.examples.ajaxgracefulcrud;

import jumpstart.web.components.examples.ajaxgracefulcrud.PersonEditor.Mode;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.ajax.MultiZoneUpdate;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

public class Index {

	// The activation context

	@Property
	private Mode _mode;

	@Property
	private Long _personId;


	// Screen fields

	@Property
	@Persist
	private boolean _highlightZoneUpdates;

	@SuppressWarnings("unused")
	@Property
	private Long _highlightPersonId;

	// Generally useful bits and pieces

	@InjectComponent
	private Zone _listZone;

	@InjectComponent
	private Zone _editorZone;

	@Inject
	private Request _request;

	// The code

	// onPassivate() is called by Tapestry to get the activation context to put in the URL.

	Object[] onPassivate() {

		if (_mode == null) {
			return null;
		}
		else if (_mode == Mode.CREATE) {
			return new Object[] { _mode };
		}
		else if (_mode == Mode.REVIEW || _mode == Mode.UPDATE || _mode == Mode.CONFIRM_DELETE) {
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
		_highlightPersonId = _personId;
	}

	// /////////////////////////////////////////////////////////////////////
	// FILTER
	// /////////////////////////////////////////////////////////////////////

	// Handle event "filter" from component "list"

	Object onFilterFromList() {
		_highlightPersonId = _personId;

		if (_request.isXHR()) {
			return _listZone.getBody();
		}
		else {
			return null;
		}
	}

	// /////////////////////////////////////////////////////////////////////
	// CREATE
	// /////////////////////////////////////////////////////////////////////

	// Handle event "toCreate" from this page

	Object onToCreate() {
		_mode = Mode.CREATE;
		_personId = null;
		_highlightPersonId = null;

		if (_request.isXHR()) {
			return getListAndEditorZones();
		}
		else {
			return null;
		}
	}

	// Handle event "cancelCreate" from component "editor"

	Object onCancelCreateFromEditor() {
		_mode = null;
		_personId = null;
		_highlightPersonId = null;

		if (_request.isXHR()) {
			return _editorZone.getBody();
		}
		else {
			return null;
		}
	}

	// Handle event "successfulCreate" from component "editor"

	Object onSuccessfulCreateFromEditor(Long personId) {
		_mode = Mode.REVIEW;
		_personId = personId;
		_highlightPersonId = personId;

		if (_request.isXHR()) {
			return getListAndEditorZones();
		}
		else {
			return null;
		}
	}

	// Handle event "failedCreate" from component "editor"

	Object onFailedCreateFromEditor() {
		_mode = Mode.CREATE;
		_personId = null;

		if (_request.isXHR()) {
			return _editorZone.getBody();
		}
		else {
			return null;
		}
	}

	// /////////////////////////////////////////////////////////////////////
	// REVIEW
	// /////////////////////////////////////////////////////////////////////

	// Handle event "selected" from component "list"

	Object onSelectedFromList(Long personId) {
		_mode = Mode.REVIEW;
		_personId = personId;
		_highlightPersonId = personId;

		if (_request.isXHR()) {
			return getListAndEditorZones();
		}
		else {
			return null;
		}
	}

	// /////////////////////////////////////////////////////////////////////
	// UPDATE
	// /////////////////////////////////////////////////////////////////////

	// Handle event "toUpdate" from component "editor"

	Object onToUpdateFromEditor(Long personId) {
		_mode = Mode.UPDATE;
		_personId = personId;

		if (_request.isXHR()) {
			return _editorZone.getBody();
		}
		else {
			return null;
		}
	}

	// Handle event "cancelUpdate" from component "editor"

	Object onCancelUpdateFromEditor(Long personId) {
		_mode = Mode.REVIEW;
		_personId = personId;

		if (_request.isXHR()) {
			return _editorZone.getBody();
		}
		else {
			return null;
		}
	}

	// Handle event "successfulUpdate" from component "editor"

	Object onSuccessfulUpdateFromEditor(Long personId) {
		_mode = Mode.REVIEW;
		_personId = personId;
		_highlightPersonId = personId;

		if (_request.isXHR()) {
			return getListAndEditorZones();
		}
		else {
			return null;
		}
	}

	// Handle event "failedUpdate" from component "editor"

	Object onFailedUpdateFromEditor(Long personId) {

		if (_request.isXHR()) {
			return _editorZone.getBody();
		}
		else {
			return null;
		}
	}

	// /////////////////////////////////////////////////////////////////////
	// DELETE
	// /////////////////////////////////////////////////////////////////////

	// Handle event "successfulDelete" from component "editor"

	Object onSuccessfulDeleteFromEditor(Long personId) {
		_mode = null;
		_personId = null;
		_highlightPersonId = null;

		if (_request.isXHR()) {
			return getListAndEditorZones();
		}
		else {
			return null;
		}
	}

	// Handle event "failedDelete" from component "editor"

	Object onFailedDeleteFromEditor(Long personId) {
		_mode = Mode.REVIEW;
		_personId = personId;

		if (_request.isXHR()) {
			return _editorZone.getBody();
		}
		else {
			return null;
		}
	}

	// /////////////////////////////////////////////////////////////////////
	// CONFIRM DELETE - used only when JavaScript is disabled.
	// /////////////////////////////////////////////////////////////////////

	// Handle event "cancelConfirmDelete" from component "editor"

	Object onCancelConfirmDeleteFromEditor(Long personId) {
		_mode = Mode.REVIEW;
		_personId = personId;
		_highlightPersonId = personId;
		return null;
	}

	// /////////////////////////////////////////////////////////////////////
	// GETTERS
	// /////////////////////////////////////////////////////////////////////

	private MultiZoneUpdate getListAndEditorZones() {
		return new MultiZoneUpdate("listZone", _listZone.getBody()).add("editorZone", _editorZone.getBody());
	}

	public String getZoneUpdateFunction() {
		return _highlightZoneUpdates ? "highlight" : "show";
	}

}
