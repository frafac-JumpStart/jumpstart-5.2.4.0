package jumpstart.web.pages.examples.state;

import jumpstart.web.services.CountryNames;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class SharingAcrossTheApplication {

	// Screen fields

	@SuppressWarnings("unused")
	@Property
	@Inject
	private CountryNames _countryNames;

}
