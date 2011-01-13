package jumpstart.web.pages.examples.jodatime;

import java.util.Locale;

import jumpstart.business.domain.examples.DateStuff;
import jumpstart.business.domain.examples.iface.IDateStuffServiceLocal;
import jumpstart.client.IBusinessServicesLocator;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class JodaTime1 {

	// Screen fields

	@Property
	private DateStuff _dateStuff;
	
	@SuppressWarnings("unused")
	@Property
	private DateTimeFormatter _french;
	
	@SuppressWarnings("unused")
	@Property
	private DateTimeFormatter _ISODate;
	
	// Generally useful bits and pieces

	@Inject
	private IBusinessServicesLocator _businessServicesLocator;

	// The code
	
	void setupRender() {

		// Ask business service to find DateStuff with id = 1.

		_dateStuff = getDateStuffService().findDateStuff(1L);

		if (_dateStuff == null) {
			throw new IllegalStateException("Database data has not been set up!");
		}

		_french = DateTimeFormat.fullDate().withLocale(Locale.FRENCH);
		_ISODate = ISODateTimeFormat.date();
	}

	private IDateStuffServiceLocal getDateStuffService() {
		return _businessServicesLocator.getDateStuffServiceLocal();
	}
}