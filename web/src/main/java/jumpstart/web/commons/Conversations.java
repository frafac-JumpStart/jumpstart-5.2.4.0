package jumpstart.web.commons;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Conversations {

	private Map<String, Integer> _counters = new HashMap<String, Integer>();
	private Map<String, Conversation> _conversations = new HashMap<String, Conversation>();

	public String startConversation() {
		return startConversation("dEfAuLt");
	}

	public synchronized String startConversation(String conversationIdPrefix) {
		int conversationNumber = incrementCounter(conversationIdPrefix);
		String conversationId = conversationIdPrefix + Integer.toString(conversationNumber);

		startConversationForId(conversationId);
		
		return conversationId;
	}

	public synchronized void startConversationForId(String conversationId) {
		Conversation conversation = new Conversation(conversationId);
		add(conversation);
	}

	public void saveToConversation(String conversationId, Object key, Object value) {
		Conversation conversation = get(conversationId);
		// Save a new reference to the object, just in case Tapestry cleans up the other one as we leave the page.
		Object valueNewRef = value;
		conversation.setObject(key, valueNewRef);
	}

	public Object restoreFromConversation(String conversationId, Object key) {
		Conversation conversation = get(conversationId);
		return conversation == null ? null : conversation.getObject(key);
	}

	public void endConversation(String conversationId) {
		remove(conversationId);
	}

	public Collection<Conversation> getAll() {
		return _conversations.values();
	}

	public boolean isEmpty() {
		return _conversations.isEmpty();
	}

	private synchronized void add(Conversation conversation) {
		if (_conversations.containsKey(conversation.getId())) {
			throw new IllegalArgumentException("Conversation already exists. conversationId = " + conversation.getId());
		}
		_conversations.put(conversation.getId(), conversation);
	}

	public Conversation get(String conversationId) {
		return _conversations.get(conversationId);
	}

	private void remove(String conversationId) {
		Object obj = _conversations.remove(conversationId);
		if (obj == null) {
			throw new IllegalArgumentException("Conversation did not exist. conversationId = " + conversationId);
		}
	}

	public synchronized int incrementCounter(String counterKey) {

		if (_counters == null) {
			_counters = new HashMap<String, Integer>(2);
		}
		
		Integer counterValue = _counters.get(counterKey);
		
		if (counterValue == null) {
			counterValue = 1;
		}
		else {
			counterValue++;
		}
		
		_counters.put(counterKey, counterValue);
		return counterValue;
	}
	
	public String toString() {
		final String DIVIDER = ", ";

		StringBuffer buf = new StringBuffer();
		buf.append(this.getClass().getSimpleName() + ": ");
		buf.append("[ ");
		buf.append("counters=");
		if (_counters == null) {
			buf.append("null");
		}
		else {
			buf.append("{");
			for (Iterator<String> iterator = _counters.keySet().iterator(); iterator.hasNext();) {
				String key = (String) iterator.next();
				buf.append("(" + key + ", " + _counters.get(key) + ")");
			}
			buf.append("}");
		}
		buf.append(DIVIDER);
		buf.append("conversations=");
		if (_conversations == null) {
			buf.append("null");
		}
		else {
			buf.append("{");
			for (Iterator<String> iterator = _conversations.keySet().iterator(); iterator.hasNext();) {
				String key = (String) iterator.next();
				buf.append("(" + key + ", " + _conversations.get(key) + ")");
			}
			buf.append("}");
		}
		buf.append("]");
		return buf.toString();
	}

}
