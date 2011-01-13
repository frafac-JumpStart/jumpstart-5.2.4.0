package jumpstart.web.pages.examples.totalcontrolcrud;

import java.text.Format;
import java.text.SimpleDateFormat;

import jumpstart.business.domain.examples.Person;
import jumpstart.business.domain.examples.Regions;
import jumpstart.business.domain.examples.iface.IPersonServiceLocal;
import jumpstart.client.IBusinessServicesLocator;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

public class Review {

	// The activation context

	@Property
	private Long _personId;

	// Screen fields

	@Property
	private Person _person;

	// Generally useful bits and pieces
	
	@Inject
	private IBusinessServicesLocator _businessServicesLocator;

	@Inject
	private Messages _messages;

	// The code

	// onPassivate() is called by Tapestry to get the activation context to put in the URL.

	Long onPassivate() {
		return _personId;
	}

	// onActivate() is called by Tapestry to pass in the activation context from the URL.

	void onActivate(Long personId) {
		_personId = personId;
	}

	// setupRender() is called by Tapestry right before it starts rendering the page.

	void setupRender() {
		_person = getPersonService().findPerson(_personId);
		// Handle null person in the template.
	}

	private IPersonServiceLocal getPersonService() {
		// Use our business services locator to get the EJB3 session bean called "PersonServiceLocal".
		return _businessServicesLocator.getPersonServiceLocal();
	}
	
	public String getPersonRegion() {
		// Follow the same naming convention that the Select component uses
		return _messages.get(Regions.class.getSimpleName() + "." + _person.getRegion().name());
	}

	public Format getStartDateFormat() {
		final Format f = new SimpleDateFormat("dd MMMM yyyy G");
		return f;
	}
}
