package jumpstart.business.commons.interpreter;

import java.io.Serializable;
import java.sql.BatchUpdateException;
import java.sql.SQLException;

import jumpstart.business.commons.exception.BusinessException;
import jumpstart.business.commons.exception.CannotDeleteIsReferencedException;
import jumpstart.business.commons.exception.DuplicateAlternateKeyException;
import jumpstart.business.commons.exception.DuplicatePrimaryKeyException;
import jumpstart.business.commons.exception.DuplicatePrimaryOrAlternateKeyException;
import jumpstart.business.commons.exception.NotNullableException;
import jumpstart.business.commons.exception.OptimisticLockException;
import jumpstart.business.commons.exception.UnexpectedException;

import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.GenericJDBCException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernatePersistenceExceptionInterpreter implements IPersistenceExceptionInterpreter {
	static private final Logger LOGGER = LoggerFactory.getLogger(HibernatePersistenceExceptionInterpreter.class);

	static public final int SQL_ERRORCODE_HSQLDB_INTEGRITY_CONSTRAINT_VIOLATION = -8;
	static public final int SQL_ERRORCODE_HSQLDB_UNIQUE_CONSTRAINT_VIOLATION = -104;
	static public final int SQL_ERRORCODE_MYSQL_CANNOT_DELETE_OR_UPDATE_A_PARENT_ROW = 1451;
	static public final int SQL_ERRORCODE_MYSQL_CANNOT_ADD_OR_UPDATE_A_CHILD_ROW = 1452;
	static public final int SQL_ERRORCODE_MYSQL_DUPLICATE_ENTRY = 1062;
	static public final int SQL_ERRORCODE_UNKNOWN = 0;

	static public final String SQL_STATE_DERBY_INTEGRITY_CONSTRAINT_VIOLATION = "23503";
	static public final String SQL_STATE_DERBY_DUPLICATE_ENTRY = "23505";

	// private DBType _dbType = null;
	//
	// public enum DBType {
	// HSQLDB, MYSQL, DERBY;
	// }

	public BusinessException interpret(javax.persistence.PersistenceException pe) {

		if (pe instanceof javax.persistence.OptimisticLockException) {
			return interpretOptimisticLockException((javax.persistence.OptimisticLockException) pe);
		}
		else if (pe instanceof javax.persistence.EntityExistsException) {
			return interpretEntityExistsException((javax.persistence.EntityExistsException) pe);
		}
		else {
			return interpretOtherPersistenceException((javax.persistence.PersistenceException) pe);
		}

	}

	private BusinessException interpretOptimisticLockException(javax.persistence.OptimisticLockException oe) {
		Throwable cause = oe.getCause();

		if (cause instanceof org.hibernate.StaleObjectStateException) {
			org.hibernate.StaleObjectStateException sose = (org.hibernate.StaleObjectStateException) cause;
			BusinessException be = new OptimisticLockException(sose.getEntityName(), sose.getIdentifier(), sose);
			return be;
		}
		else {
			LOGGER.error("Cannot interpret OptimisticLockException " + oe);
			LOGGER.error("  Cause = " + cause);
			throw new UnexpectedException(oe, cause);
		}
	}

	private BusinessException interpretEntityExistsException(javax.persistence.EntityExistsException eee) {
		Throwable cause = eee.getCause();

		if (cause instanceof org.hibernate.exception.ConstraintViolationException) {
			BusinessException be = interpretConstraintViolationException((org.hibernate.exception.ConstraintViolationException) cause);
			return be;
		}

		else {
			// TODO - review whether this is ever reached.
			Throwable rootCause = org.hibernate.exception.ExceptionUtils.getRootCause(eee);
			if (rootCause instanceof org.hibernate.NonUniqueObjectException) {
				org.hibernate.NonUniqueObjectException rootE = (org.hibernate.NonUniqueObjectException) rootCause;
				// String technicalMessage = rootE.getLocalizedMessage();
				// String entityName = rootE.getEntityName();
				Serializable identifier = rootE.getIdentifier();
				BusinessException be = new DuplicatePrimaryKeyException(identifier);
				return be;
			}
			else {
				LOGGER.error("Cannot interpret EntityExistsException " + eee);
				LOGGER.error("  Cause = " + cause);
				LOGGER.error("  RootCause = " + rootCause);
				throw new UnexpectedException(eee, rootCause);
			}
		}

	}

	private BusinessException interpretOtherPersistenceException(javax.persistence.PersistenceException pe) {
		Throwable t = pe.getCause();

		if (t instanceof org.hibernate.exception.ConstraintViolationException) {
			BusinessException be = interpretConstraintViolationException((ConstraintViolationException) t);
			return be;
		}

		else if (t instanceof org.hibernate.PropertyValueException) {
			BusinessException be = interpretPropertyValueException((org.hibernate.PropertyValueException) t);
			return be;
		}

		else if (t instanceof org.hibernate.exception.GenericJDBCException) {
			GenericJDBCException g = (GenericJDBCException) t;
			SQLException sqle = g.getSQLException();

			if (sqle instanceof BatchUpdateException) {
				int errorCode = sqle.getErrorCode();

				if (errorCode == SQL_ERRORCODE_UNKNOWN) {
					// Have seen this in HSQLDB, and it was due to duplicate primary key!!
					BusinessException be = new DuplicatePrimaryKeyException();
					return be;
				}
				else {
					LOGGER.error("Cannot interpret GenericJDBCException " + t);
					LOGGER.error("  SQLException = " + sqle);
					LOGGER.error("  ErrorCode = " + errorCode);
					throw new UnexpectedException(sqle, org.hibernate.exception.ExceptionUtils.getRootCause(pe));
				}
			}
			else {
				LOGGER.error("Cannot interpret GenericJDBCException " + t);
				LOGGER.error("  SQLException = " + sqle);
				throw new UnexpectedException(sqle, org.hibernate.exception.ExceptionUtils.getRootCause(pe));
			}
		}

		else if (t instanceof RuntimeException) {
			Throwable cause = t.getCause();

			if (cause instanceof BusinessException) {
				// A BusinessException - it was probably thrown by an Entity Lifecycle callback
				BusinessException be = (BusinessException) cause;
				return be;
			}
			else {
				LOGGER.error("Cannot interpret RuntimeException " + t);
				LOGGER.error("  Cause = " + cause);
				throw new UnexpectedException(t, org.hibernate.exception.ExceptionUtils.getRootCause(pe));
			}

		}

		else {
			LOGGER.error("Cannot interpret PersistenceException " + pe);
			throw new UnexpectedException(pe, org.hibernate.exception.ExceptionUtils.getRootCause(pe));
		}
	}

	private BusinessException interpretConstraintViolationException(
			org.hibernate.exception.ConstraintViolationException cve) {

		// Throwable rootCause = org.hibernate.exception.ExceptionUtils.getRootCause(cve);
		SQLException sqle = cve.getSQLException();

		if (sqle instanceof SQLException) {
			BusinessException be = interpret((SQLException) sqle);
			return be;
		}
		else {
			LOGGER.error("Cannot interpret ConstraintViolationException " + cve);
			LOGGER.error("  SQLException = " + sqle);
			throw new UnexpectedException(cve, sqle);
		}
	}

	private BusinessException interpretPropertyValueException(org.hibernate.PropertyValueException pve) {
		String rootMsg = pve.getMessage();
		// String technicalMessage = pve.getLocalizedMessage();

		if (rootMsg.indexOf("not-null property references") > -1) {
			String[] qualifiedEntityNameParts = pve.getEntityName().split("\\.");
			String referencedByEntityName = qualifiedEntityNameParts[qualifiedEntityNameParts.length - 1];
			String referencedByPropertyName = pve.getPropertyName();
			BusinessException be = new NotNullableException(referencedByEntityName, referencedByPropertyName);
			return be;
		}
		else {
			LOGGER.error("Cannot interpret PropertyValueException " + pve);
			LOGGER.error("  RootMsg = " + rootMsg);
			throw new UnexpectedException(pve, org.hibernate.exception.ExceptionUtils.getRootCause(pve));
		}
	}

	public BusinessException interpret(java.sql.SQLException sqle) {
		// DBType dbType = detectDatabaseType();

		int errorCode = sqle.getErrorCode();
		String sqlState = sqle.getSQLState();
		String message = sqle.getMessage();

		// if (dbType == DBType.HSQLDB)

		if (errorCode == SQL_ERRORCODE_HSQLDB_UNIQUE_CONSTRAINT_VIOLATION) {
			// This will be a message like this:
			// Violation of unique constraint SYS_CT_63: duplicate value(s) for column $$ in statement [insert into
			// ...
			if (message.startsWith("Violation of unique constraint")) {
				String[] chunks = sqle.getMessage().split(" ");
				String technicalMessage = sqle.getLocalizedMessage();

				if (chunks[4].startsWith("SYS_CT") || chunks[2].startsWith("unique")) {
					BusinessException be = new DuplicateAlternateKeyException(technicalMessage);
					return be;
				}
			}
		}

		else if (errorCode == SQL_ERRORCODE_HSQLDB_INTEGRITY_CONSTRAINT_VIOLATION) {
			String[] chunks = sqle.getMessage().split(" ");
			if (chunks[4].equals("table:") && chunks[6].equals("in")) {
				String entityName = chunks[5];
				// String constraintName = chunks[3];
				BusinessException be = new CannotDeleteIsReferencedException(entityName);
				return be;
			}
		}

		// else if (dbType == DBType.MYSQL)

		else if (errorCode == SQL_ERRORCODE_MYSQL_DUPLICATE_ENTRY) {
			// Expect message like "Duplicate entry 'john' for key 'name'"
			String technicalMessage = sqle.getLocalizedMessage();
			String[] chunks = message.split("\'");

			String value = chunks[1];
			String field = chunks[3];

			if (field.equals("PRIMARY")) {
				BusinessException be = new DuplicatePrimaryKeyException(value);
				return be;
			}
			else {
				BusinessException be = new DuplicateAlternateKeyException(field, technicalMessage);
				return be;
			}
		}

		else if (errorCode == SQL_ERRORCODE_MYSQL_CANNOT_DELETE_OR_UPDATE_A_PARENT_ROW) {
			// Expect message starting with "Cannot delete"
			String[] chunks = message.split("`");

			if (message.contains("foreign key constraint fails")) {
				String referencedByTableName = chunks[3];
				BusinessException be = new CannotDeleteIsReferencedException(referencedByTableName);
				return be;
			}
		}

		else if (errorCode == SQL_ERRORCODE_MYSQL_CANNOT_ADD_OR_UPDATE_A_CHILD_ROW) {
			// Expect message starting with "Cannot delete"
			String[] chunks = message.split("`");

			if (message.contains("foreign key constraint fails")) {
				@SuppressWarnings("unused")
				String referencedByTableName = chunks[3];
				// BusinessException be = new CannotDeleteIsReferencedException(referencedByTableName);
				// return be;
				LOGGER.error("Cannot currently handle SQLException " + sqle);
				LOGGER.error("  ErrorCode = " + errorCode);
				LOGGER.error("  SQLState = " + sqlState);
				LOGGER.error("  Message = " + message);
				throw new UnexpectedException(sqle);
			}
		}

		// else if (dbType == DBType.DERBY)

		else if (sqlState.equals(SQL_STATE_DERBY_DUPLICATE_ENTRY)) {
			// Expect message containing "duplicate key value"
			String technicalMessage = sqle.getLocalizedMessage();
			String[] chunks = message.split(" ");
			if (technicalMessage.contains("duplicate key value in a unique or primary key constraint")
					&& chunks[27].equals("on")) {
				String tableName = chunks[28].replace("'", "").replace(".", "");
				BusinessException be = new DuplicatePrimaryOrAlternateKeyException(tableName);
				return be;
			}
		}

		else if (sqlState.equals(SQL_STATE_DERBY_INTEGRITY_CONSTRAINT_VIOLATION)) {
			String[] chunks = sqle.getMessage().split(" ");
			if (chunks[0].equals("DELETE") && chunks[2].equals("table")) {
				String tableName = chunks[3].replace("'", "");
				String id = chunks[14].replace("(", "").replace(")", "").replace(".", "");
				// String constraintName = chunks[11];
				BusinessException be = new CannotDeleteIsReferencedException(tableName, id);
				return be;
			}
		}

		// else {
		// throw new IllegalStateException("Should never get here.");
		// }

		LOGGER.error("Cannot interpret SQLException " + sqle);
		LOGGER.error("  ErrorCode = " + errorCode);
		LOGGER.error("  SQLState = " + sqlState);
		LOGGER.error("  Message = " + message);
		throw new UnexpectedException(sqle);
	}

	//
	// This is unreliable because hibernate.dialect is often not ready in time for us.
	//
	// private DBType detectDatabaseType() {
	//		
	// if (_dbType == null) {
	// String hibernateDialect = System.getProperty("hibernate.dialect");
	//			
	// if (hibernateDialect == null) {
	// throw new IllegalStateException("Hibernate dialect has not been set.");
	// }
	// else if (hibernateDialect.equals("org.hibernate.dialect.HSQLDialect")) {
	// _dbType = DBType.HSQLDB;
	// }
	// else if (hibernateDialect.equals("org.hibernate.dialect.MySQLInnoDBDialect")) {
	// _dbType = DBType.MYSQL;
	// }
	// else if (hibernateDialect.equals("org.hibernate.dialect.DerbyDialect")) {
	// _dbType = DBType.DERBY;
	// }
	// else {
	// throw new IllegalStateException("Hibernate dialect not recognised: " + hibernateDialect + ".");
	// }
	// }
	// return _dbType;
	// }
}
