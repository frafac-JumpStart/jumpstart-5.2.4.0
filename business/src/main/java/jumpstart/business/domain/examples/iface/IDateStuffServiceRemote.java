package jumpstart.business.domain.examples.iface;

import jumpstart.business.domain.examples.DateStuff;


/**
 * The <code>IDateStuffServiceRemote</code> bean exposes the business methods
 * in the interface.
 */
public interface IDateStuffServiceRemote {

	// DateStuff

	DateStuff findDateStuff(Long id);

	void changeDateStuff(DateStuff dateStuff);

}
