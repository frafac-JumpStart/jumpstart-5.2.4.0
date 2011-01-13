package jumpstart.web.pages.examples.input;


public class MultipleSubmits2 {
	
	// The activation context

	private SearchType _searchType;

	private String _name;

	// Generally useful bits and pieces

	public enum SearchType {
		CUSTOMERS, SUPPLIERS;
	}

	// The code
	
	// set() is public so that other pages can use it to set up this page.
	
	public void set(SearchType searchType, String lastName) {
		_searchType = searchType;
		_name = lastName;
	}
	
	// onPassivate() is called by Tapestry to get the activation context to put in the URL.
	
	Object[] onPassivate() {
		return new Object[] { _searchType, _name };
	}

	// onActivate() is called by Tapestry to pass in the activation context from the URL.

	void onActivate(SearchType searchType, String name) {
		_searchType = searchType;
		_name = name;
	}

	public String getYourSearch() {
		if (_searchType == SearchType.CUSTOMERS) {
			return "You chose to search Customers with \"" + _name + "\".";
		}
		else {
			return "You chose to search Suppliers with \"" + _name + "\".";
		}
	}
}
