package jumpstart.web.commons;

public class MenuOption {
	private String _label;
	private String _page;

	public MenuOption(String label, String page) {
		_label = label;
		_page = page;
	}

	public String getLabel() {
		return _label;
	};
	
	public String getPage() {
		return _page;
	};
}
