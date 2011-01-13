package jumpstart.web.components.examples.layoutwithmenu;

import jumpstart.web.commons.Menu;
import jumpstart.web.commons.MenuOption;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

public class MenuBar {
	private static final String CHOSEN_OPTION_CSS_CLASS = "chosenOption";

	// Parameters
	
	@Parameter(required = true, allowNull = false)
	@SuppressWarnings("unused")
	@Property
	private Menu _menu;
	
	@Parameter
	private String _chosenOption = "";
	
	// Screen fields
	
	@Property
	private MenuOption _menuOption;
	
	// The code
	
	public String getCSSClass() {
		return _menuOption.getLabel().equals(_chosenOption) ? CHOSEN_OPTION_CSS_CLASS : "";
	}

}
