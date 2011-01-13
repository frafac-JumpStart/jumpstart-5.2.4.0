package jumpstart.web.pages.examples.jodatime;

import jumpstart.business.domain.examples.DateStuff;
import jumpstart.business.domain.examples.iface.IDateStuffServiceLocal;
import jumpstart.client.IBusinessServicesLocator;
import jumpstart.util.ExceptionUtil;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

public class JodaTime2 {
	
	// The activation context

	private Long _dateStuffId;

	// Screen fields

	@Property
	private DateStuff _dateStuff;

	// Generally useful bits and pieces

	@Inject
	private IBusinessServicesLocator _businessServicesLocator;

	@Component(id = "dates")
	private Form _form;

	// The code
	
	Long onPassivate() {
		return _dateStuffId;
	}

	void onActivate(Long dateStuffId) {
		_dateStuffId = dateStuffId;
	}
	
	// Form triggers the PREPARE event during form render and form submission.

	void onPrepare() {
		// Ask business service to find DateStuff
		_dateStuff = getDateStuffService().findDateStuff(_dateStuffId);

		if (_dateStuff == null) {
			throw new IllegalStateException("Database data has not been set up!");
		}
	}
	
	void onValidateForm() {
		try {
			getDateStuffService().changeDateStuff(_dateStuff);
		}
		catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a user-friendly message.
			_form.recordError(ExceptionUtil.getRootCause(e));
		}
	}

	Object onSuccess() {
		return JodaTime1.class;
	}

	private IDateStuffServiceLocal getDateStuffService() {
		return _businessServicesLocator.getDateStuffServiceLocal();
	}

	public String getDateFieldFormat() {
		return "dd/MM/yyyy";
	}
}