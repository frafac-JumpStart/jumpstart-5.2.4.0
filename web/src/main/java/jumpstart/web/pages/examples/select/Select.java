package jumpstart.web.pages.examples.select;

import org.apache.tapestry5.annotations.Property;

public class Select {

	@Property
	private String _color0;

	@Property
	private Color1 _color1;

	public enum Color1 {
		RED, GREEN, BLUE
	}

	@Property
	private String _color2;

	@Property
	private Color3 _color3;

	public enum Color3 {
		YELLOW, CYAN, MAGENTA
	}

	Object[] onPassivate() {
		return new Object[] { _color0, _color1, _color2, _color3 };
	}

	void onActivate(String color0, Color1 color1, String color2, Color3 color3) {
		_color0 = color0;
		_color1 = color1;
		_color2 = color2;
		_color3 = color3;
	}
}
