package jumpstart.web.pages.examples.navigation;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;

public class EventLinks {

	// Use "flash" persistence to keep the message only until it's been displayed once.
	
	@Persist(PersistenceConstants.FLASH)
	private String _hiMessage;

	void onSayHi(String name) {
		_hiMessage = "Hi, " + name + ".";
	}

	public String getHiMessage() {
		return _hiMessage;
	}
	
}