package jumpstart.client;

import javax.naming.NamingException;

import jumpstart.business.domain.security.iface.ISecurityFinderServiceLocal;
import jumpstart.business.domain.security.iface.ISecurityFinderServiceRemote;
import jumpstart.business.domain.security.iface.ISecurityManagerServiceLocal;
import jumpstart.business.domain.security.iface.ISecurityManagerServiceRemote;
import jumpstart.business.domain.workout.iface.IWorkoutServiceLocal;
import jumpstart.business.domain.workout.iface.IWorkoutServiceRemote;
import jumpstart.util.EJBProviderEnum;
import jumpstart.util.EJBProviderUtil;
import jumpstart.business.commons.jndi.JNDIObjectLocator;

import org.slf4j.Logger;

/**
 * BusinessServicesLocator is used to centralize all JNDI lookups. It is a global singleton registered in AppModule. It
 * minimises the overhead of JNDI lookups by caching the objects it looks up. If this class becomes a bottleneck, then
 * you may need to decentralise it.
 */
public class BusinessServicesLocator2 extends JNDIObjectLocator implements IBusinessServicesLocator2 {

	protected String _workoutServiceName, _securityFinderServiceName, _securityManagerServiceName;

	public BusinessServicesLocator2(Logger logger) throws NamingException {
		super(logger);
		loadUpServiceNames(logger);
	}

	public IWorkoutServiceLocal getWorkoutServiceLocal() {
		return (IWorkoutServiceLocal) getJNDIObject(_workoutServiceName);
	}

	public IWorkoutServiceRemote getWorkoutServiceRemote() {
		return (IWorkoutServiceRemote) getJNDIObject(_workoutServiceName);
	}
	
	public ISecurityFinderServiceLocal getSecurityFinderServiceLocal() {
		return (ISecurityFinderServiceLocal) getJNDIObject(_securityFinderServiceName);
	}

	public ISecurityFinderServiceRemote getSecurityFinderServiceRemote() {
		return (ISecurityFinderServiceRemote) getJNDIObject(_securityFinderServiceName);
	}

	public ISecurityManagerServiceLocal getSecurityManagerServiceLocal() {
		return (ISecurityManagerServiceLocal) getJNDIObject(_securityManagerServiceName);
	}

	public ISecurityManagerServiceRemote getSecurityManagerServiceRemote() {
		return (ISecurityManagerServiceRemote) getJNDIObject(_securityManagerServiceName);
	}

	private void loadUpServiceNames(Logger logger) {
		EJBProviderEnum ejbProvider = EJBProviderUtil.detectEJBProvider(logger);

		if (ejbProvider == EJBProviderEnum.OPENEJB_LOCAL || ejbProvider == EJBProviderEnum.TOMCAT_OPENEJB_LOCAL) {
			_workoutServiceName = "WorkoutServiceLocal";
			_securityFinderServiceName = "SecurityFinderServiceLocal";
			_securityManagerServiceName = "SecurityManagerServiceLocal";
		}
		else if (ejbProvider == EJBProviderEnum.OPENEJB_REMOTE) {
			_workoutServiceName = "WorkoutServiceRemote";
			_securityFinderServiceName = "SecurityFinderServiceRemote";
			_securityManagerServiceName = "SecurityManagerServiceRemote";
		}
		else if (ejbProvider == EJBProviderEnum.GLASSFISH_LOCAL) {
			// Local interfaces in Glassfish are a bit touchy: the JNDI name used here must match the ejb-ref-name in
			// web.xml. See https://glassfish.dev.java.net/javaee5/ejb/EJB_FAQ.html
			_workoutServiceName = "java:comp/env/WorkoutService";
			_securityFinderServiceName = "java:comp/env/SecurityFinderService";
			_securityManagerServiceName = "java:comp/env/SecurityManagerService";
		}
		else if (ejbProvider == EJBProviderEnum.GLASSFISH_REMOTE) {
			_workoutServiceName = "jumpstart.business.domain.workout.iface.IWorkoutServiceRemote";
			_securityFinderServiceName = "jumpstart.business.domain.security.iface.ISecurityFinderServiceRemote";
			_securityManagerServiceName = "jumpstart.business.domain.security.iface.ISecurityManagerServiceRemote";
		}
		else if (ejbProvider == EJBProviderEnum.JBOSS_LOCAL) {
			_workoutServiceName = "jumpstart/WorkoutService/local";
			_securityFinderServiceName = "jumpstart/SecurityFinderService/local";
			_securityManagerServiceName = "jumpstart/SecurityManagerService/local";
		}
		else if (ejbProvider == EJBProviderEnum.JBOSS_REMOTE) {
			_workoutServiceName = "jumpstart/WorkoutService/remote";
			_securityFinderServiceName = "jumpstart/SecurityFinderService/remote";
			_securityManagerServiceName = "jumpstart/SecurityManagerService/remote";
		}
		else {
			throw new IllegalStateException("Don't know how to use ejbProvider = " + ejbProvider);
		}
	}
}
