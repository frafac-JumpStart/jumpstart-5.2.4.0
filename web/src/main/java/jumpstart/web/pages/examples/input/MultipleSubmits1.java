package jumpstart.web.pages.examples.input;

import jumpstart.web.pages.examples.input.MultipleSubmits2.SearchType;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;

public class MultipleSubmits1 {

	// Screen fields

	@Property
	private String _name;

	// Other pages
	
	@InjectPage
	private MultipleSubmits2 _page2;
	
	// Generally useful bits and pieces

	private SearchType _searchType;

	// The code
	
	void onActivate() {
		_searchType = SearchType.CUSTOMERS;
	}
	
	void onSelectedFromSuppliers() {
		_searchType = SearchType.SUPPLIERS;
	}

	Object onSuccess() {
		_page2.set(_searchType, _name);
		return _page2;
	}
}
