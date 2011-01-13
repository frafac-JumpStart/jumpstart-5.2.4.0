// From http://wiki.apache.org/tapestry/Tapestry5AndJavaScriptExplained

// A class that attaches a confirmation box (with logic)  to
// the 'onclick' event of any HTML element.
// @author Chris Lewis Apr 18, 2008 <chris@thegodcode.net>

var Confirm = Class.create();
Confirm.prototype = {
		
        initialize: function(elementId, message) {
            this.message = message;
            Event.observe($(elementId), 'click', this.doConfirm.bindAsEventListener(this));
        },
        
        doConfirm: function(e) {
            if (! confirm(this.message)) {
                    e.stop();
            }
        }
        
}