package jumpstart.client;

import javax.naming.NamingException;

import jumpstart.business.commons.jndi.JNDIObjectLocator;
import jumpstart.business.domain.examples.iface.IDateStuffServiceLocal;
import jumpstart.business.domain.examples.iface.IDateStuffServiceRemote;
import jumpstart.business.domain.examples.iface.IPersonServiceLocal;
import jumpstart.business.domain.examples.iface.IPersonServiceRemote;
import jumpstart.util.EJBProviderEnum;
import jumpstart.util.EJBProviderUtil;

import org.slf4j.Logger;

/**
 * BusinessServicesLocator is used to centralize all lookups of business services in JNDI. It is a global singleton
 * registered in AppModule. This version knows the formats of JNDI names assigned by OpenEJB, Glassfish, and JBoss
 * (unfortunately the EJB 3.0 specification didn't standardize them). It minimises the overhead of JNDI lookups by
 * caching the objects it looks up. If this class becomes a bottleneck, then you may need to decentralise it.
 */
public class BusinessServicesLocator extends JNDIObjectLocator implements IBusinessServicesLocator {

	protected String _personServiceName, _dateStuffServiceName;

	public BusinessServicesLocator(Logger logger) throws NamingException {
		super(logger);
		loadUpServiceNames(logger);
	}

	public IPersonServiceLocal getPersonServiceLocal() {
		return (IPersonServiceLocal) getJNDIObject(_personServiceName);
	}

	public IPersonServiceRemote getPersonServiceRemote() {
		return (IPersonServiceRemote) getJNDIObject(_personServiceName);
	}

	public IDateStuffServiceLocal getDateStuffServiceLocal() {
		return (IDateStuffServiceLocal) getJNDIObject(_dateStuffServiceName);
	}

	public IDateStuffServiceRemote getDateStuffServiceRemote() {
		return (IDateStuffServiceRemote) getJNDIObject(_dateStuffServiceName);
	}

	private void loadUpServiceNames(Logger logger) {

		// You wouldn't normally have to do all this work but JumpStart has to deal with many types of environment.

		EJBProviderEnum ejbProvider = EJBProviderUtil.detectEJBProvider(logger);

		if (ejbProvider == EJBProviderEnum.OPENEJB_LOCAL || ejbProvider == EJBProviderEnum.TOMCAT_OPENEJB_LOCAL) {
			_personServiceName = "PersonServiceLocal";
			_dateStuffServiceName = "DateStuffServiceLocal";
		}
		else if (ejbProvider == EJBProviderEnum.OPENEJB_REMOTE) {
			_personServiceName = "PersonServiceRemote";
			_dateStuffServiceName = "DateStuffServiceRemote";
		}
		else if (ejbProvider == EJBProviderEnum.GLASSFISH_LOCAL) {
			// Local interfaces in Glassfish are a bit touchy: the JNDI name used here must match the ejb-ref-name in
			// web.xml. See https://glassfish.dev.java.net/javaee5/ejb/EJB_FAQ.html
			_personServiceName = "java:comp/env/PersonService";
			_dateStuffServiceName = "java:comp/env/DateStuffService";
		}
		else if (ejbProvider == EJBProviderEnum.GLASSFISH_REMOTE) {
			_personServiceName = "jumpstart.business.domain.examples.iface.IPersonServiceRemote";
			_dateStuffServiceName = "jumpstart.business.domain.examples.iface.IDateStuffServiceRemote";
		}
		else if (ejbProvider == EJBProviderEnum.JBOSS_LOCAL) {
			_personServiceName = "jumpstart/PersonService/local";
			_dateStuffServiceName = "jumpstart/DateStuffService/local";
		}
		else if (ejbProvider == EJBProviderEnum.JBOSS_REMOTE) {
			_personServiceName = "jumpstart/PersonService/remote";
			_dateStuffServiceName = "jumpstart/DateStuffService/remote";
		}
		else {
			throw new IllegalStateException("Don't know how to use ejbProvider = " + ejbProvider);
		}
	}
}
