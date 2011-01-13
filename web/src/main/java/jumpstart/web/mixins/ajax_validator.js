// A class that invokes a listener in the component via AJAX.
// Based on http://tinybits.blogspot.com/2010/03/new-and-better-zoneupdater.html
// and http://tinybits.blogspot.com/2009/05/simple-onevent-mixin.html
// and tapestry.js.

function addRequestParameter(name, value, url) {
	if (url.indexOf('?') < 0) {
		url += '?'
	} else {
		url += '&';
	}
	value = escape(value);
	url += name + '=' + value;
	return url;
}

var AjaxValidator = Class.create({

	initialize : function(spec) {
		this.field = $(spec.elementId);
		this.listenerURI = spec.listenerURI;

		// Set up a listener that validates the field - asynchronously in the server - on change of focus

		document.observe(Tapestry.FOCUS_CHANGE_EVENT, function(event) {
			// If changing focus *within the same form* then perform validation.  
			// Note that Tapestry.currentFocusField does not change
			// until after the FOCUS_CHANGE_EVENT notification.
			
			if (Tapestry.currentFocusField == this.field &&
				this.field.form == event.memo.form)
				this.asyncValidateInServer();
			}.bindAsEventListener(this)
		);
	},
	
	asyncValidateInServer : function() {
		var value = this.field.value;
		var listenerURIWithParam = this.listenerURI;
    			
		if (value) {
			listenerURIWithParam = addRequestParameter('param', value, this.listenerURI);
			
			new Ajax.Request(listenerURIWithParam, {
				method: 'get',
				onFailure: function(t) {
				    alert('Error communication with the server: ' + t.responseText.stripTags());
				},
				onException: function(t, exception) {
				    alert('Error communication with the server: ' + exception.message);
				},
				onSuccess: function(t) {
					if (t.responseJSON.error) {
						this.field.showValidationMessage(t.responseJSON.error);
					}
				}.bind(this)
			});
		}
	}
	
});
