// A class that ignores clicks after the first one.

var ClickOnce = Class.create();
var alreadyClickedOnce = false;

ClickOnce.prototype = {

	initialize: function(elementId) {
		Event.observe($(elementId), 'click', this.doClickOnce.bindAsEventListener(this));
	},
        
	doClickOnce: function(e) {
		if (alreadyClickedOnce) {
			e.stop();
		}
		alreadyClickedOnce = true;
	}
}