package jumpstart.web.pages.examples.javascript;

import java.util.ArrayList;
import java.util.List;

import jumpstart.web.services.CountryNames;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class AutocompleteMixin {

	// Screen fields

	@SuppressWarnings("unused")
	@Property
	private String _countryName;
	
	// Generally useful bits and pieces

	@Inject
	private CountryNames _countryNames;

	// The code
	
	List<String> onProvideCompletionsFromCountryName(String partial) {
		List<String> matches = new ArrayList<String>();
		partial = partial.toUpperCase();

		for (String countryName : _countryNames.getSet()) {
			if (countryName.startsWith(partial)) {
				matches.add(countryName);
			}
		}

		return matches;
	}

}