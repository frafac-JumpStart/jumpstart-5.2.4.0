package jumpstart.util;


import org.slf4j.Logger;

public class EJBProviderUtil {
	protected Logger _logger;

	static public EJBProviderEnum detectEJBProvider(Logger _logger) {
		_logger.info("Looking for an EJB provider...");
		
		try {
			Class.forName("org.apache.openejb.tomcat.loader.TomcatEmbedder");
			_logger.info("...found TomCat OpenEJB local.");
			return EJBProviderEnum.TOMCAT_OPENEJB_LOCAL;
		}
		catch (Exception e) {
		}

		try {
			Class.forName("org.apache.openejb.loader.Loader");
			_logger.info("...found OpenEJB local.");
			return EJBProviderEnum.OPENEJB_LOCAL;
		}
		catch (Exception e) {
		}

		try {
			Class.forName("org.apache.openejb.client.Client");
			_logger.info("...found OpenEJB remote.");
			return EJBProviderEnum.OPENEJB_REMOTE;
		}
		catch (Exception e) {
		}

		try {
			Class.forName("org.jboss.Main");
			_logger.info("...found JBoss local.");
			return EJBProviderEnum.JBOSS_LOCAL;
		}
		catch (Exception e) {
		}

		try {
			Class.forName("org.jboss.remoting.Client");
			_logger.info("...found JBoss remote.");
			return EJBProviderEnum.JBOSS_REMOTE;
		}
		catch (Exception e) {
		}

		try {
			Class.forName("com.sun.enterprise.ee.tools.launcher.ProcessLauncher");
			_logger.info("...found Glassfish (SunAS9) local.");
			return EJBProviderEnum.GLASSFISH_LOCAL;
		}
		catch (Exception e) {
		}

		try {
			Class.forName("PLBootstrap");
			_logger.info("...found Glassfish (SunAS9) remote.");
			return EJBProviderEnum.GLASSFISH_REMOTE;
		}
		catch (Exception e) {
		}

		throw new IllegalStateException(
				"Failed to detect a known EJBProvider. Tried OpenEJB, JBoss, and Glassfish.");
	}
}
