package jumpstart.web.commons;

import java.util.ArrayList;
import java.util.List;


public class Menu {
	private List<MenuOption> _menuOptions = new ArrayList<MenuOption>();

	public Menu() {
	}

	public void add(MenuOption menuOption) {
		_menuOptions.add(menuOption);
	};

	public List<MenuOption> getMenuOptions() {
		return _menuOptions;
	};
}
