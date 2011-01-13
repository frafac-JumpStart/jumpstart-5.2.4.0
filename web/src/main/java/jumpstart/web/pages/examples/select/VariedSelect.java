package jumpstart.web.pages.examples.select;

import org.apache.tapestry5.annotations.Property;

public class VariedSelect {

	@Property
	private String _color0;

	@Property
	private String _color1;

	@Property
	private String _color2;

	@Property
	private String _color3;
	
	String[] onPassivate() {
		return new String[] { _color0, _color1, _color2, _color3 };
	}

	void onActivate(String color0, String color1, String color2, String color3) {
		_color0 = color0;
		_color1 = color1;
		_color2 = color2;
		_color3 = color3;
	}
}