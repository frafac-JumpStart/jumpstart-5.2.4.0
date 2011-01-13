package jumpstart.web.commons;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.services.Request;

/**
 * This class helps to add URL parameters to Links and extract URL parameters later from Requests.
 */
public class URLParameterHelper {
	static private final DateFormat ISO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	static public void addStringParameter(Link link, String name, String value) {
		if (value != null && !value.trim().equals("")) {
			link.addParameter(name, value);
		}
	}

	static public String getStringParameter(Request request, String name) {
		String value = request.getParameter(name);
		return value == null ? null : value;
	}

	static public void addLongParameter(Link link, String name, Long value) {
		if (value != null) {
			link.addParameter(name, value.toString());
		}
	}

	static public Long getLongParameter(Request request, String name) {
		String value = request.getParameter(name);
		return value == null ? null : Long.valueOf(value);
	}

	static public void addBooleanParameter(Link link, String name, Boolean value) {
		if (value != null) {
			link.addParameter(name, value.toString());
		}
	}

	static public Boolean getBooleanParameter(Request request, String name) {
		String value = request.getParameter(name);
		return value == null ? null : Boolean.valueOf(value);
	}

	static public void addDateParameter(Link link, String name, Date value) {
		if (value != null) {
			link.addParameter(name, ISO_DATE_FORMAT.format(value));
		}
	}

	static public Date getDateParameter(Request request, String name) {
		String value = request.getParameter(name);
		if (value != null) {
			try {
				Date d = ISO_DATE_FORMAT.parse(value);
				return d;
			}
			catch (ParseException e) {
				return null;
			}
		}
		return null;
	}

	static public <T extends Enum<T>> T getEnumParameter(Request request, String name, Class<T> enumType) {
		String value = request.getParameter(name);
		try {
			return value == null ? null : Enum.valueOf(enumType, value);
		}
		catch (Exception e) {
			return null;
		}
	}

	static public <T extends Enum<T>> void addEnumParameter(Link link, String name, Enum<T> value) {
		if (value != null) {
			link.addParameter(name, value.name());
		}
	}
}
