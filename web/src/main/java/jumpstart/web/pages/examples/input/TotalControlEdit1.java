package jumpstart.web.pages.examples.input;

import jumpstart.business.domain.examples.Person;
import jumpstart.business.domain.examples.iface.IPersonServiceLocal;
import jumpstart.client.IBusinessServicesLocator;
import jumpstart.util.ExceptionUtil;
import jumpstart.web.components.CustomForm;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

/* This annotation tells Tapestry to declare the js file in the page so that the browser will pull it in. */
@IncludeJavaScriptLibrary({"context:js/validators.js", "context:js/custom_error.js"})
public class TotalControlEdit1 {

	// The activation context

	private Long _personId;

	// Screen fields

	@Property
	private Person _person;

	// Other pages

	@InjectPage
	private TotalControlEdit2 _page2;

	// Generally useful bits and pieces

	@Component(id = "person")
	private CustomForm _form;

	@Inject
	private IBusinessServicesLocator _businessServicesLocator;

	// The code

	// onPassivate() is called by Tapestry to get the activation context to put in the URL.

	Long onPassivate() {
		return _personId;
	}

	// onActivate() is called by Tapestry to pass in the activation context from the URL.

	void onActivate(Long personId) {
		_personId = personId;
	}

	// Form triggers the PREPARE event during form render and form submission.

	void onPrepare() throws Exception {
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

	void onValidateForm() {

		if (_form.getHasErrors()) {
			// We get here only if a validator detected an error and javascript is disabled in the browser.
			return;
		}

		try {
			getPersonService().changePerson(_person);
		}
		catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a user-friendly message.
			_form.recordError(ExceptionUtil.getRootCause(e));
		}
	}

	Object onSuccess() {
		_page2.set(_personId);
		return _page2;
	}

	Object onActionFromRefresh() {
		return this;
	}

	private IPersonServiceLocal getPersonService() {
		// Use our business services locator to get the EJB3 session bean called "PersonServiceLocal".
		return _businessServicesLocator.getPersonServiceLocal();
	}
}
