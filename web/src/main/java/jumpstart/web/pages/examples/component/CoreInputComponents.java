package jumpstart.web.pages.examples.component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.internal.services.StringValueEncoder;

public class CoreInputComponents {

	/* Checkbox */

	@SuppressWarnings("unused")
	@Property
	@Persist
	private boolean _checkboxValue;

	/* DateField */

	@Property
	@Persist
	private Date _dateValue;

	// We could return a DateFormat, but instead we'll return a String which DateField will coerce into a DateFormat.
	public String getDateFieldFormat() {
		return "dd/MM/yyyy";
	}

	/* Palette */

	@Property
	@Persist
	private List<String> _paletteSelectedValues;

	@SuppressWarnings("unused")
	@Property
	private final String[] PETS = { "Dog", "Cat", "Parrot", "Mouse" };

	/* Password */

	@SuppressWarnings("unused")
	@Property
	@Persist
	private String _passwordValue;

	/* RadioGroup and Radio */

	@SuppressWarnings("unused")
	@Property
	@Persist
	private String _radioSelectedValue;

	@SuppressWarnings("unused")
	@Property
	private StringValueEncoder _stringEncoder = new StringValueEncoder();

	/* Select */

	@SuppressWarnings("unused")
	@Property
	@Persist
	private String _selectedValue;

	/* TextArea */

	@SuppressWarnings("unused")
	@Property
	@Persist
	private String _textAreaValue;

	/* TextField */

	@SuppressWarnings("unused")
	@Property
	@Persist
	private String _textValue;

	/* Life-cycle stuff.  Fields that are marked @Persist MUST be initialized here rather than where they are declared. */

	void setupRender() {
		if (_dateValue == null) {
			_dateValue = new Date();
		}
		if (_paletteSelectedValues == null) {
			_paletteSelectedValues = new ArrayList<String>();
		}
	}

}