package jumpstart.web.pages.examples.input;

import org.apache.tapestry5.annotations.Property;

public class Translators2 {

	// Screen fields

	@Property(write = false)
	private Integer _integerField;

	@Property(write = false)
	private Long _longField;

	@Property(write = false)
	private Double _doubleField;

	@Property(write = false)
	private String _stringField;

	// The code
	
	public void set(Integer integerField, Long longField, Double doubleField, String stringField) {
		_integerField = integerField;
		_longField = longField;
		_doubleField = doubleField;
		_stringField = stringField;
	}

	Object[] onPassivate() {
		return new Object[] { _integerField, _longField, _doubleField, _stringField };
	}

	void onActivate(Integer integerField, Long longField, Double doubleField, String stringField) {
		_integerField = integerField;
		_longField = longField;
		_doubleField = doubleField;
		_stringField = stringField;
	}
}
