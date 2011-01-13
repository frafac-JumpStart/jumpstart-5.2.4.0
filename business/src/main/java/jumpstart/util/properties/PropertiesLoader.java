/*
 * Created on May 9, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jumpstart.util.properties;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesLoader {
	public static final Logger LOGGER = LoggerFactory.getLogger(PropertiesLoader.class);

	private String _resourceName;
	private URL _resourceURL;
	private Properties _properties;
	InputStream _inputStream;

	public PropertiesLoader(String resourceName) {
		_resourceName = resourceName;
		_resourceURL = null;
		_properties = new Properties();

		// try classpath
		_inputStream = this.getClass().getResourceAsStream("/" + resourceName);

		if (_inputStream != null) {
			_resourceURL = this.getClass().getResource("/" + _resourceName);
		}
		else {
			// try local
			_inputStream = this.getClass().getResourceAsStream(resourceName);

			if (_inputStream != null) {
				_resourceURL = this.getClass().getResource(_resourceName);
			}
			else {
				throw new IllegalStateException("Failed to load properties resource \"" + _resourceName
						+ "\". It should be on the classpath.  If using JBoss, put the \"" + _resourceName
						+ "\" file in the server's conf/ directory.");
			}
		}
		try {
			_properties.load(_inputStream);
		}
		catch (java.io.IOException e) {
			e.printStackTrace();
			throw new IllegalStateException("Failed to load properties resource \"" + _resourceName
					+ "\". It should be on the classpath.  If using JBoss, put the \"" + _resourceName
					+ "\" file in the server's conf/ directory.", e);
		}
	}

	public Properties getProperties() {
		return _properties;
	}

	public String getOptionalProperty(String propertyName) {
		String propertyValue = _properties.getProperty(propertyName);
		return propertyValue;
	}

	public String getMandatoryProperty(String propertyName) {
		String propertyValue = getOptionalStringProperty(propertyName);

		if (propertyValue == null) {
			throw new PropertyMissingException(propertyName, _resourceName);
		}

		if (propertyValue.trim().equals("")) {
			throw new PropertyEmptyException(propertyName, _resourceName);
		}

		return propertyValue;
	}

	public String getOptionalStringProperty(String propertyName) {
		return getOptionalProperty(propertyName);
	}

	public String getMandatoryStringProperty(String propertyName) {
		return getMandatoryProperty(propertyName);
	}

	public String getMandatoryStringProperty(String propertyName, String[] allowableValues) {
		String propertyValue = getMandatoryProperty(propertyName);

		boolean valid = false;
		for (int i = 0; i < allowableValues.length; i++) {
			if (propertyValue.equals(allowableValues[i])) {
				valid = true;
				break;
			}
		}

		if (!valid) {
			throw new PropertyValueException(propertyName, _resourceName, propertyValue, allowableValues);
		}

		return propertyValue;
	}

	public Boolean getOptionalBooleanProperty(String propertyName) {
		String propertyValue = getOptionalProperty(propertyName);

		if (propertyValue == null) {
			return null;
		}
		else if (propertyValue.equals("true")) {
			return true;
		}
		else if (propertyValue.equals("false")) {
			return false;
		}
		else {
			throw new PropertyValueException(propertyName, _resourceName, propertyValue, new String[] { "", "true",
					"false" });
		}
	}

	public boolean getMandatoryBooleanProperty(String propertyName) {
		String propertyValue = getMandatoryProperty(propertyName);

		if (propertyValue.equals("true")) {
			return true;
		}
		else if (propertyValue.equals("false")) {
			return false;
		}
		else {
			throw new PropertyValueException(propertyName, _resourceName, propertyValue,
					new String[] { "true", "false" });
		}
	}

	public int getMandatoryIntProperty(String propertyName) {
		String propertyValue = getMandatoryProperty(propertyName);

		try {
			int i = Integer.parseInt(propertyValue);
			return i;
		}
		catch (NumberFormatException e) {
			throw new PropertyValueException(propertyName, _resourceName, propertyValue, " an integer.");
		}
	}

	public void store(String headers) throws IOException {
		try {
			FileOutputStream out = new FileOutputStream(_resourceURL.getFile());
			_properties.store(out, headers);
		}
		catch (IOException e) {
			LOGGER.error("Could not store properties to resource \"" + _resourceURL + "\" because: " + e);
			throw e;
		}
	}

	public Object setProperty(String key, String value) {
		return _properties.setProperty(key, value);
	}

	public Object setProperty(String key, int value) {
		final DecimalFormat df = new DecimalFormat();
		return _properties.setProperty(key, df.format(value));
	}

}
