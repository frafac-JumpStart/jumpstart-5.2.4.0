package jumpstart.web.pages.examples.localization;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jumpstart.web.commons.LocalesUtil;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.PersistentLocale;

public class LocalizationByMessageCatalog {

	// Screen fields

	@Inject
	@Property
	private Locale _currentLocale;

	@SuppressWarnings("unused")
	@Property
	private Date _date;

	@SuppressWarnings("unused")
	@Property
	private double _number = 1234.56;

	@SuppressWarnings("unused")
	@Property
	private DateFormat _dateFormat;

	@SuppressWarnings("unused")
	@Property
	private NumberFormat _numberFormat;

	@SuppressWarnings("unused")
	@Property
	private List<Locale> _supportedLocales;

	@SuppressWarnings("unused")
	@Property
	private Locale _supportedLocale;

	// Generally useful bits and pieces

	@Inject
	private PersistentLocale _persistentLocaleService;

	@Inject
	@Symbol("tapestry.supported-locales")
	private String _supportedLocalesString;
	
	// The code

	void setupRender() {
		_supportedLocales = LocalesUtil.convertToLocales(_supportedLocalesString);
		_date = new Date();
		_dateFormat = DateFormat.getDateInstance(DateFormat.LONG, _currentLocale);
		_numberFormat = NumberFormat.getInstance(_currentLocale);
	}

	void onActionFromSwitchLocale(String localeCode) {
		_persistentLocaleService.set(LocalesUtil.convertToLocale(localeCode));
	}
}