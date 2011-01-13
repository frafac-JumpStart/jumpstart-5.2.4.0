package jumpstart.util.query;

import javax.persistence.EntityManager;

import jumpstart.util.query.QueryBuilder;
import jumpstart.util.query.SearchOptions;
import junit.framework.TestCase;

public class QueryBuilderTest extends TestCase {

	public void test_appendLike_functions_correctly() {

		QueryBuilder q = new QueryBuilder();

		// test empty string
		q.appendLikeSkipEmpty("property", "");
		assertEquals(0, q.getQueryString().length());

		// test null string
		q.appendLikeSkipEmpty("property", null);
		assertEquals(0, q.getQueryString().length());

		// test for successful case
		q.appendLikeSkipEmpty("propertyA", "valueA");
		assertTrue(q.getQueryString().length() > 0);
		assertEquals(" where propertyA like ?1", q.getQueryString());
		assertEquals(1, q.getParameters().length);

		// test for successful case with and appended
		q.appendLikeSkipEmpty("propertyB", "valueB");
		assertTrue(q.getQueryString().length() > 0);
		assertEquals(" where propertyA like ?1 and propertyB like ?2", q.getQueryString());

		Object[] params = q.getParameters();
		assertEquals(2, params.length);
		assertEquals("valueA%", params[0]);
		assertEquals("valueB%", params[1]);
	}

	public void test_appendEquals_functions_correctly() {

		QueryBuilder q = new QueryBuilder();

		// test empty string
		q.appendEqualsSkipEmpty("property", "");
		assertEquals(0, q.getQueryString().length());

		// test null string
		q.appendEqualsSkipEmpty("property", null);
		assertEquals(0, q.getQueryString().length());

		q = new QueryBuilder();
		q.appendEquals("property", "");
		assertTrue(q.getQueryString().length() > 0);
		assertEquals(" where property = ?1", q.getQueryString());
		assertEquals(1, q.getParameters().length);
		assertEquals("", q.getParameters()[0]);

		q = new QueryBuilder();
		q.appendEquals("property", null);
		assertTrue(q.getQueryString().length() > 0);
		assertEquals(" where property = ?1", q.getQueryString());
		assertEquals(1, q.getParameters().length);
		assertNull(q.getParameters()[0]);

		// test for successful case
		q = new QueryBuilder();
		q.appendEqualsSkipEmpty("propertyA", "valueA");
		assertTrue(q.getQueryString().length() > 0);
		assertEquals(" where propertyA = ?1", q.getQueryString());
		assertEquals(1, q.getParameters().length);

		// test for successful case with and appended
		q.appendEqualsSkipEmpty("propertyB", "valueB");
		assertTrue(q.getQueryString().length() > 0);
		assertEquals(" where propertyA = ?1 and propertyB = ?2", q.getQueryString());

		Object[] params = q.getParameters();
		assertEquals(2, params.length);
		assertEquals("valueA", params[0]);
		assertEquals("valueB", params[1]);
	}

	public void test_order_by_portion_of_query_is_correct() {
		QueryBuilder q = new QueryBuilder();
		q.append("from Class c");

		SearchOptions options = new SearchOptions("propertyA", true);
		options.addSearchOptions("propertyB", false);
		EntityManager em = new MockEntityManager();

		q.createQuery(em, options, "c");
		assertEquals("from Class c order by c.propertyA asc, c.propertyB desc", q.getQueryString());
	}

}
