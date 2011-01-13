package jumpstart.web.pages.examples.javascript;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

public class AJAXSelect2 {

	// Screen fields

	@SuppressWarnings("unused")
	@Persist(PersistenceConstants.FLASH)
	@Property
	private String _carMake;

	@SuppressWarnings("unused")
	@Persist(PersistenceConstants.FLASH)
	@Property
	private String _carModel;

	@SuppressWarnings("unused")
	@Persist(PersistenceConstants.FLASH)
	@Property
	private String _keyWords;

	// The code

	// set() is public so that other pages can use it to set up this page.

	public void set(String carMake, String carModel, String keyWords) {
		_carMake = carMake;
		_carModel = carModel;
		_keyWords = keyWords;
	}
}
