package jumpstart.web.components.examples.layoutwithmenu;

import jumpstart.web.annotation.ProtectedPage;
import jumpstart.web.commons.Menu;
import jumpstart.web.commons.MenuOption;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

@ProtectedPage
public class Layout {
	
	// Parameters
	
	@SuppressWarnings("unused")
	@Parameter
	@Property
	private String _title = "";

	@SuppressWarnings("unused")
	@Parameter
	@Property
	private String _chosenOption = "";
	
	private Menu _menu;

	// The code
	
	public Menu getMenu() {
		
		if (_menu == null) {
			_menu = new Menu();
			_menu.add(new MenuOption("Page 1", "examples/layoutwithmenu/Page1"));
			_menu.add(new MenuOption("Page 2", "examples/layoutwithmenu/Page2"));
			_menu.add(new MenuOption("Page 3", "examples/layoutwithmenu/Page3"));
		}
		
		return _menu;
	}

}
