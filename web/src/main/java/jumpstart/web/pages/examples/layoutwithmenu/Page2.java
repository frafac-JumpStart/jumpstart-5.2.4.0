package jumpstart.web.pages.examples.layoutwithmenu;

import jumpstart.web.components.examples.componentscrud.PersonEditor.Mode;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.annotations.Property;

public class Page2 {

	// The activation context

	@Property
	private String _partialName;

	@SuppressWarnings("unused")
	@Property
	private Long _highlightPersonId;

	@Property
	private Mode _mode;

	@Property
	private Long _personId;

	// The code

	// onPassivate() is called by Tapestry to get the activation context to put in the URL.

	Object[] onPassivate() {

		if (_mode == null) {
			return new Object[] { _partialName };
		}
		else if (_mode == Mode.CREATE) {
			return new Object[] { _partialName, _mode };
		}
		else if (_mode == Mode.REVIEW || _mode == Mode.UPDATE) {
			return new Object[] { _partialName, _mode, _personId };
		}
		else {
			throw new IllegalStateException(_mode.toString());
		}

	}

	// onActivate() is called by Tapestry to pass in the activation context from the URL.

	void onActivate(EventContext ec) {

		if (ec.getCount() == 0) {
			_partialName = null;
			_mode = null;
			_personId = null;
		}
		else if (ec.getCount() == 1) {
			_partialName = ec.get(String.class, 0);
			_mode = null;
			_personId = null;
		}
		else if (ec.getCount() == 2) {
			_partialName = ec.get(String.class, 0);
			_mode = ec.get(Mode.class, 1);
			_personId = null;
		}
		else {
			_partialName = ec.get(String.class, 0);
			_mode = ec.get(Mode.class, 1);
			_personId = ec.get(Long.class, 2);
		}

	}

	// setupRender() is called by Tapestry right before it starts rendering the page.
	
	void setupRender() {
		_highlightPersonId = _personId;
	}
	
	// /////////////////////////////////////////////////////////////////////
	// CREATE
	// /////////////////////////////////////////////////////////////////////

	// Handle event "toCreate" from this page

	void onToCreate() {
		_mode = Mode.CREATE;
		_personId = null;
	}
	
	// Handle event "cancelCreate" from component "editor"

	void onCancelCreateFromEditor() {
		_mode = null;
		_personId = null;
	}
	
	// Handle event "successfulCreate" from component "editor"

	void onSuccessfulCreateFromEditor(Long personId) {
		_mode = Mode.REVIEW;
		_personId = personId;
	}
	
	// Handle event "failedCreate" from component "editor"

	void onFailedCreateFromEditor() {
		// Nothing to do
	}

	// /////////////////////////////////////////////////////////////////////
	// REVIEW
	// /////////////////////////////////////////////////////////////////////

	// Handle event "selected" from component "list"

	void onSelectedFromList(Long personId) {
		_mode = Mode.REVIEW;
		_personId = personId;
	}

	// /////////////////////////////////////////////////////////////////////
	// UPDATE
	// /////////////////////////////////////////////////////////////////////

	// Handle event "toUpdate" from component "editor"

	void onToUpdateFromEditor(Long personId) {
		_mode = Mode.UPDATE;
		_personId = personId;
	}

	// Handle event "cancelUpdate" from component "editor"

	void onCancelUpdateFromEditor(Long personId) {
		_mode = Mode.REVIEW;
		_personId = personId;
	}

	// Handle event "successfulUpdate" from component "editor"
	
	void onSuccessfulUpdateFromEditor(Long personId) {
		_mode = Mode.REVIEW;
		_personId = personId;
	}

	// Handle event "failedUpdate" from component "editor"
	
	void onFailedUpdateFromEditor(Long personId) {
		// Nothing to do
	}

	// /////////////////////////////////////////////////////////////////////
	// DELETE
	// /////////////////////////////////////////////////////////////////////

	// Handle event "successfulDelete" from component "editor"

	void onSuccessfulDeleteFromEditor(Long personId) {
		_mode = null;
		_personId = null;
	}

	// Handle event "failedDelete" from component "editor"

	void onFailedDeleteFromEditor(Long personId) {
		// Nothing to do
	}

}
