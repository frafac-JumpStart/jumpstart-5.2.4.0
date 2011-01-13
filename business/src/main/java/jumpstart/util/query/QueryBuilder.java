package jumpstart.util.query;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import jumpstart.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class QueryBuilder {
	static private final Logger LOGGER = LoggerFactory.getLogger(QueryBuilder.class);
	static public final String LINE_SEPARATOR = System.getProperty("line.separator");

	private StringBuffer _queryStringBuf = new StringBuffer();
	private List<Object> _parameters = new ArrayList<Object>();
	private int _parameterNumber = 0;
	private boolean _startedWhereClause = false;

	/**
	 * QueryBuilder simplifies the building of complex, dynamic queries. Here's an example of a typical usage:
	 * <p>
	 * QueryBuilder builder = new QueryBuilder();<br>
	 * builder.append("select u from User u");<br>
	 * builder.append(" join fetch u.status");<br>
	 * builder.appendLikeIgnoreCaseSkipEmpty("u.firstName", getFirstName());<br>
	 * builder.appendLikeIgnoreCaseSkipEmpty("u.lastName", getLastName());<br>
	 * Query q = builder.createQuery(_em, options, "u");<br>
	 * List l = q.getResultList();
	 */
	public QueryBuilder() {
	}

	/**
	 * Appends a string to the query
	 * 
	 * @param str eg. "select u from User u".
	 */
	public void append(String str) {
		if (_queryStringBuf.length() > 0){
			_queryStringBuf.append(" ");
		}
		_queryStringBuf.append(str);
	}

	/**
	 * Appends a LIKE test to the query, unless the value given is empty (null, empty string, or whitespace string). You
	 * may embed a wildcard, "%", in the value.
	 * 
	 * @param property eg. "u.emailAddress". This would be converted to "u.emailAddress like ".
	 * @param value eg. "Goo" or "%.net". These would be converted to "'Goo%'" and "'%.net%'".
	 */
	public void appendLikeSkipEmpty(String property, String value) {

		if (!StringUtil.isEmpty(value)) {
			_queryStringBuf.append(whereOrAnd());
			_queryStringBuf.append(" ").append(property).append(" like ?" + ++_parameterNumber);
			_parameters.add(value + "%");
		}
	}

	/**
	 * Appends a LIKE test to the query, unless the value given is empty (null, empty string, or whitespace string), You
	 * may embed a wildcard, "%", in the value.
	 * 
	 * @param property eg. "u.emailAddress". This would be converted to "lower(u.emailAddress) like ".
	 * @param value eg. "Goo" or "%.net". These would be converted to "lower('goo%')" and "lower('%.net%')".
	 */
	public void appendLikeIgnoreCaseSkipEmpty(String property, String value) {
		if (!StringUtil.isEmpty(value)) {
			appendLikeSkipEmpty("lower(" + property + ")", (value == null ? null : value.toLowerCase()));
		}
	}

	/**
	 * Appends an "=" test to the query. If a WHERE clause has not been started then it adds "where" to the query,
	 * followed by the test. Otherwise it adds "and" to the query, followed by the test.
	 * 
	 * @param property eg. "u.expiryDate". This would be converted to "u.expiryDate = ".
	 * @param value Any valid object, eg. a Date or a String.
	 */
	public void appendEquals(String property, Object value) {
		_queryStringBuf.append(whereOrAnd());
		_queryStringBuf.append(" ").append(property).append(" = ?" + ++_parameterNumber);
		_parameters.add(downcast(value));
	}

	/**
	 * Appends an "=" test to the query. If a WHERE clause has not been started then it adds "where" to the query,
	 * followed by the test. Otherwise it adds "and" to the query, followed by the test.
	 * 
	 * @param property eg. "u.statusCode". This would be converted to "lower(u.statusCode) = ".
	 * @param value A string, eg. "ACTIVE". This would be converted to "lower('ACTIVE')". Can be null.
	 */
	public void appendEqualsIgnoreCase(String property, String value) {
		appendEquals("lower(" + property + ")", (value == null ? null : value.toLowerCase()));
	}

	/**
	 * Appends an "=" test to the query unless the value given is empty (null, empty string, or whitespace string). If a
	 * WHERE clause has not been started then it adds "where" to the query, followed by the test. Otherwise it adds
	 * "and" to the query, followed by the test.
	 * 
	 * @param property eg. "u.expiryDate". This would be converted to "u.expiryDate = ".
	 * @param value Any valid object, eg. a Date or a String. If empty (null, empty string, or whitespace string) then
	 *        this operation will be abandoned and the query will not have changed.
	 */
	public void appendEqualsSkipEmpty(String property, Object value) {

		if (value != null) {
			if (value instanceof String) {
				if (!StringUtil.isEmpty((String) value)) {
					appendEquals(property, value);
				}
			}
			else {
				appendEquals(property, value);
			}
		}
	}

	/**
	 * Appends an "=" test to the query unless the value given is empty (null, empty string, or whitespace string). If a
	 * WHERE clause has not been started then it adds "where" to the query, followed by the test. Otherwise it adds
	 * "and" to the query, followed by the test.
	 * 
	 * @param property eg. "u.statusCode". This would be converted to "lower(u.statusCode) = ".
	 * @param value A string, eg. "ACTIVE". This would be converted to "lower('ACTIVE')". If empty (null, empty string,
	 *        or whitespace string) then this operation will be abandoned and the query will not have changed.
	 */
	public void appendEqualsIgnoreCaseSkipEmpty(String property, String value) {
		if (!StringUtil.isEmpty(value)) {
			appendEqualsIgnoreCase(property, value);
		}
	}

	/**
	 * Appends an IN expression to the query, eg. "where u.statusCode in ('ACTIVE', 'SUSPENDED')". If a WHERE clause has
	 * not been started then it adds "where" to the query, followed by the test. Otherwise it adds "and" to the query,
	 * followed by the test.
	 * 
	 * @param property eg. "u.statusCode". This would be converted to "u.statusCode in ".
	 * @param values A list of objects, eg. a list of strings such as {"ACTIVE", "SUSPENDED"}. This would be converted
	 *        to ('ACTIVE', 'SUSPENDED').
	 */
	public void appendIn(String property, List<Object> values) {

		if (values.size() > 0) {

			_queryStringBuf.append(whereOrAnd());
			_queryStringBuf.append(" (");

			for (int i = 0; i < values.size(); i++) {

				if (i != 0) {
					_queryStringBuf.append(" or");
				}

				Object value = values.get(i);
				_queryStringBuf.append(" ").append(property).append(" = ?" + ++_parameterNumber);
				_parameters.add(downcast(value));
			}

			if (values.size() > 0) {
				_queryStringBuf.append(")");
			}

		}
	}

	/**
	 * Appends an IN expression to the query, eg. "where u.statusCode in ('ACTIVE', 'SUSPENDED')". If a WHERE clause has
	 * not been started then it adds "where" to the query, followed by the test. Otherwise it adds "and" to the query,
	 * followed by the test.
	 * 
	 * @param property eg. "u.statusCode". This would be converted to "u.statusCode in ".
	 * @param values A list of objects, eg. a list of strings such as {"ACTIVE", "SUSPENDED"}. This would be converted
	 *        to ('ACTIVE', 'SUSPENDED').
	 */
	public void appendIn(String property, Object... values) {

		if (values.length > 0) {

			_queryStringBuf.append(whereOrAnd());
			_queryStringBuf.append(" (");

			for (int i = 0; i < values.length; i++) {

				if (i != 0) {
					_queryStringBuf.append(" or");
				}

				Object value = values[i];
				_queryStringBuf.append(" ").append(property).append(" = ?" + ++_parameterNumber);
				_parameters.add(downcast(value));
			}

			if (values.length > 0) {
				_queryStringBuf.append(")");
			}

		}
	}

	/**
	 * Appends a logical comparison to the query, eg. "where u.expiryDate > '2007-07-01'". If a WHERE clause has not
	 * been started then it adds "where" to the query, followed by the test. Otherwise it adds "and" to the query,
	 * followed by the test.
	 * 
	 * @param property, eg. "u.expiryDate".
	 * @param comparisonOperator, eg. "ComparisonOperator.GT". Theis would be converted to ">".
	 * @param value A single object, eg. a String, a Long, or a Date.
	 */
	public void appendComparison(String property, ComparisonOperator comparisonOperator, Object value) {

		if (value != null) {

			_queryStringBuf.append(whereOrAnd());

			switch (comparisonOperator) {
			case LT:
				_queryStringBuf.append(" ").append(property).append(" < ?" + ++_parameterNumber);
				break;
			case LE:
				_queryStringBuf.append(" ").append(property).append(" <= ?" + ++_parameterNumber);
				break;
			case EQ:
				_queryStringBuf.append(" ").append(property).append(" = ?" + ++_parameterNumber);
				break;
			case GE:
				_queryStringBuf.append(" ").append(property).append(" >= ?" + ++_parameterNumber);
				break;
			case GT:
				_queryStringBuf.append(" ").append(property).append(" > ?" + ++_parameterNumber);
				break;
			case NE:
				_queryStringBuf.append(" ").append(property).append(" <> ?" + ++_parameterNumber);
				break;
			default:
				throw new IllegalStateException("comparisonOperator = " + comparisonOperator);
			}

			_parameters.add(downcast(value));
		}
	}

	/**
	 * Appends a BETWEEN expression to the query. Actually it uses logical operators instead of BETWEEN, but the effect
	 * is the same, eg. "where u.expiryDate >= '2008-07-01' and u.expiryDate <= '2009-06-30'".
	 * <p>
	 * If a WHERE clause has not already been started then it adds "where" to the query, followed by the test. Otherwise
	 * it adds "and" to the query, followed by the test.
	 * 
	 * @param property eg. "u.expiryDate".
	 * @param fromValue Can be null, in which case it will be left out of the test. If toValue is also null then no test
	 *        will be generated.
	 * @param toValue Can be null, in which case it will be left out of the test. If fromValue is also null then no test
	 *        will be generated.
	 * @param includeEquals eg. true will result in a test like "fromValue <= property <= toValue". false will result in
	 *        a test like "fromValue < property < toValue".
	 */
	public void appendBetween(String property, Object fromValue, Object toValue, boolean includeEquals) {

		if ((fromValue != null) || (toValue != null)) {

			if (fromValue != null) {
				_queryStringBuf.append(whereOrAnd());
				if (includeEquals) {
					_queryStringBuf.append(" ").append(property).append(" >= ?" + ++_parameterNumber);
				}
				else {
					_queryStringBuf.append(" ").append(property).append(" > ?" + ++_parameterNumber);
				}
				_parameters.add(downcast(fromValue));
			}

			if (toValue != null) {
				_queryStringBuf.append(whereOrAnd());
				if (includeEquals) {
					_queryStringBuf.append(" ").append(property).append(" <= ?" + ++_parameterNumber);
				}
				else {
					_queryStringBuf.append(" ").append(property).append(" < ?" + ++_parameterNumber);
				}
				_parameters.add(downcast(toValue));
			}

		}
	}

	/**
	 * Get the query string that you've been building in QueryBuilder. Rarely used - it is more usual to call one of the
	 * createQuery(...) methods.
	 * 
	 * @return The query string you've been building woth QueryBuilder.
	 */
	public String getQueryString() {
		return _queryStringBuf.toString();
	}

	/**
	 * This method is used solely for testing this class.
	 * 
	 * @return
	 */
	Object[] getParameters() {
		return _parameters.toArray();
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("Query string = \"").append(_queryStringBuf.toString()).append("\"").append(LINE_SEPARATOR);
		buf.append("Parameters = { ");
		for (Object parameter : _parameters) {
			buf.append("[");
			if (parameter instanceof String) {
				buf.append("\"").append(parameter.toString()).append("\"");
			}
			else {
				buf.append(parameter.getClass().getName() + ": " + parameter.toString());
			}
			buf.append("] ");
		}
		buf.append(" }");
		return buf.toString();
	}

	/**
	 * Create the query. This is usually the last step after instantiating a QueryBuilder and using the various "append"
	 * methods to build up the query string.
	 * 
	 * @param em An EntityManager.
	 * @return A query object ready to use.
	 */
	public Query createQuery(EntityManager em) {

		try {
			// Create the query in the usual way
			Query q = em.createQuery(_queryStringBuf.toString());

			// Set its parameters
			int parameterNumber = 0;
			for (Object parameter : _parameters) {
				q.setParameter(++parameterNumber, parameter);
			}

			return q;
		}
		catch (RuntimeException e) {
			LOGGER.error("Exception creating query \"" + _queryStringBuf + "\".");
			throw e;
		}

	}

	/**
	 * Create the query. This is usually the last step after instantiating a QueryBuilder and using the various "append"
	 * methods to build up the query string.
	 * 
	 * @param em An EntityManager.
	 * @param options A SearchOptions object.
	 * @param objectPrefix This will be prepended to the sorting column names found in the options object.
	 * @return A query object ready to use.
	 */
	public Query createQuery(EntityManager em, SearchOptions options, String objectPrefix) {

		try {
			// Add the order-by string
			appendOrderBy(options, objectPrefix);

			// Create the query in the usual way
			Query q = em.createQuery(_queryStringBuf.toString());

			// Set its parameters
			int parameterNumber = 0;
			for (Object parameter : _parameters) {
				q.setParameter(++parameterNumber, parameter);
			}

			// Set other criteria of the query
			q.setMaxResults(options.getMaxRows());

			return q;
		}
		catch (RuntimeException e) {
			LOGGER.error("Exception creating query \"" + _queryStringBuf + "\".");
			throw e;
		}

	}

	private void appendOrderBy(SearchOptions options, String objectPrefix) {
		_queryStringBuf.append(createOrderByClause(options, objectPrefix));
	}

	private String createOrderByClause(SearchOptions options, String objectPrefix) {
		StringBuffer buf = new StringBuffer();

		int index = 0;
		boolean firstOrder = true;
		for (String sortColumnName : options.getSortColumnNames()) {
			if (!StringUtil.isEmpty(sortColumnName)) {
				if (firstOrder) {
					firstOrder = false;
					buf.append(" order by");
				}
				else {
					buf.append(",");
				}
				buf.append(" ").append(objectPrefix).append(".").append(sortColumnName);
				buf.append(options.isSortAscending(index) ? " asc" : " desc");
			}
			index++;
		}

		return buf.toString();
	}

	private String whereOrAnd() {

		if (_startedWhereClause) {
			return " and";
		}
		else {
			_startedWhereClause = true;
			return " where";
		}

	}

	private Object downcast(Object obj) {
		Object out = obj;

		// Nothing to do. Will implement when joda-time involved.

		return out;
	}

}
