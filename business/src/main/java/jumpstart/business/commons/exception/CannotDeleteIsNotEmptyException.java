package jumpstart.business.commons.exception;

import java.io.Serializable;

import javax.ejb.ApplicationException;

import jumpstart.business.MessageUtil;
import jumpstart.business.domain.base.BaseEntity;
import jumpstart.util.ClassUtil;

@SuppressWarnings("serial")
@ApplicationException(rollback = true)
public class CannotDeleteIsNotEmptyException extends BusinessException {
	private String _entityLabelMessageId;
	private Serializable _id;
	private String _partEntityLabelMessageId;

	/**
	 * Throw this exception from an entity that "contains" other objects that have not been deleted
	 * and are not defined to be "cascade deleted".  For example, deleting a Department that still contains Teachers.
	 *
	 * @param	entity	the entity holding the set of objects, eg. a Department object.
	 * @param	id		the id of the entity.
	 * @param	partEntityLabelMessageId	the key of a message that represents the object still contained, eg. "Teacher".
	 */
	public CannotDeleteIsNotEmptyException(BaseEntity entity, Serializable id, String partEntityLabelMessageId) {

		// Don't convert the message ids to messages yet because we're in the server's locale, not the user's.

		super();
		_entityLabelMessageId = ClassUtil.extractUnqualifiedName(entity);
		_id = id;
		_partEntityLabelMessageId = partEntityLabelMessageId;
	}

	@Override
	public String getMessage() {

		// We deferred converting the message ids to messages until now, when we are more likely to be in the user's
		// locale.

		Object[] msgArgs = new Object[] { MessageUtil.toText(_entityLabelMessageId), _id,
				MessageUtil.toText(_partEntityLabelMessageId) };

		String msg = MessageUtil.toText("CannotDeleteIsNotEmptyException", msgArgs);
		return msg;
	}

	public String getPartEntityLabelMessageId() {
		return _partEntityLabelMessageId;
	}

	public String getEntityLabelMessageId() {
		return _entityLabelMessageId;
	}

	public Serializable getId() {
		return _id;
	}

}
