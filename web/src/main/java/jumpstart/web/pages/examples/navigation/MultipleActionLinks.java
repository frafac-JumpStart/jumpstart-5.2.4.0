package jumpstart.web.pages.examples.navigation;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;

public class MultipleActionLinks {

	// Use "flash" persistence to keep the message only until it's been displayed once.
	
	@Persist(PersistenceConstants.FLASH)
	private String _hiMessage;

	@Persist(PersistenceConstants.FLASH)
	private String _helloMessage;

	void onActionFromSayHi(String name) {
		_hiMessage = "Hi, " + name + ".";
	}

	void onActionFromSayHello(String name) {
		_helloMessage = "Hello, " + name + ".";
	}

	public String getHiMessage() {
		return _hiMessage;
	}
	
	public String getHelloMessage() {
		return _helloMessage;
	}
	
}