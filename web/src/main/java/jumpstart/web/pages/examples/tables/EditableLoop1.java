package jumpstart.web.pages.examples.tables;

import java.text.DateFormat;
import java.text.Format;
import java.util.List;
import java.util.Locale;

import jumpstart.business.domain.examples.Person;
import jumpstart.business.domain.examples.iface.IPersonServiceLocal;
import jumpstart.client.IBusinessServicesLocator;
import jumpstart.util.ExceptionUtil;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

public class EditableLoop1 {
	private final int MAX_RESULTS = 30;

	// Screen fields
	
	@Property
	private List<Person> _persons;

	@SuppressWarnings("unused")
	@Property
	private Person _person;
	
	// Other pages
	
	@InjectPage
	private EditableLoop2 _page2;

	// Generally useful bits and pieces

	@Component(id = "personsedit")
	private Form _form;

	@Inject
	private IBusinessServicesLocator _businessServicesLocator;

	@Inject
	private Locale _currentLocale;
	
	// The code

	// Form triggers the PREPARE event during form render and form submission.
	
	void onPrepare() {
		// Get all persons - ask business service to find them (from the database)
		_persons = getPersonService().findPersons(MAX_RESULTS);
	}

	void onValidateForm() {

		if (_form.getHasErrors()) {
			// We get here only if a validator detected an error and javascript is disabled in the browser.
			return;
		}

		try {
			System.out.println(">>> persons = " + _persons);
			// In a real application you would persist them to the database instead of printing them
			//getPersonService().changePersons(_persons);
		}
		catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a user-friendly message.
			_form.recordError(ExceptionUtil.getRootCause(e));
		}
	}

	Object onSuccess() {
		_page2.set(_persons);
		return _page2;
	}

	private IPersonServiceLocal getPersonService() {
		// Use our business services locator to get the EJB3 session bean called "PersonServiceLocal".
		return _businessServicesLocator.getPersonServiceLocal();
	}

	public Format getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.MEDIUM, _currentLocale);
	}
}
