package jumpstart.web.pages.examples.styling;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class SwitchableStylesheets {

	// The activation context

	private int _styleNum;
	
	// Screen fields

	@SuppressWarnings("unused")
	@Persist(PersistenceConstants.FLASH)
	@Property
	private String _firstName;

	@SuppressWarnings("unused")
	@Persist(PersistenceConstants.FLASH)
	@Property
	private String _lastName;

	// Generally useful bits and pieces

	@Inject
	@Path("context:css/examples/examples.css")
	private Asset _stylesheet0;

	@Inject
	@Path("context:css/examples/switched.css")
	private Asset _stylesheet1;
	
	// The code
	
	int onPassivate() {
		return _styleNum;
	}
	
	void onActivate(int styleNum) {
		_styleNum = styleNum;
	}
	
	void onSuccess() {
		_styleNum = (_styleNum + 1) % 2;
	}
	
	public Asset getStylesheet() {
		switch (_styleNum) {
		case 0:
			return _stylesheet0;
		case 1:
			return _stylesheet1;
		default:
			return _stylesheet0;
		}
	}
}
