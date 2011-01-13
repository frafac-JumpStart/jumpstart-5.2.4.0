package jumpstart.business.commons.exception;

import javax.ejb.ApplicationException;

import jumpstart.business.MessageUtil;
import jumpstart.business.domain.base.BaseEntity;
import jumpstart.util.ClassUtil;

@SuppressWarnings("serial")
@ApplicationException(rollback = true)
public class DuplicateAlternateKeyException extends BusinessException {
	static public final int INFORMATIONLEVEL_ENTITY_TECHMSG = 1;
	static public final int INFORMATIONLEVEL_TECHMSG = 2;
	static public final int INFORMATIONLEVEL_KEY_VALUE = 3;

	private int _informationLevel = INFORMATIONLEVEL_ENTITY_TECHMSG;
	private String _entityLabelMessageId;
	private String _technicalMessageText;
	private String _keyValue;

	/**
	 * This exception is thrown by an IPersistenceExceptionInterpreter when an attempt to create an entity has failed because
	 * the entity specifies an alternate key (a unique key other than the primary key) that is already in use.
	 * 
	 * @param entity	the entity being created.
	 * @param technicalMessageText	typically this is a constraint violation message from the database.
	 */
	public DuplicateAlternateKeyException(BaseEntity entity, String technicalMessageText) {

		// Don't convert the message ids to messages yet because we're in the
		// server's locale, not the user's.

		super();
		_informationLevel = INFORMATIONLEVEL_ENTITY_TECHMSG;
		_entityLabelMessageId = ClassUtil.extractUnqualifiedName(entity);
		_technicalMessageText = technicalMessageText;
	}

	/**
	 * This exception is thrown by an IPersistenceExceptionInterpreter when an attempt to create an entity has failed because
	 * the entity specifies an alternate key (a unique key other than the primary key) that is already in use.
	 * 
	 * @param technicalMessageText	typically this is a constraint violation message from the database.
	 */
	public DuplicateAlternateKeyException(String technicalMessageText) {

		// Don't convert the message ids to messages yet because we're in the
		// server's locale, not the user's.

		super();
		_informationLevel = INFORMATIONLEVEL_TECHMSG;
		_technicalMessageText = technicalMessageText;
	}
	/**
	 * This exception is thrown by an IPersistenceExceptionInterpreter when an attempt to create an entity has failed because
	 * the entity specifies an alternate key (a unique key other than the primary key) that is already in use.
	 * 
	 * @param entity	the entity being created.
	 * @param technicalMessageText	typically this is a constraint violation message from the database.
	 */
	public DuplicateAlternateKeyException(String keyValue, String technicalMessageText) {

		// Don't convert the message ids to messages yet because we're in the
		// server's locale, not the user's.

		super();
		_informationLevel = INFORMATIONLEVEL_KEY_VALUE;
		_keyValue = keyValue;
		_technicalMessageText = technicalMessageText;
	}


	@Override
	public String getMessage() {
		String msg;
		Object[] msgArgs;

		// We deferred converting the message ids to messages until now, when we
		// are more likely to be in the user's locale.

		if (_informationLevel == INFORMATIONLEVEL_ENTITY_TECHMSG) {
			msgArgs = new Object[] { MessageUtil.toText(_entityLabelMessageId), _technicalMessageText };
			msg = MessageUtil.toText("DuplicateAlternateKeyException", msgArgs);
		}
		else if (_informationLevel == INFORMATIONLEVEL_TECHMSG) {
			msgArgs = new Object[] { _technicalMessageText };
			msg = MessageUtil.toText("DuplicateAlternateKeyException_2", msgArgs);
		}
		else if (_informationLevel == INFORMATIONLEVEL_KEY_VALUE) {
			msgArgs = new Object[] { _keyValue, _technicalMessageText };
			msg = MessageUtil.toText("DuplicateAlternateKeyException_3", msgArgs);
		}
		else {
			throw new IllegalStateException("_informationLevel = " + _informationLevel);
		}

		return msg;
	}

	public String getEntityLabelMessageId() {
		return _entityLabelMessageId;
	}

	public String getTechnicalMessageText() {
		return _technicalMessageText;
	}

	public int getInformationLevel() {
		return _informationLevel;
	}
}
