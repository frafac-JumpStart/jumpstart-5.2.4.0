package jumpstart.web.pages.examples.input;

import java.util.List;

import jumpstart.business.domain.examples.Person;
import jumpstart.business.domain.examples.iface.IPersonServiceLocal;
import jumpstart.client.IBusinessServicesLocator;

import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Log;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;

// This annotation tells Tapestry to declare the js file in the page so that the browser will pull it in.
@IncludeJavaScriptLibrary( { "context:js/custom_error.js" })
public class AJAXValidators1 {
	private final int MAX_RESULTS = 30;

	// Screen fields

	@Property
	private String _firstName;

	@Property
	private String _lastName;

	@SuppressWarnings("unused")
	@Property
	private List<Person> _persons;

	// Other pages

	@InjectPage
	private NoValidationBubbles2 _page2;

	// Useful bits and pieces

	@Inject
	private Request _request;

	@Inject
	private IBusinessServicesLocator _businessServicesLocator;

	// The code

	void setupRender() {
		_persons = getPersonService().findPersons(MAX_RESULTS);
	}

	@Log
	JSONObject onAjaxValidateFromFirstName() {
		String firstName = _request.getParameter("param");

		try {
			validateFirstNameIsUnique(firstName);
		}
		catch (Exception e) {
			return new JSONObject().put("error", e.getMessage());
		}
		
		return new JSONObject();
	}

	JSONObject onAjaxValidateFromLastName() {
		String lastName = _request.getParameter("param");

		try {
			validateLastNameIsUnique(lastName);
		}
		catch (Exception e) {
			return new JSONObject().put("error", e.getMessage());
		}
		
		return new JSONObject();
	}

	Object onSuccess() {
		_page2.set(_firstName, _lastName);
		return _page2;
	}

	void validateFirstNameIsUnique(String firstName) throws Exception {
		if (firstName != null) {
			List<Person> persons = getPersonService().findPersonsByFirstName(firstName);

			if (persons.size() > 0) {
				throw new Exception("The name is not available.");
			}
		}
	}

	void validateLastNameIsUnique(String lastName) throws Exception {
		if (lastName != null) {
			List<Person> persons = getPersonService().findPersonsByLastName(lastName);

			if (persons.size() > 0) {
				throw new Exception("The name is not available.");
			}
		}
	}

	private IPersonServiceLocal getPersonService() {
		// Use our business services locator to get the EJB3 session bean called "PersonServiceLocal".
		return _businessServicesLocator.getPersonServiceLocal();
	}
}
