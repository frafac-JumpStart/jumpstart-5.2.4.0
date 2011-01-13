package jumpstart.business.domain.examples;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jumpstart.business.domain.examples.iface.IPersonServiceLocal;
import jumpstart.business.domain.examples.iface.IPersonServiceRemote;

@Stateless
@Local(IPersonServiceLocal.class)
@Remote(IPersonServiceRemote.class)
public class PersonService implements IPersonServiceLocal, IPersonServiceRemote {

	@PersistenceContext(unitName = "jumpstart")
	protected EntityManager _em;

	public Person findPerson(Long id) {
		return _em.find(Person.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<Person> findPersons(int maxResults) {
		return _em.createQuery("select p from Person p order by lower(p.firstName), lower(p.lastName)").setMaxResults(
				maxResults).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Person> findPersons(String partialName, int maxResults) {
		String searchName = partialName == null ? "" : partialName.toLowerCase();

		StringBuffer buf = new StringBuffer();
		buf.append("select p from Person p");
		buf.append(" where lower(firstName) like :firstName");
		buf.append(" or lower(lastName) like :lastName");
		buf.append(" order by lower(p.firstName), lower(p.lastName)");

		Query q = _em.createQuery(buf.toString());
		q.setParameter("firstName", "%" + searchName + "%");
		q.setParameter("lastName", "%" + searchName + "%");
		q.setMaxResults(maxResults);

		List l = q.getResultList();
		return l;
	}

	@SuppressWarnings("unchecked")
	public List<Person> findPersonsByFirstName(String firstName) {
		String searchName = firstName == null ? "" : firstName.trim().toLowerCase();
		return _em.createQuery("select p from Person p where lower(p.firstName) = \'" + searchName + "\'").getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Person> findPersonsByLastName(String lastName) {
		String searchName = lastName == null ? "" : lastName.trim().toLowerCase();
		return _em.createQuery("select p from Person p where lower(p.lastName) = \'" + searchName + "\'").getResultList();
	}

	public Person createPerson(Person person) {
		_em.persist(person);
		return person;
	}

	public void createPersons(List<Person> persons) {
		for (Person person : persons) {
			_em.persist(person);
		}
	}

	public void changePerson(Person person) {
		_em.merge(person);
		// Flush to work around OPENEJB issue https://issues.apache.org/jira/browse/OPENEJB-782
		_em.flush();
	}

	public void changePersons(List<Person> persons) {
		for (Person person : persons) {
			_em.merge(person);
		}
	}

	public void bulkEditPersons(List<Person> personsToCreate, List<Person> personsToChange, List<Person> personsToDelete) {
		for (Person person : personsToCreate) {
			_em.persist(person);
		}
		for (Person person : personsToChange) {
			_em.merge(person);
		}
		for (Person person : personsToDelete) {
			person = _em.merge(person);
			_em.remove(person);
		}
	}

	public void deletePerson(Person person) {
		person = _em.merge(person);
		_em.remove(person);
		// Flush to work around OPENEJB issue https://issues.apache.org/jira/browse/OPENEJB-782
		_em.flush();
	}
}
