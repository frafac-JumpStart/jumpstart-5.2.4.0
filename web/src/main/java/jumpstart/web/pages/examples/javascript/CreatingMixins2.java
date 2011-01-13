package jumpstart.web.pages.examples.javascript;

import jumpstart.web.state.examples.javascript.MyOrder;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;

public class CreatingMixins2 {

	// Screen fields

	@SuppressWarnings("unused")
	@SessionState
	@Property
	private MyOrder _myOrder;

}
