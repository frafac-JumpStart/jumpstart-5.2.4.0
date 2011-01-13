package jumpstart.util.query;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.Query;
import javax.persistence.TemporalType;

public class MockQuery implements Query {

	public List<?> getResultList() {
		return null;
	}

	public Object getSingleResult() {
		return null;
	}

	public int executeUpdate() {
		return 0;
	}

	public Query setMaxResults(int arg0) {
		return null;
	}

	public Query setFirstResult(int arg0) {
		return null;
	}

	public Query setHint(String arg0, Object arg1) {
		return null;
	}

	public Query setParameter(String arg0, Object arg1) {
		return null;
	}

	public Query setParameter(String arg0, Date arg1, TemporalType arg2) {
		return null;
	}

	public Query setParameter(String arg0, Calendar arg1, TemporalType arg2) {
		return null;
	}

	public Query setParameter(int arg0, Object arg1) {
		return null;
	}

	public Query setParameter(int arg0, Date arg1, TemporalType arg2) {
		return null;
	}

	public Query setParameter(int arg0, Calendar arg1, TemporalType arg2) {
		return null;
	}

	public Query setFlushMode(FlushModeType arg0) {
		return null;
	}

}
