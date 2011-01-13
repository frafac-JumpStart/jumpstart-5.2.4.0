package jumpstart.web.pages.examples.state;

import jumpstart.web.pages.Index;
import jumpstart.web.state.examples.state.ShoppingBasket;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;

public class SharingAcrossMultiplePages2 {

	// Screen fields

	@SuppressWarnings("unused")
	@SessionState
	@Property
	private ShoppingBasket _myBasket;
	
	// The code

	Object onGoHome() {
		// Delete the ASO from the session
		_myBasket = null;
		return Index.class;
	}

}
