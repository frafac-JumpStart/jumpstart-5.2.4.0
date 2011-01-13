package jumpstart.web.pages.examples.input;

import jumpstart.web.pages.examples.input.MultipleForms2.SearchType;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;

public class MultipleForms1 {

	// Screen fields

	@Property
	private String _customerName;

	@Property
	private String _supplierName;

	// Other pages
	
	@InjectPage
	private MultipleForms2 _page2;

	// The code
	
	void onPrepareFromSearchCustomers() {
		// Any setting up of editable objects or fields on this form should be done in here.
	}

	void onPrepareFromSearchSuppliers() {
		// Any setting up of editable objects or fields on this form should be done in here.
	}

	Object onSuccessFromSearchCustomers() {
		_page2.set(SearchType.CUSTOMERS, _customerName);
		return _page2;
	}

	Object onSuccessFromSearchSuppliers() {
		_page2.set(SearchType.SUPPLIERS, _supplierName);
		return _page2;
	}
}
