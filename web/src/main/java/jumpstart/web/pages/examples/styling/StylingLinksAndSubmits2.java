package jumpstart.web.pages.examples.styling;

public class StylingLinksAndSubmits2 {

	private String _yourChoice;
	
	public void set(String yourChoice) {
		_yourChoice = yourChoice;
	}
	
	String onPassivate() {
		return _yourChoice;
	}

	void onActivate(String yourChoice) {
		_yourChoice = yourChoice;
	}

	public String getYourChoice() {
		return _yourChoice;
	}
}
