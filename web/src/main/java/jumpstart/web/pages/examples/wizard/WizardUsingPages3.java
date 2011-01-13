package jumpstart.web.pages.examples.wizard;

import jumpstart.util.ExceptionUtil;
import jumpstart.web.base.examples.wizard.WizardConversationalPage;
import jumpstart.web.pages.Index;
import jumpstart.web.state.examples.wizard.CreditRequest;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;

public class WizardUsingPages3 extends WizardConversationalPage {

	// The conversation contents

	@Property
	private CreditRequest _creditRequest;

	// Other pages

	@InjectPage
	private WizardUsingPages2 _prevPage;

	@InjectPage
	private WizardUsingPagesSuccess _nextPage;

	@InjectPage
	private Index _index;

	// Generally useful bits and pieces

	@Component(id = "form")
	private Form _form;

	// The code

	void onPrepare() {
		if (_creditRequest == null) {
			// Get objects for the form fields to overlay.
			_creditRequest = restoreCreditRequestFromConversation();
		}
	}

	void onValidateForm() {
		saveCreditRequestToConversation(_creditRequest);

		try {
			// In the real world we would probably submit it to the business layer here
			// but we're not, so let's simulate a busy period then complete the request!

			sleep(5000);
			_creditRequest.complete();
		}
		catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a user-friendly message.
			_form.recordError(ExceptionUtil.getRootCause(e));
		}
	}

	Object onSuccess() {
		endConversation();
		_nextPage.set(_creditRequest.getAmount(), _creditRequest.getApplicantName());
		return _nextPage;
	}

	Object onPrev() {
		_prevPage.set(getConversationId());
		return _prevPage;
	}

	Object onQuit() {
		endConversation();
		return _index;
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		}
		catch (InterruptedException e) {
			// Ignore
		}
	}
}
