package jumpstart.business.commons.interpreter;

import jumpstart.business.commons.exception.BusinessException;
import jumpstart.business.commons.exception.UnexpectedException;
import jumpstart.util.ExceptionUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BusinessServiceExceptionInterpreter {
	static private final Logger LOGGER = LoggerFactory.getLogger(BusinessServiceExceptionInterpreter.class);

	private IPersistenceExceptionInterpreter _persistenceExceptionInterpreter = new HibernatePersistenceExceptionInterpreter();

	/**
	 * Interpret a Throwable into a JumpStart BusinessException or throw an UnexpectedException.
	 */
	public BusinessException interpret(Throwable t) {
		try {
			BusinessException be = null;

			if (t instanceof BusinessException) {
				be = (BusinessException) t;
			}

			else if (t instanceof javax.persistence.PersistenceException) {
				be = _persistenceExceptionInterpreter.interpret((javax.persistence.PersistenceException) t);
			}

			else if (t instanceof java.sql.SQLException) {
				// SQLException is leaking out of JBoss 5.0 in duplicate alternate key situations
				be = _persistenceExceptionInterpreter.interpret((java.sql.SQLException) t);
			}

			else if (t.getCause() != null && !t.getCause().equals(t)) {
				be = interpret(t.getCause());
			}

			else {
				LOGGER.error("Cannot interpret Throwable " + t);
				throw new UnexpectedException(t);
			}

			return be;
		}
		catch (UnexpectedException e) {
			LOGGER.error(ExceptionUtil.printStackTrace(e));
			LOGGER.error("  Caused by: " + e.getCause());
			if (e.getRootCause() != null) {
				LOGGER.error("   Root cause: " + e.getRootCause().toString());
			}
			throw e;
		}
	}

}
