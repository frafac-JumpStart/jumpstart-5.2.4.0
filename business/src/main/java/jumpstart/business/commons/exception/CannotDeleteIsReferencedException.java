package jumpstart.business.commons.exception;

import java.io.Serializable;

import javax.ejb.ApplicationException;

import jumpstart.business.MessageUtil;
import jumpstart.business.domain.base.BaseEntity;
import jumpstart.util.ClassUtil;

@SuppressWarnings("serial")
@ApplicationException(rollback = true)
public class CannotDeleteIsReferencedException extends BusinessException {
	static public final int INFORMATIONLEVEL_ENTITY_ID_REFBYENTITY_REFBYPROPERTY = 1;
	static public final int INFORMATIONLEVEL_TABLE_ID = 2;
	static public final int INFORMATIONLEVEL_REFBYENTITY = 3;

	private int _informationLevel = INFORMATIONLEVEL_ENTITY_ID_REFBYENTITY_REFBYPROPERTY;

	private String _entityLabelMessageId;
	private String _tableName;
	private Serializable _id;
	private String _referencedByEntityName;
	private String _referencedByPropertyName;

	private String _entityName;

	/**
	 * This exception is thrown by an IPersistenceExceptionInterpreter when an attempt to delete an entity has failed because the
	 * entity is referenced by other entities or it "contains" entities that are referenced by other entities. For
	 * example, it is thrown if attempting to delete a Department that still contains Teachers that are referenced by other
	 * entities.
	 * 
	 * @param entity
	 *            the entity being deleted, eg. a Department object.
	 * @param id
	 *            the id of the entity.
	 * @param referencedByEntityName
	 *            the name of another entity that references the entity.
	 * @param referencedByPropertyName
	 *            the name of the property in another entity that references the entity.
	 */
	public CannotDeleteIsReferencedException(BaseEntity entity, Serializable id, String referencedByEntityName,
			String referencedByPropertyName) {

		// Don't convert the message ids to messages yet because we're in the
		// server's locale, not the user's.

		super();
		_informationLevel = INFORMATIONLEVEL_ENTITY_ID_REFBYENTITY_REFBYPROPERTY;
		_entityLabelMessageId = ClassUtil.extractUnqualifiedName(entity);
		_id = id;
		_referencedByEntityName = referencedByEntityName;
		_referencedByPropertyName = referencedByPropertyName;
	}
	
	/**
	 * This exception is thrown by an IPersistenceExceptionInterpreter when an attempt to delete an entity has failed because the
	 * entity is referenced by other entities or it "contains" entities that are referenced by other entities. For
	 * example, it is thrown if attempting to delete a Department that still contains Teachers that are referenced by other
	 * entities.
	 * 
	 * @param tableName
	 *            the entity being deleted, eg. a Department object.
	 * @param id
	 *            the id of the entity.
	 */
	public CannotDeleteIsReferencedException(String tableName, Serializable id) {

		// Don't convert the message ids to messages yet because we're in the
		// server's locale, not the user's.

		super();
		_informationLevel = INFORMATIONLEVEL_TABLE_ID;
		_tableName = tableName;
		_id = id;
	}

	/**
	 * This exception is thrown by an IPersistenceExceptionInterpreter when an attempt to delete an entity has failed because
	 * the entity is referenced by other entities or it "contains" entities that are referenced by other entities. For
	 * example, it is thrown if attempting to delete a Department that still contains Teachers that are referenced by other
	 * entities.
	 * 
	 * @param referencedByEntityName
	 *            the name of another entity that references the entity.
	 */
	public CannotDeleteIsReferencedException(String referencedByEntityName) {

		// Don't convert the message ids to messages yet because we're in the
		// server's locale, not the user's.

		super();
		_informationLevel = INFORMATIONLEVEL_REFBYENTITY;
		_referencedByEntityName = referencedByEntityName;
	}

	@Override
	public String getMessage() {
		String msg;
		Object[] msgArgs;

		// We deferred converting the message ids to messages until now, when we
		// are more likely to be in the user's locale.

		if (_informationLevel == INFORMATIONLEVEL_ENTITY_ID_REFBYENTITY_REFBYPROPERTY) {
			msgArgs = new Object[] { MessageUtil.toText(_entityLabelMessageId), _id,
					MessageUtil.toText(_referencedByEntityName),
					MessageUtil.toText(_referencedByEntityName + "_" + _referencedByPropertyName) };
			msg = MessageUtil.toText("CannotDeleteIsReferencedException", msgArgs);
		}
		else if (_informationLevel == INFORMATIONLEVEL_TABLE_ID) {
			msgArgs = new Object[] { _tableName, _id };
			msg = MessageUtil.toText("CannotDeleteIsReferencedException_2", msgArgs);
		}
		else if (_informationLevel == INFORMATIONLEVEL_REFBYENTITY) {
			msgArgs = new Object[] { _referencedByEntityName };
			msg = MessageUtil.toText("CannotDeleteIsReferencedException_3", msgArgs);
		}
		else {
			throw new IllegalStateException("_informationLevel = " + _informationLevel);
		}

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

	public int getInformationLevel() {
		return _informationLevel;
	}

	public String getReferencedByEntityName() {
		return _referencedByEntityName;
	}

	public String getReferencedByPropertyName() {
		return _referencedByPropertyName;
	}

}
