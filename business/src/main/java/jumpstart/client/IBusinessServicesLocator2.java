package jumpstart.client;

import jumpstart.business.domain.security.iface.ISecurityFinderServiceLocal;
import jumpstart.business.domain.security.iface.ISecurityManagerServiceLocal;
import jumpstart.business.domain.workout.iface.IWorkoutServiceLocal;

public interface IBusinessServicesLocator2 {

	public abstract IWorkoutServiceLocal getWorkoutServiceLocal();

	public abstract ISecurityFinderServiceLocal getSecurityFinderServiceLocal();

	public abstract ISecurityManagerServiceLocal getSecurityManagerServiceLocal();

	/**
	 * Invoked after any kind of naming or remote exception. All cached naming contexts and interfaces are discarded.
	 */
	public abstract void clear();

}
