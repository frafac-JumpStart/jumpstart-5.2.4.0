package jumpstart.web.pages.examples.layoutwithmenu;

import jumpstart.web.components.examples.ajaxcomponentscrud.PersonEditor.Mode;

import org.apache.tapestry5.ajax.MultiZoneUpdate;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;

public class Page3 {

	// Screen fields

	@Property
	@Persist
	private boolean _highlightZoneUpdates;

	@SuppressWarnings("unused")
	@Property
	private Long _highlightPersonId;

	@SuppressWarnings("unused")
	@Property
	private Mode _mode;

	@Property
	private Long _personId;

	// Generally useful bits and pieces

	@InjectComponent
	private Zone _listZone;

	@InjectComponent
	private Zone _editorZone;

	// The code

	// setupRender() is called by Tapestry right before it starts rendering the page.

	void setupRender() {
		_highlightPersonId = _personId;
	}

	// /////////////////////////////////////////////////////////////////////
	// FILTER
	// /////////////////////////////////////////////////////////////////////

	// Handle event "filter" from component "list"

	Object onFilterFromList(Long highlightPersonId) {
		_highlightPersonId = highlightPersonId;
		return _listZone.getBody();
	}

	// /////////////////////////////////////////////////////////////////////
	// CREATE
	// /////////////////////////////////////////////////////////////////////

	// Handle event "toCreate" from this page

	Object onToCreate() {
		_mode = Mode.CREATE;
		_personId = null;
		_highlightPersonId = null;
		return getListAndEditorZones();
	}

	// Handle event "cancelCreate" from component "editor"

	Object onCancelCreateFromEditor() {
		_mode = null;
		_personId = null;
		return _editorZone.getBody();
	}

	// Handle event "successfulCreate" from component "editor"

	Object onSuccessfulCreateFromEditor(Long personId) {
		_mode = Mode.REVIEW;
		_personId = personId;
		_highlightPersonId = personId;
		return getListAndEditorZones();
	}

	// Handle event "failedCreate" from component "editor"

	Object onFailedCreateFromEditor() {
		_mode = Mode.CREATE;
		_personId = null;
		return _editorZone.getBody();
	}

	// /////////////////////////////////////////////////////////////////////
	// REVIEW
	// /////////////////////////////////////////////////////////////////////

	// Handle event "selected" from component "list"

	Object onSelectedFromList(Long personId) {
		_mode = Mode.REVIEW;
		_personId = personId;
		_highlightPersonId = personId;
		return getListAndEditorZones();
	}

	// /////////////////////////////////////////////////////////////////////
	// UPDATE
	// /////////////////////////////////////////////////////////////////////

	// Handle event "toUpdate" from component "editor"

	Object onToUpdateFromEditor(Long personId) {
		_mode = Mode.UPDATE;
		_personId = personId;
		return _editorZone.getBody();
	}

	// Handle event "cancelUpdate" from component "editor"

	Object onCancelUpdateFromEditor(Long personId) {
		_mode = Mode.REVIEW;
		_personId = personId;
		return _editorZone.getBody();
	}

	// Handle event "successfulUpdate" from component "editor"

	Object onSuccessfulUpdateFromEditor(Long personId) {
		_mode = Mode.REVIEW;
		_personId = personId;
		_highlightPersonId = personId;
		return getListAndEditorZones();
	}

	// Handle event "failedUpdate" from component "editor"

	Object onFailedUpdateFromEditor(Long personId) {
		return _editorZone.getBody();
	}

	// /////////////////////////////////////////////////////////////////////
	// DELETE
	// /////////////////////////////////////////////////////////////////////

	// Handle event "successfulDelete" from component "editor"

	Object onSuccessfulDeleteFromEditor(Long personId) {
		_mode = null;
		_personId = null;
		_highlightPersonId = null;
		return getListAndEditorZones();
	}

	// Handle event "failedDelete" from component "editor"

	Object onFailedDeleteFromEditor(Long personId) {
		_mode = Mode.REVIEW;
		_personId = personId;
		return _editorZone.getBody();
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
