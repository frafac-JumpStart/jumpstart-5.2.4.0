package jumpstart.business.commons.exception;

import javax.ejb.ApplicationException;

import jumpstart.business.MessageUtil;

@SuppressWarnings("serial")
@ApplicationException(rollback = true)
public class DuplicatePrimaryOrAlternateKeyException extends BusinessException {
	static public final int INFORMATIONLEVEL_TABLE_NAME = 1;

	private int _informationLevel = INFORMATIONLEVEL_TABLE_NAME;
	private String _tableName;

	/**
	 * This exception is thrown by an IPersistenceExceptionInterpreter when an attempt to create an entity in Derby has
	 * failed because it already exists or a unique key violation occurs. Derby cannot differentiate between the 2
	 * cases. This exception is not possible when the entity definition specifies auto-generated ids.
	 * 
	 * @param tableName
	 */
	public DuplicatePrimaryOrAlternateKeyException(String tableName) {

		// Don't convert the message ids to messages yet because we're in the
		// server's locale, not the user's.

		super();
		_informationLevel = INFORMATIONLEVEL_TABLE_NAME;
		_tableName = tableName;
	}

	@Override
	public String getMessage() {
		String msg;
		Object[] msgArgs;

		// We deferred converting the message ids to messages until now, when we
		// are more likely to be in the user's locale.

		msgArgs = new Object[] { _tableName };
		msg = MessageUtil.toText("DuplicatePrimaryOrAlternateKeyException", msgArgs);

		return msg;
	}

	public String getTableName() {
		return _tableName;
	}

	public int getInformationLevel() {
		return _informationLevel;
	}
}
