package jumpstart.web.pages.examples.state;

import jumpstart.web.pages.Index;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * This page demonstrates using page persistence to remember data through the redirect. By default, the Persist
 * annotation saves the data in the session, but other strategies can be specified in Persist.
 */
public class PassingDataBetweenPages3 {

	// Work fields
	
	@Persist(PersistenceConstants.FLASH)
	private String _firstName;

	@Persist(PersistenceConstants.FLASH)
	private String _lastName;

	// Generally useful bits and pieces
	
	@Inject
	private ComponentResources _resources;
	
	// The code

	// set() is public so that other pages can use it to set up this page.

	public void set(String firstName, String lastName) {
		_firstName = firstName;
		_lastName = lastName;
	}

	public String getName() {
		return _firstName + " " + _lastName;
	}

	Object onActionFromReturn() {
		_resources.discardPersistentFieldChanges();
		return PassingDataBetweenPages1.class;
	}

	Object onActionFromGoHome() {
		_resources.discardPersistentFieldChanges();
		return Index.class;
	}
}
