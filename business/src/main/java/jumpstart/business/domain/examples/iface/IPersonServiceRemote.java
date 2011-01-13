package jumpstart.business.domain.examples.iface;

import java.util.List;

import jumpstart.business.domain.examples.Person;

/**
 * The <code>IPersonServiceRemote</code> bean exposes the business methods in the interface.
 */
public interface IPersonServiceRemote {

	// Person

	Person findPerson(Long id);

	List<Person> findPersons(int maxResults);

	List<Person> findPersons(String partialName, int maxResults);

	List<Person> findPersonsByFirstName(String firstName);

	List<Person> findPersonsByLastName(String lastName);

	Person createPerson(Person person);

	void createPersons(List<Person> persons);

	void changePerson(Person person);

	void changePersons(List<Person> persons);

	void bulkEditPersons(List<Person> personsToCreate, List<Person> personsToChange, List<Person> personsToDelete);

	void deletePerson(Person person);
}
