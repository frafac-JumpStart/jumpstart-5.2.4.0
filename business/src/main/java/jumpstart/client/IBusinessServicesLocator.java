package jumpstart.client;

import jumpstart.business.domain.examples.iface.IDateStuffServiceLocal;
import jumpstart.business.domain.examples.iface.IPersonServiceLocal;

/**
 * A business services locator used by the examples.
 */
public interface IBusinessServicesLocator {

	public abstract IPersonServiceLocal getPersonServiceLocal();

	public abstract IDateStuffServiceLocal getDateStuffServiceLocal();
	/**
	 * Invoked after any kind of naming or remote exception. All cached naming contexts and interfaces are discarded.
	 */
	public abstract void clear();

}
