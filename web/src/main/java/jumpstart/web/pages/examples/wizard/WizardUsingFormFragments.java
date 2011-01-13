package jumpstart.web.pages.examples.wizard;

import jumpstart.util.ExceptionUtil;
import jumpstart.web.commons.Conversations;
import jumpstart.web.pages.Index;
import jumpstart.web.state.examples.wizard.CreditRequest;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;

public class WizardUsingFormFragments {

	public static final String WIZARD_CONVERSATION_PREFIX = "wiz";
	public static final String CREDIT_REQUEST_KEY = "CR";

	public enum Step {
		START, APPLICANT, SUBMIT, SUCCESS, BAD_FLOW
	}

	// The activation context

	private String _conversationId = null;

	private Step _step = null;

	// The conversation and its contents

	@SessionState
	private Conversations _conversations;

	@Property
	private CreditRequest _creditRequest;

	// Other pages

	@InjectPage
	private WizardUsingFormFragmentsSuccess _successPage;

	@InjectPage
	private WizardUsingPagesBadFlow _badFlowPage;

	@InjectPage
	private Index _index;

	// Generally useful bits and pieces

	@Component(id = "form")
	private Form _form;

	// The code
	
	public void set(Step step, String conversationId) {
		_step = step;
		_conversationId = conversationId;
	}

	Object[] onPassivate() {
		return new Object[] { _step, _conversationId };
	}

	Object onActivate() {
		if (_step == null) {
			startConversation();
			_step = Step.START;
			return this;
		}
		return null;
	}

	Object onActivate(Step step, String conversationId) throws Exception {
		_step = step;
		_conversationId = conversationId;

		if (_step == null) {
			startConversation();
			_step = Step.START;
			return this;
		}

		// If the conversation does not contain the model
		// then it means the Back/Reload/Refresh button has been used to reach an old conversation,
		// so redirect to the bad-flow-step

		if (restoreCreditRequestFromConversation() == null) {
			return _badFlowPage;
		}

		return null;
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
			switch (_step) {
			case START:
				_creditRequest.validateAmountInfo();
				break;
			case APPLICANT:
				_creditRequest.validateApplicantInfo();
				break;
			case SUBMIT:
				// In the real world we would probably submit it to the business layer here
				// but we're not, so let's simulate a busy period then complete the request!

				sleep(5000);
				_creditRequest.complete();
				break;
			default:
				throw new IllegalStateException("Should not get here. _step = " + _step);
			}
		}
		catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a user-friendly message.
			_form.recordError(ExceptionUtil.getRootCause(e));
		}
	}

	Object onSuccess() {
		switch (_step) {
		case START:
			_step = Step.APPLICANT;
			return null;
		case APPLICANT:
			_step = Step.SUBMIT;
			return null;
		case SUBMIT:
			endConversation();

			// In the real world we would now save the credit request's id instead of these fields

			_successPage.set(_creditRequest.getAmount(), _creditRequest.getApplicantName());
			return _successPage;
		default:
			throw new IllegalStateException("Should not get here. _step = " + _step);
		}
	}

	void onPrev() {
		switch (_step) {
		case APPLICANT:
			_step = Step.START;
			break;
		case SUBMIT:
			_step = Step.APPLICANT;
			break;
		default:
			throw new IllegalStateException("Should not get here. _step = " + _step);
		}
	}

	void onRestart() {
		_step = null;
	}

	Object onQuit() {
		endConversation();
		return _index;
	}

	public void startConversation() {
		_conversationId = _conversations.startConversation(WIZARD_CONVERSATION_PREFIX);
		_creditRequest = new CreditRequest();
		saveCreditRequestToConversation(_creditRequest);
	}

	private void saveCreditRequestToConversation(CreditRequest creditRequest) {
		_conversations.saveToConversation(_conversationId, CREDIT_REQUEST_KEY, creditRequest);
	}

	private CreditRequest restoreCreditRequestFromConversation() {
		return (CreditRequest) _conversations.restoreFromConversation(_conversationId, CREDIT_REQUEST_KEY);
	}

	private void endConversation() {
		_conversations.endConversation(_conversationId);

		// If conversations ASO is now empty then remove it from the session

		if (_conversations.isEmpty()) {
			_conversations = null;
		}
	}

	public boolean isInEntrySteps() {
		return _step == Step.START || _step == Step.APPLICANT || _step == Step.SUBMIT;
	}

	public boolean isInStart() {
		return _step == Step.START;
	}

	public boolean isInApplicant() {
		return _step == Step.APPLICANT;
	}

	public boolean isInSubmit() {
		return _step == Step.SUBMIT;
	}

	public boolean isInBadFlow() {
		return _step == Step.BAD_FLOW;
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
