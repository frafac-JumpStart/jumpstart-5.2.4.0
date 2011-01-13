package jumpstart.web.pages.examples.wizard;

import jumpstart.util.ExceptionUtil;
import jumpstart.web.base.examples.wizard.WizardConversationalPage;
import jumpstart.web.pages.Index;
import jumpstart.web.state.examples.wizard.CreditRequest;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;

public class WizardUsingPages1 extends WizardConversationalPage {

	// The conversation contents

	@Property
	private CreditRequest _creditRequest;

	// Other pages

	@InjectPage
	private WizardUsingPages2 _nextPage;

	@InjectPage
	private Index _index;

	// Generally useful bits and pieces

	@Component(id = "form")
	private Form _form;

	// The code

	@Override
	public void startConversation() {
		super.startConversation();
		_creditRequest = new CreditRequest();
		saveCreditRequestToConversation(_creditRequest);
	}

	void onPrepare() {
		if (_creditRequest == null) {
			// Get objects for the form fields to overlay.
			_creditRequest = restoreCreditRequestFromConversation();
		}
	}

	void onValidateForm() {

		if (_form.getHasErrors()) {
			// We get here only if a validator detected an error and javascript is disabled in the browser.
			return;
		}

		saveCreditRequestToConversation(_creditRequest);

		try {
			_creditRequest.validateAmountInfo();
		}
		catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a user-friendly message.
			_form.recordError(ExceptionUtil.getRootCause(e));
		}
	}

	Object onSuccess() {
		_nextPage.set(getConversationId());
		return _nextPage;
	}

	Object onQuit() {
		endConversation();
		return _index;
	}

}
