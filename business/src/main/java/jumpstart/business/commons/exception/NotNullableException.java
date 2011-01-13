package jumpstart.business.commons.exception;

import java.io.Serializable;

import javax.ejb.ApplicationException;

import jumpstart.business.MessageUtil;

@SuppressWarnings("serial")
@ApplicationException(rollback = true)
public class NotNullableException extends BusinessException {
	private String _entityLabelMessageId;
	private Serializable _id;
	private String _entityName;
	private String _propertyName;

	/**
	 * This exception is thrown by an IPersistenceExceptionInterpreter when a property that is not nullable has been provided as null.
	 * Ideally, this will never occur because you have tested them in the @PrePersist or @PreUpdate methods of
	 * the entity and thrown a ValueRequiredException. However, a common mistake that will slip through that test is a
	 * reference to an entity that has not been persisted.  This exception will catch it. 
	 * 
	 * @param entityName
	 *            the name of another entity that references the entity.
	 * @param propertyName
	 *            the name of the property in another entity that references the entity.
	 */
	public NotNullableException(String entityName, String propertyName) {

		// Don't convert the message ids to messages yet because we're in the
		// server's locale, not the user's.

		super();
		_entityName = entityName;
		_propertyName = propertyName;
	}

	@Override
	public String getMessage() {
		String msg;
		Object[] msgArgs;

		// We deferred converting the message ids to messages until now, when we
		// are more likely to be in the user's locale.

		msgArgs = new Object[] { MessageUtil.toText(_entityName),
				MessageUtil.toText(_entityName + "_" + _propertyName) };
		msg = MessageUtil.toText("NotNullableException", msgArgs);

		return msg;
	}

	public String getEntityLabelMessageId() {
		return _entityLabelMessageId;
	}

	public String getEntityName() {
		return _entityName;
	}

	public Serializable getId() {
		return _id;
	}

	public String getReferencedByEntityName() {
		return _entityName;
	}

	public String getReferencedByPropertyName() {
		return _propertyName;
	}

}
