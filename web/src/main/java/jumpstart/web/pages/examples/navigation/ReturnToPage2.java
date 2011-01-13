package jumpstart.web.pages.examples.navigation;

import jumpstart.web.pages.Index;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.ioc.annotations.Inject;

public class ReturnToPage2 {

	// The activation context

	private String _messageFromCaller;

	// Screen fields

	@Persist
	private Link _linkBackToCaller;

	// Generally useful bits and pieces

	@Inject
	private ComponentResources _componentResources;

	// The code
	
	public void set(String messageFromCaller, Link linkBackToCaller) {
		_messageFromCaller = messageFromCaller;
		_linkBackToCaller = linkBackToCaller;
	}

	String onPassivate() {
		return _messageFromCaller;
	}

	void onActivate(String messageFromCaller) {
		_messageFromCaller = messageFromCaller;
	}

	Link onActionFromReturn() {
		_componentResources.discardPersistentFieldChanges();
		return _linkBackToCaller;
	}

	Object onActionFromGoHome() {
		_componentResources.discardPersistentFieldChanges();
		return Index.class;
	}

	public String getMessageFromCaller() {
		return _messageFromCaller;
	}

	public Link getLinkBackToCaller() {
		return _linkBackToCaller;
	}
}
