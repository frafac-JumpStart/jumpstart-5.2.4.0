//
// Based on a solution by Piero Sartini at http://article.gmane.org/gmane.comp.java.tapestry.user/81532
//

/**
 * A script that overrides the FieldEventManager defined in tapestry.js 
 * to replace its validation bubbles with our preferred way of displaying 
 * and removing errors detected by client-side validators.
 * 
 * If a validated field has an error, this script adds the css class "error-field"
 * to the field, "error-field-c" to the field's container, "error-label" to the label, 
 * "error-label-c" to the label's container, "error-msg" to the message, and 
 * "error-msg-c" to the message's container.
 * 
 * To work, it depends on the field element having been generated by a Tapestry 
 * Field component (eg. TextField), the label having been generated by a Tapestry 
 * Label component, and the error message having been generated by our CustomError
 * component.
 */
Tapestry.FieldEventManager.addMethods( {

	/**
	 * Overrides the corresponding method of Tapestry.FieldEventManager in tapestry.js.
	 */
	initialize : function(field) {
		this.field = $(field);
		
		// Identify the field, label and message and their containers 

		var id = this.field.id;
		this.fieldContainer = this.field.parentNode;
	    Element.extend(this.fieldContainer);

		var selector = "label[for='" + id + "']";

		this.label = this.field.up("form").down(selector);
		this.icon = $(id + '_icon');
		
		this.labelContainer = this.label.parentNode;
		Element.extend(this.labelContainer);

		var msgId = id + '-msg';
		this.msg = $(msgId);

		// If there's no message element then must not have supplied a CustomError component, 
		// but rather than not displaying messages, we'll create a message element below the field
		if (!this.msg) {
			this.msg = new Element('strong', {
				'id' : msgId,
				'class' : 'msg'
			});
			this.fieldContainer.insert( {
				bottom : this.msg
			});
		}
		
		this.msgContainer = this.msg.parentNode;
		Element.extend(this.msgContainer);
		
		this.translator = Prototype.K;

		var fem = $(this.field.form).getFormEventManager();

		if (fem.validateOnBlur) {

			document.observe(Tapestry.FOCUS_CHANGE_EVENT, function(event) {
				/*
				 * If changing focus *within the same form* then perform
				 * validation. Note that Tapestry.currentFocusField does not
				 * change until after the FOCUS_CHANGE_EVENT notification.
				 */
				if (Tapestry.currentFocusField == this.field
						&& this.field.form == event.memo.form)
					this.validateInput();

			}.bindAsEventListener(this));
		}

		if (fem.validateOnSubmit) {
			$(this.field.form).observe(Tapestry.FORM_VALIDATE_FIELDS_EVENT,
					this.validateInput.bindAsEventListener(this));
		}
	},
	
	/**
	 * Show a validation message and decorate the parent element of it, the field, and the label.
	 * Overrides the corresponding method of Tapestry.FieldEventManager in tapestry.js.
	 * 
	 * @param message
	 *            validation message to display
	 */
	showValidationMessage : function(message) {
		$T(this.field).validationError = true;
		$T(this.field.form).validationError = true;
		
		// Put the given message inside the msg element
		
		this.msg.update(message);

		// Add css class "error" to the input field
	
		this.label.addClassName("error-label");
		this.labelContainer.addClassName("error-label-c");
		this.field.addClassName("error-field");
		this.fieldContainer.addClassName("error-field-c");
		this.msg.addClassName("error-msg");
		this.msgContainer.addClassName("error-msg-c");
	},
	
	/**
	 * Removes validation decorations if present.
	 * Overrides the corresponding method of Tapestry.FieldEventManager in tapestry.js.
	 */
	removeDecorations : function() {
		
		// Clear the msg element
		
		this.msg.update(null);
	
		// Remove css class "error" from my field (the input field)
	
		this.label.removeClassName("error-label");
		this.labelContainer.removeClassName("error-label-c");
		this.field.removeClassName("error-field");
		this.fieldContainer.removeClassName("error-field-c");
		this.msg.removeClassName("error-msg");
		this.msgContainer.removeClassName("error-msg-c");
	}
} );