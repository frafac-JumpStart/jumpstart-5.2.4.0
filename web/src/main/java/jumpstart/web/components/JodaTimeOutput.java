// This is based on Tapestry's Output component.

package jumpstart.web.components;

import java.text.Format;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.services.ComponentDefaultProvider;
import org.joda.time.base.AbstractInstant;
import org.joda.time.base.AbstractPartial;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * A component for formatting for output of JodaTime objects. It supports subclasses of AbstractInstant and
 * AbstractPartial. If the component is represented in the template using an element, then the element (plus any
 * informal parameters) will be output around the formatted value.
 */
@SupportsInformalParameters
public class JodaTimeOutput {
	/**
	 * The value to be output (before formatting). If the formatted value is blank, no output is produced.
	 */
	@Parameter(required = true)
	private Object _value;

	/** The format to be applied to the object. */
	@Parameter(required = false)
	private DateTimeFormatter _formatter;

	/** The format to be applied to the object. */
	@Parameter(required = false, defaultPrefix = BindingConstants.LITERAL)
	private String _style;

	/** This is declared so we catch slip-ups - an error will point the developer to _formatter instead. */
	@Parameter(required = false, defaultPrefix = BindingConstants.LITERAL)
	private Format _format;

	/**
	 * The element name, derived from the component template. This can even be overridden manually if desired (for
	 * example, to sometimes render a surrounding element and other times not).
	 */
	@Parameter("componentResources.elementName")
	private String _elementName;

	@Inject
	private ComponentDefaultProvider _defaultProvider;

	@Inject
	private ComponentResources _resources;

	Binding defaultValue() {
		return _defaultProvider.defaultBinding("value", _resources);
	}

	void setupRender() {

		if (_format != null) {
			throw new IllegalArgumentException(
					"JodaTimeOutput does not allow \"format\" parameter.  Valid parameters are \"style\" and \"formatter\".  Formatter type is DateTimeFormatter.");
		}

		if ((_style != null) && (_formatter != null)) {
			throw new IllegalArgumentException(
					"JodaTimeOutput can optionally receive \"style\" parameter or \"formatter\" parameter, but not both.  Received style=\""
							+ _style + "\" and formatter=" + _formatter + ".");
		}

	}

	boolean beginRender(MarkupWriter writer) {

		String formatted = (_value == null ? "" : format(_value));

		if (InternalUtils.isNonBlank(formatted)) {
			if (_elementName != null) {
				writer.element(_elementName);

				_resources.renderInformalParameters(writer);
			}

			writer.write(formatted);

			if (_elementName != null)
				writer.end();
		}

		return false;
	}

	private String format(Object value) {
		String formatted = "";

		if (value != null) {

			// If value is an AbstractInstant - includes DateTime and DateMidnight

			if (value instanceof AbstractInstant) {
				AbstractInstant ai = ((AbstractInstant) value);
				if (_style != null) {
					formatted = DateTimeFormat.forStyle(_style).print(ai);
				}
				else if (_formatter != null) {
					formatted = ai.toString(_formatter);
				}
				else {
					formatted = value.toString();
				}
			}

			// Else if value is an AbstractPartial - includes LocalDate, LocalTime,
			// LocalDateTime, YearMonthDay, and TimeOfDay

			else if (value instanceof AbstractPartial) {
				AbstractPartial ap = ((AbstractPartial) value);
				if (_style != null) {
					formatted = DateTimeFormat.forStyle(_style).print(ap);
				}
				else if (_formatter != null) {
					formatted = ap.toString(_formatter);
				}
				else {
					formatted = value.toString();
				}
			}

			// Else value is an unsupported type

			else {
				throw new IllegalArgumentException(
						"JodaTimeOutput received a value of the wrong type.  Supported types are subclasses of AbstractInstant and AbstractPartial.  Type found is "
								+ value.getClass().getName() + ".");
			}
		}

		return formatted;
	}

	// For testing.

	void setup(Object value, DateTimeFormatter formatter, String style, String elementName, ComponentResources resources) {
		_value = value;
		_formatter = formatter;
		_style = style;
		_elementName = elementName;
		_resources = resources;
	}
}
