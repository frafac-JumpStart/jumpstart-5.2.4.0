// From http://wiki.apache.org/tapestry/Tapestry5AndJavaScriptExplained

/*
 * A mixin that watches that watches every link (<a>) contained in the given element.
 * When one is clicked it sets the class of the link to "active" and the class of the others to "".
 */

var Tabber = Class.create();
var links = null;

Tabber.prototype = {
		
        initialize: function(elementId, activeLinkNum) {
//	alert('initializeactiveLinkNum = ' + activeLinkNum);
			var me = this;
			links = $$('#' + elementId + ' a');

			links.each(function(link) {
				Event.observe(link, 'click', me.doLinkClicked.bindAsEventListener(me));
			});

			var target = links[activeLinkNum];
			this.makeActive(target);
		},
        
		doLinkClicked: function(evt) {
//	alert('doTabClicked: evt = ' + evt);
			var target = Event.element(evt);
			this.makeActive(target);
			
			return false;
		},
        
		makeActive: function(element) {
//	alert('makeActive: element =' + element);
			
			links.each(function(link) {
				if (link == element) {
//					alert('Same');
					link.className = 'active';
				}
				else {
//					alert('Different');
					link.className = '';
				}
			});
		}
		
		// Where should this IE stuff go?
//		$(document).ready(function(){
//			  $("ul.tabs li").hover(
//			    function() { $(this).addClass("iehover"); },
//			    function() { $(this).removeClass("iehover"); }
//			  );
//		});

}