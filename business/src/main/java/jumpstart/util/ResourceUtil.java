package jumpstart.util;

import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Insert the type's description here. Creation date: (26/02/01 22:22:21)
 * 
 * @author: Administrator
 */
public class ResourceUtil {
	static private final Logger LOGGER = LoggerFactory.getLogger(ResourceUtil.class);
	static private ResourceUtil _myClass = null;

	private ResourceUtil() {
	}

	static public Properties getAsProperties(String resourceName) throws UtilRuntimeException {
		return getAsProperties(resourceName, Thread.currentThread().getContextClassLoader());
	}

	static public Properties getAsProperties(String resourceName, ClassLoader classLoader) throws UtilRuntimeException {

		Properties p;

		if (resourceName == null) {
			throw new IllegalArgumentException("resourceName is null");
		}
		if (_myClass == null) {
			_myClass = new ResourceUtil();
		}

		InputStream resourceStream = null;
		try {
			// Try local

			resourceStream = classLoader.getResourceAsStream("/" + resourceName);

			// If not found, try classpath

			if (resourceStream == null) {
				resourceStream = classLoader.getResourceAsStream(resourceName);
			}
			
			// If not found, then get out

			if (resourceStream == null) {
				LOGGER.error("Could not load properties from resource \"" + resourceName + "\".  Check the classpath.");
				System.err.println("Could not load properties from resource \"" + resourceName
						+ "\".  Check the classpath.");
				throw new UtilRuntimeException("Could not find resource \"" + resourceName
						+ "\".  Check the classpath.");
			}
			
			// Load the properties!

			p = new Properties();
			p.load(resourceStream);
		}
		catch (UtilRuntimeException e) {
			throw e;
		}
		catch (Exception e) {
			throw new UtilRuntimeException("Could not load properties from resource \"" + resourceName
					+ "\".  Check the classpath.", e);
		}
		finally {
			if (resourceStream != null) {
				try {
					resourceStream.close();
				}
				catch (Throwable ignore) {
				}
			}
		}

		return p;

	}
	
	static public String getExtension(String path) {
		String[] strings = path.split("\\.");
		return strings[strings.length - 1];
	}

}
