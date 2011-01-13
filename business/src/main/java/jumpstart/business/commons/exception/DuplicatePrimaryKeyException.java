package jumpstart.business.commons.exception;

import java.io.Serializable;

import javax.ejb.ApplicationException;

import jumpstart.business.MessageUtil;
import jumpstart.business.domain.base.BaseEntity;
import jumpstart.util.ClassUtil;

@SuppressWarnings("serial")
@ApplicationException(rollback = true)
public class DuplicatePrimaryKeyException extends BusinessException {
	static public final int INFORMATIONLEVEL_ENTITY_ID = 1;
	static public final int INFORMATIONLEVEL_ID = 2;
	static public final int INFORMATIONLEVEL_NONE = 3;

	private int _informationLevel = INFORMATIONLEVEL_ENTITY_ID;
	private String _entityLabelMessageId;
	private Serializable _id;

	/**
	 * This exception is thrown by an IPersistenceExceptionInterpreter when an attempt to create an entity has failed because
	 * it already exists.  This is not possible when the entity definition specifies auto-generated ids.
	 * 
	 * @param entity	the entity being created.
	 * @param id	the id of the entity.
	 */
	public DuplicatePrimaryKeyException(BaseEntity entity, Serializable id) {

		// Don't convert the message ids to messages yet because we're in the
		// server's locale, not the user's.

		super();
		_informationLevel = INFORMATIONLEVEL_ENTITY_ID;
		_entityLabelMessageId = ClassUtil.extractUnqualifiedName(entity);
		_id = id;
	}

	/**
	 * This exception is thrown by an IPersistenceExceptionInterpreter when an attempt to create an entity has failed because
	 * it already exists.  This is not possible when the entity definition specifies auto-generated ids.
	 * 
	 * @param entityLabelMessageId	the key of a message that describes the entity.
	 * @param id	the id of the entity.
	 */
	public DuplicatePrimaryKeyException(String entityLabelMessageId, Serializable id) {

		// Don't convert the message ids to messages yet because we're in the
		// server's locale, not the user's.

		super();
		_informationLevel = INFORMATIONLEVEL_ENTITY_ID;
		_entityLabelMessageId = entityLabelMessageId;
		_id = id;
	}

	/**
	 * This exception is thrown by an IPersistenceExceptionInterpreter when an attempt to create an entity has failed because
	 * it already exists.  This is not possible when the entity definition specifies auto-generated ids.
	 * 
	 * @param id	the id of the entity.
	 */
	public DuplicatePrimaryKeyException(Serializable id) {

		// Don't convert the message ids to messages yet because we're in the
		// server's locale, not the user's.

		super();
		_informationLevel = INFORMATIONLEVEL_ID;
		_id = id;
	}

	/**
	 * This exception is thrown by an IPersistenceExceptionInterpreter when an attempt to create an entity has failed because
	 * it already exists.  This is not possible when the entity definition specifies auto-generated ids.
	 */
	public DuplicatePrimaryKeyException() {

		// Don't convert the message ids to messages yet because we're in the
		// server's locale, not the user's.

		super();
		_informationLevel = INFORMATIONLEVEL_NONE;
	}

	@Override
	public String getMessage() {
		String msg;
		Object[] msgArgs;

		// We deferred converting the message ids to messages until now, when we
		// are more likely to be in the user's locale.

		if (_informationLevel == INFORMATIONLEVEL_ENTITY_ID) {
			msgArgs = new Object[] { MessageUtil.toText(_entityLabelMessageId), _id };
			msg = MessageUtil.toText("DuplicatePrimaryKeyException", msgArgs);
		}
		else if (_informationLevel == INFORMATIONLEVEL_ID) {
			msgArgs = new Object[] { _id };
			msg = MessageUtil.toText("DuplicatePrimaryKeyException_2", msgArgs);
		}
		else if (_informationLevel == INFORMATIONLEVEL_NONE) {
			msgArgs = new Object[] {};
			msg = MessageUtil.toText("DuplicatePrimaryKeyException_3", msgArgs);
		}
		else {
			throw new IllegalStateException("_informationLevel = " + _informationLevel);
		}

		return msg;
	}

	public String getEntityLabelMessageId() {
		return _entityLabelMessageId;
	}

	public Serializable getId() {
		return _id;
	}

	public int getInformationLevel() {
		return _informationLevel;
	}
}
