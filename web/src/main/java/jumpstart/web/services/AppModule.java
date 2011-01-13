package jumpstart.web.services;

import java.util.Arrays;
import java.util.HashSet;

import jumpstart.client.BusinessServicesLocator;
import jumpstart.client.BusinessServicesLocator2;
import jumpstart.client.IBusinessServicesLocator;
import jumpstart.client.IBusinessServicesLocator2;
import jumpstart.util.JodaTimeUtil;
import jumpstart.web.translators.YesNoTranslator;
import jumpstart.web.validators.Letters;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.Translator;
import org.apache.tapestry5.Validator;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.services.ClasspathURLConverter;
import org.apache.tapestry5.ioc.services.Coercion;
import org.apache.tapestry5.ioc.services.CoercionTuple;
import org.apache.tapestry5.services.BindingFactory;
import org.apache.tapestry5.services.BindingSource;
import org.apache.tapestry5.services.ComponentRequestFilter;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.apache.tapestry5.services.RequestFilter;
import org.joda.time.DateMidnight;
import org.joda.time.LocalDate;

/**
 * This module is automatically included as part of the Tapestry IoC Registry, it's a good place to configure and extend
 * Tapestry, or to place your own service definitions. See http://tapestry.apache.org/tapestry5/tapestry-ioc/module.html
 */
public class AppModule {

	// Add 2 services to those provided by Tapestry.
	// - IBusinessServicesLocator, IBusinessServicesLocator2 and CountryNames are used by pages which ask Tapestry
	// to @Inject them.

	public static void bind(ServiceBinder binder) {
		binder.bind(IBusinessServicesLocator.class, BusinessServicesLocator.class);
		binder.bind(IBusinessServicesLocator2.class, BusinessServicesLocator2.class);
		binder.bind(CountryNames.class);

		// This next line addresses an issue affecting Glassfish and JBoss - see http://blog.progs.be/?p=52
		javassist.runtime.Desc.useContextClassLoader = true;
	}

	// Tell Tapestry about our custom validators, translators, and their message files.
	// We do this by contributing to Tapestry's FieldValidatorSource service, TranslatorSource service,
	// and ValidationMessagesSource service.

	@SuppressWarnings("unchecked")
	public static void contributeFieldValidatorSource(MappedConfiguration<String, Validator> configuration) {
		configuration.add("letters", new Letters());
	}

	@SuppressWarnings("unchecked")
	public static void contributeTranslatorSource(Configuration<Translator> configuration) {
		configuration.add(new YesNoTranslator());
	}

	public void contributeValidationMessagesSource(OrderedConfiguration<String> configuration) {
		configuration.add("myValidationMessages", "jumpstart/web/validators/ValidationMessages");
		configuration.add("myTranslationMessages", "jumpstart/web/translators/TranslationMessages");
	}

	// Tell Tapestry which locales we support.
	// We do this by contributing to Tapestry's ApplicationDefaults service.

	public static void contributeApplicationDefaults(MappedConfiguration<String, String> configuration) {
		configuration.add(SymbolConstants.SUPPORTED_LOCALES, "en_US,en_GB,fr");
	}

	// Tell Tapestry how to handle a new binding prefix, "list:" (we use "list:" in at least one place, UserSearch.tml).
	// We do this by contributing to Tapestry's BindingSource service.
	// - Based on http://wiki.apache.org/tapestry/Tapestry5HowToAddBindingPrefix

	public static void contributeBindingSource(MappedConfiguration<String, BindingFactory> configuration,
			BindingSource bindingSource) {
		// Not necessary if using chenillekit because it also contributes a "list" binding prefix.
		// configuration.add("list", new ListBindingFactory(bindingSource));
	}

	// Tell Tapestry how to block access to WEB-INF/, META-INF/, and assets that are not in our "whitelist".
	// We do this by contributing a custom RequestFilter to Tapestry's RequestHandler service.
	// - This is necessary due to https://issues.apache.org/jira/browse/TAP5-815 .
	// - RequestHandler is shown in http://uli.spielviel.de/~uli/tapestry_request_processing.png .
	// - RequestHandler is described in http://tapestry.apache.org/tapestry5/guide/request.html
	// - Based on an entry in the Tapestry Users mailing list by martijn.list on 15 Aug 09.

	public void contributeRequestHandler(OrderedConfiguration<RequestFilter> configuration,
			PageRenderLinkSource pageRenderLinkSource) {
		final HashSet<String> ASSETS_WHITE_LIST = new HashSet<String>(Arrays.asList("jpg", "jpeg", "png", "gif", "js",
				"css", "ico"));
		configuration.add("AssetProtectionFilter", new AssetProtectionFilter(ASSETS_WHITE_LIST, pageRenderLinkSource),
				"before:*");
	}

	// Tell Tapestry how to prevent access to pages marked as @ProtectedPage unless logged in.
	// We do this by contributing a custom ComponentRequestFilter to Tapestry's ComponentRequestHandler service.
	// - ComponentRequestHandler is shown in http://uli.spielviel.de/~uli/tapestry_request_processing.png
	// - Based on http://tapestryjava.blogspot.com/2009/12/securing-tapestry-pages-with.html

	public void contributeComponentRequestHandler(OrderedConfiguration<ComponentRequestFilter> configuration) {
		configuration.addInstance("PageProtectionFilter", PageProtectionFilter.class);
	}

	// Tell Tapestry how to handle classpath URLs - we provide a converter to handle JBoss 5.
	// See http://wiki.apache.org/tapestry/HowToRunTapestry5OnJBoss5 .

	@SuppressWarnings("unchecked")
	public static void contributeServiceOverride(MappedConfiguration<Class, Object> configuration) {
		configuration.add(ClasspathURLConverter.class, new ClasspathURLConverterJBoss5());
	}

	// Tell Tapestry how to coerce Joda Time types to and from Java Date types
	// We do this by contributing to Tapestry's TypeCoercer service.
	// - Based on http://tapestry.apache.org/tapestry5/tapestry-ioc/coerce.html

	@SuppressWarnings("unchecked")
	public static void contributeTypeCoercer(Configuration<CoercionTuple> configuration) {

		// From java.util.Date to DateMidnight

		Coercion<java.util.Date, DateMidnight> toDateMidnight = new Coercion<java.util.Date, DateMidnight>() {
			public DateMidnight coerce(java.util.Date input) {
				// TODO - confirm this conversion always works, esp. across timezones
				return JodaTimeUtil.toDateMidnight(input);
			}
		};

		configuration.add(new CoercionTuple<java.util.Date, DateMidnight>(java.util.Date.class, DateMidnight.class,
				toDateMidnight));

		// From DateMidnight to java.util.Date

		Coercion<DateMidnight, java.util.Date> fromDateMidnight = new Coercion<DateMidnight, java.util.Date>() {
			public java.util.Date coerce(DateMidnight input) {
				// TODO - confirm this conversion always works, esp. across timezones
				return JodaTimeUtil.toJavaDate(input);
			}
		};

		configuration.add(new CoercionTuple<DateMidnight, java.util.Date>(DateMidnight.class, java.util.Date.class,
				fromDateMidnight));

		// From java.util.Date to LocalDate

		Coercion<java.util.Date, LocalDate> toLocalDate = new Coercion<java.util.Date, LocalDate>() {
			public LocalDate coerce(java.util.Date input) {
				// TODO - confirm this conversion always works, esp. across timezones
				return JodaTimeUtil.toLocalDate(input);
			}
		};

		configuration.add(new CoercionTuple<java.util.Date, LocalDate>(java.util.Date.class, LocalDate.class,
				toLocalDate));

		// From LocalDate to java.util.Date

		Coercion<LocalDate, java.util.Date> fromLocalDate = new Coercion<LocalDate, java.util.Date>() {
			public java.util.Date coerce(LocalDate input) {
				// TODO - confirm this conversion always works, esp. across timezones
				return JodaTimeUtil.toJavaDate(input);
			}
		};

		configuration.add(new CoercionTuple<LocalDate, java.util.Date>(LocalDate.class, java.util.Date.class,
				fromLocalDate));

	}

}
