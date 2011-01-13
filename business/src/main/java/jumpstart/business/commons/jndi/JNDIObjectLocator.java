package jumpstart.business.commons.jndi;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

import jumpstart.business.commons.exception.SystemUnavailableException;
import jumpstart.util.EJBProviderEnum;
import jumpstart.util.EJBProviderUtil;

import org.slf4j.Logger;

/**
 * JNDIObjectLocator is used to centralize all JNDI lookups. It minimises the overhead of JNDI lookups by caching the
 * objects it looks up.
 */
public class JNDIObjectLocator {
	protected Logger _logger;
	private InitialContext _initialContext;
	private Map<String, Object> _jndiObjectCache = Collections.synchronizedMap(new HashMap<String, Object>());

	public JNDIObjectLocator(Logger logger) throws NamingException {
		_logger = logger;
	}

	public synchronized void clear() {
		_jndiObjectCache.clear();
	}

	public Object getJNDIObject(String jndiName) {

		Object jndiObject = _jndiObjectCache.get(jndiName);

		if (jndiObject == null && !_jndiObjectCache.containsKey(jndiName)) {
			try {
				jndiObject = lookup(jndiName);
				_jndiObjectCache.put(jndiName, jndiObject);
			}
			catch (RuntimeException e) {
				clear();
				throw e;
			}
		}
		return jndiObject;
	}

	private synchronized Object lookup(String name) {

		// Recheck the cache because the name we're looking for may have been added while we were waiting for sync.

		if (!_jndiObjectCache.containsKey(name)) {
			try {
				return getInitialContext().lookup(name);
			}
			catch (NameNotFoundException e) {
				clear();
				throw new SystemUnavailableException("JNDI lookup failed for \"" + name
						+ "\".  Is ejb server not started? Has the ejb.provider property been specified correctly", e);
			}
			catch (NamingException e) {
				clear();
				throw new SystemUnavailableException(
						"JNDI lookup failed for \""
								+ name
								+ "\".  Is ejb server not started?  If using jboss, is jbossall-client.jar missing from classpath?"
								+ " Error looking up " + e.getRemainingName() + " because of " + e.getCause()
								+ " while " + e.getExplanation(), e);
			}
		}
		else {
			return _jndiObjectCache.get(name);
		}
	}

	private synchronized InitialContext getInitialContext() {
		if (_initialContext == null) {

			try {
				EJBProviderEnum ejbProvider = EJBProviderUtil.detectEJBProvider(_logger);

				// If we're in Tomcat then jndi.properties will be ignored so hard-code the properties

				if (ejbProvider == EJBProviderEnum.TOMCAT_OPENEJB_LOCAL) {
					Properties p = new Properties();
					p.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.LocalInitialContextFactory");
					p.put(Context.URL_PKG_PREFIXES, "org.apache.openejb.core.ivm.naming");
					_initialContext = new InitialContext(p);
				}

				// Else, we intend InitialContext to be configured by a jndi.properties file we put in the classpath,
				// eg. in a conf/ directory of your project or in a jboss server's conf/. It's not a good idea to
				// hard-code the properties here.

				else {
					_initialContext = new InitialContext();
				}

				_logger.info("InitialContext environment = " + _initialContext.getEnvironment());
				_logger.info("InitialContext contains:");
				listContext("   ", _initialContext);
			}
			catch (NamingException e) {
				clear();
				throw new SystemUnavailableException("Cannot get initial context."
						+ " Is JNDI server not started?  If using jboss, is jbossall-client.jar missing from classpath?"
						+ " Error looking up " + e.getRemainingName() + " because of " + e.getCause() + " while "
						+ e.getExplanation(), e);
			}

		}

		return _initialContext;
	}

	/**
	 * This is not essential, but it can be handy when things go wrong to have the objects in the context listed in the log.
	 * 
	 * @param s
	 * @param c
	 * @throws NamingException
	 */
	private final void listContext(String s, Context c) throws NamingException {
		NamingEnumeration<NameClassPair> pairs = c.list("");
		for (; pairs.hasMoreElements();) {
			NameClassPair p = pairs.next();
			_logger.info(s + "/" + p.getName() + " : " + p.getClassName());

			try {
				Object o = c.lookup(p.getName());

				if (o instanceof Context) {
					Context child = (Context) o;
					listContext(s + "/" + p.getName(), child);
				}
			}
			catch (Throwable t) {
				// Not really a problem so just log it.
				_logger.debug("      " + t.getClass().getName() + ": " + t.getMessage());
			}
		}
	}
}
