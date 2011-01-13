package jumpstart.web.pages.examples.input;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;

public class Translators1 {

	// Screen fields

	@Property
	private Integer _integerField;

	@Property
	private Long _longField;

	@Property
	private Double _doubleField;

	@Property
	private String _stringField;

	// Other pages

	@InjectPage
	private Translators2 _page2;

	// The code

	Object onSuccess() {
		_page2.set(_integerField, _longField, _doubleField, _stringField);
		return _page2;
	}
}
