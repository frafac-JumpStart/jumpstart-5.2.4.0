package jumpstart.web.components;

import java.text.DateFormat;
import java.util.Date;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Parameter;
import org.joda.time.LocalDate;

/**
 * A date picker component built for LocalDate, implemented as a wrapper around Tapestry's DateField component.
 * DEPRECATED in favour of type coercions.
 */
@Deprecated
public class LocalDateField {

	@Parameter(required = true, principal = true, autoconnect = true)
	private LocalDate _value;

    @Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
	private DateFormat _format;

    public Date getDate() {
		// TODO - confirm this conversion always works, esp. across timezones
		Date date = (_value == null ? null : new Date(_value.toDateTimeAtStartOfDay().getMillis()));
		return date;
	}

	public void setDate(Date date) {
		// TODO - confirm this conversion always works, esp. across timezones
		_value = date == null ? null : LocalDate.fromDateFields(date);
	}

	public DateFormat getFormat() {
		return _format;
	}

	public void setFormat(DateFormat format) {
		_format = format;
	}
}
