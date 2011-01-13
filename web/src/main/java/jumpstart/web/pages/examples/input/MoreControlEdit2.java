package jumpstart.web.pages.examples.input;

import jumpstart.business.domain.examples.Person;
import jumpstart.business.domain.examples.iface.IPersonServiceLocal;
import jumpstart.client.IBusinessServicesLocator;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class MoreControlEdit2 {

	// The activation context

	private Long _personId;

	// Screen fields

	@Property
	private Person _person;

	// Generally useful bits and pieces

	@Inject
	private IBusinessServicesLocator _businessServicesLocator;
	
	// The code
	
	// set() is public so that other pages can use it to set up this page.
	
	public void set(Long personId) {
		_personId = personId;
	}

	// onPassivate() is called by Tapestry to get the activation context to put in the URL.

	Long onPassivate() {
		return _personId;
	}

	// onActivate() is called by Tapestry to pass in the activation context from the URL.

	void onActivate(Long personId) {
		_personId = personId;
	}

	// setupRender() is called by tapestry at the start of rendering - it's good for things that are display only.

	void setupRender() throws Exception {
		_person = getPersonService().findPerson(_personId);

		if (_person == null) {
			if (_personId < 4) {
				throw new IllegalStateException("Database data has not been set up!");
			}
			else {
				throw new Exception("Person " + _personId + " does not exist.");
			}
		}
	}

	private IPersonServiceLocal getPersonService() {
		// Use our business services locator to get the EJB3 session bean called "PersonServiceLocal".
		return _businessServicesLocator.getPersonServiceLocal();
	}
}
