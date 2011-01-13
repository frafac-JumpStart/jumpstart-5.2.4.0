package jumpstart.web.base.examples.wizard;

import jumpstart.web.commons.Conversations;
import jumpstart.web.pages.examples.wizard.WizardUsingPagesBadFlow;
import jumpstart.web.state.examples.wizard.CreditRequest;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.SessionState;

public class WizardConversationalPage {
	public static final String WIZARD_CONVERSATION_PREFIX = "wiz";
	public static final String CREDIT_REQUEST_KEY = "CR";

	// The conversation

	@SessionState
	private Conversations _conversations;

	private String _conversationId = null;

	// Other pages

	@InjectPage
	private WizardUsingPagesBadFlow _badFlowPage;

	// The code

	public void set(String conversationId) {
		_conversationId = conversationId;
	}

	String onPassivate() {
		return _conversationId;
	}

	Object onActivate() throws Exception {
		if (getConversationId() == null) {
			startConversation();
			return this;
		}
		return null;
	}

	protected Object onActivate(String conversationId) throws Exception {
		_conversationId = conversationId;

		// If the conversation does not contain the model
		// then it means the Back/Reload/Refresh button has been used to reach an old conversation,
		// so redirect to the bad-flow-step

		if (restoreCreditRequestFromConversation() == null) {
			return _badFlowPage;
		}

		return null;
	}

	protected void startConversation() {
		_conversationId = _conversations.startConversation(WIZARD_CONVERSATION_PREFIX);
	}

	protected void saveCreditRequestToConversation(CreditRequest creditRequest) {
		_conversations.saveToConversation(_conversationId, CREDIT_REQUEST_KEY, creditRequest);
	}

	protected CreditRequest restoreCreditRequestFromConversation() {
		return (CreditRequest) _conversations.restoreFromConversation(_conversationId, CREDIT_REQUEST_KEY);
	}

	protected void endConversation() {
		_conversations.endConversation(_conversationId);

		// If conversations ASO is now empty then remove it from the session

		if (_conversations.isEmpty()) {
			_conversations = null;
		}
	}

	protected String getConversationId() {
		return _conversationId;
	}
}
