package jumpstart.business.domain.examples;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import jumpstart.business.domain.examples.iface.IDateStuffServiceLocal;
import jumpstart.business.domain.examples.iface.IDateStuffServiceRemote;

@Stateless
@Local(IDateStuffServiceLocal.class)
@Remote(IDateStuffServiceRemote.class)
public class DateStuffService implements IDateStuffServiceLocal, IDateStuffServiceRemote {

	@PersistenceContext(unitName = "jumpstart")
	protected EntityManager _em;

	public DateStuff findDateStuff(Long id) {
		return _em.find(DateStuff.class, id);
	}

	public void changeDateStuff(DateStuff dateStuff) {
		_em.merge(dateStuff);
	}

}
