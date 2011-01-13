package jumpstart.web.pages.examples.wizard;

import java.util.Collection;

import jumpstart.web.base.examples.wizard.WizardConversationalPage;
import jumpstart.web.commons.Conversation;
import jumpstart.web.commons.Conversations;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;

public class ConversationsList {

	// Screen fields

	@Property
	private Conversation _conversation;

	// Other pages

	@InjectPage
	private WizardUsingPages1 _creditRequestsWizard;

	// Generally useful bits and pieces

	@SessionState
	private Conversations _conversations;

	// The code

	Object onActionFromGo(String conversationId) throws Exception {
		Conversation conversation = _conversations.get(conversationId);

		if (conversation != null) {
			// We know of 1 type of conversation only - it belongs to the credit requests wizard
			if (conversation.getObject(WizardConversationalPage.CREDIT_REQUEST_KEY) != null) {
				_creditRequestsWizard.set(conversationId);
				return _creditRequestsWizard;
			}
		}

		return null;
	}

	public Object getObject() throws Exception {

		if (_conversation != null) {
			// We know of 1 type of conversation only - it belongs to the credit requests wizard
			Object object = _conversation.getObject(WizardConversationalPage.CREDIT_REQUEST_KEY);
			return object;
		}

		return null;
	}

	public Collection<Conversation> getAllConversations() {
		return _conversations.getAll();
	}
}
