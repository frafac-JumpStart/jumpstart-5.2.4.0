package jumpstart.business.domain.base;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import jumpstart.business.commons.exception.BusinessException;
import jumpstart.business.commons.exception.CannotDeleteIsReferencedException;
import jumpstart.business.commons.exception.DoesNotExistException;
import jumpstart.business.commons.exception.DuplicateAlternateKeyException;
import jumpstart.business.commons.exception.DuplicatePrimaryKeyException;
import jumpstart.business.commons.interpreter.BusinessServiceExceptionInterpreter;
import jumpstart.util.EJBProviderEnum;
import jumpstart.util.EJBProviderUtil;

import org.slf4j.LoggerFactory;

public abstract class BaseService {

	@PersistenceContext(unitName = "jumpstart")
	protected EntityManager _em;

	protected final BusinessServiceExceptionInterpreter _businessServiceExceptionInterpreter = new BusinessServiceExceptionInterpreter();

	protected final EJBProviderEnum ejbProvider = EJBProviderUtil.detectEJBProvider(LoggerFactory
			.getLogger(BaseService.class));

	protected final boolean inOpenEJB = (ejbProvider == EJBProviderEnum.OPENEJB_LOCAL
			|| ejbProvider == EJBProviderEnum.OPENEJB_REMOTE || ejbProvider == EJBProviderEnum.TOMCAT_OPENEJB_LOCAL);

	protected void persist(BaseEntity entity) throws BusinessException {
		if (entity == null) {
			throw new IllegalArgumentException("persist(entity) has been given a null entity.");
		}
		try {
			_em.persist(entity);
		}
		catch (Exception e) {
			interpretAsBusinessExceptionAndThrowIt(e, entity, entity.getIdForMessages());
			throw new IllegalStateException("Shouldn't get here.", e);
		}
	}

	/**
	 * Beware - the caller MUST use the result, eg. invoice = merge(invoice).
	 * 
	 * @param <T>
	 * @param entity
	 * @return
	 * @throws BusinessException
	 */
	protected <T extends BaseEntity> T merge(T entity) throws BusinessException {
		if (entity == null) {
			throw new IllegalArgumentException("merge(entity) has been given a null entity.");
		}
		try {
			entity = _em.merge(entity);
		}
		catch (Exception e) {
			interpretAsBusinessExceptionAndThrowIt(e, entity, entity.getIdForMessages());
			throw new IllegalStateException("Shouldn't get here.", e);
		}
		return entity;
	}

	protected void remove(BaseEntity entity) throws BusinessException {
		if (entity == null) {
			throw new IllegalArgumentException("remove(entity) has been given a null entity.");
		}
		try {
			_em.remove(entity);
		}
		catch (Exception e) {
			interpretAsBusinessExceptionAndThrowIt(e, entity, entity.getIdForMessages());
			throw new IllegalStateException("Shouldn't get here.", e);
		}
	}

	protected <T> T find(Class<T> cls, Serializable id) throws DoesNotExistException {

		if (id == null) {
			throw new IllegalArgumentException("find(class, id) has been given null id.  Class is " + cls.getName()
					+ ".");
		}
		else if (id.equals(0)) {
			throw new IllegalArgumentException("find(class, id) has been given zero id.  Class is " + cls.getName()
					+ ".");
		}

		try {
			T obj = _em.find(cls, id);

			if (obj == null) {
				throw new DoesNotExistException(cls, id);
			}

			return obj;
		}
		catch (IllegalArgumentException e) {
			// Invalid id
			throw new IllegalArgumentException("find(class, id) has been given invalid id.  Class is " + cls.getName()
					+ ", id is \"" + id + "\".", e);
		}
		catch (Exception e) {
			// Doesn't exist
			throw new DoesNotExistException(cls, id);
		}
	}

	/**
	 * Calls to this method are a workaround for https://issues.apache.org/jira/browse/OPENEJB-782 and
	 * https://issues.apache.org/jira/browse/GERONIMO-3907 .
	 * 
	 * @throws BusinessException
	 */
	protected void flushToWorkAroundOPENEJB_782() throws BusinessException {
		if (inOpenEJB) {
			try {
				_em.flush();
			}
			catch (Exception e) {
				interpretAsBusinessExceptionAndThrowIt(e);
				throw new IllegalStateException("Shouldn't get here.", e);
			}
		}
	}

	/**
	 * Calls to this method are for a normal flush that you require regardless of whether you're using OPENEJB or not.
	 * See also flushToWorkAroundOPENEJB_782().
	 * 
	 * @throws BusinessException
	 */
	protected void flush() throws BusinessException {
		try {
			_em.flush();
		}
		catch (Exception e) {
			interpretAsBusinessExceptionAndThrowIt(e);
			throw new IllegalStateException("Shouldn't get here.", e);
		}
	}

	protected void interpretAsBusinessExceptionAndThrowIt(Throwable t) throws BusinessException {

		BusinessException be = _businessServiceExceptionInterpreter.interpret(t);

		// "Repackage" certain exceptions that we have more info about

		if (be instanceof DuplicatePrimaryKeyException) {
			be = new DuplicatePrimaryKeyException();
		}
		else if (be instanceof DuplicateAlternateKeyException) {
			be = new DuplicateAlternateKeyException(be.getMessage());
		}
		else if (be instanceof CannotDeleteIsReferencedException) {
			CannotDeleteIsReferencedException c = (CannotDeleteIsReferencedException) be;
			if (c.getInformationLevel() == CannotDeleteIsReferencedException.INFORMATIONLEVEL_ENTITY_ID_REFBYENTITY_REFBYPROPERTY) {
				be = new CannotDeleteIsReferencedException(c.getReferencedByEntityName());
			}
		}

		throw be;
	}

	protected void interpretAsBusinessExceptionAndThrowIt(Throwable t, BaseEntity entity, Serializable id)
			throws BusinessException {

		BusinessException be = _businessServiceExceptionInterpreter.interpret(t);

		// "Repackage" certain exceptions that we have more info about

		if (be instanceof DuplicatePrimaryKeyException) {
			be = new DuplicatePrimaryKeyException(entity, id);
		}
		else if (be instanceof DuplicateAlternateKeyException) {
			be = new DuplicateAlternateKeyException(entity, be.getMessage());
		}
		else if (be instanceof CannotDeleteIsReferencedException) {
			CannotDeleteIsReferencedException c = (CannotDeleteIsReferencedException) be;
			if (c.getInformationLevel() == CannotDeleteIsReferencedException.INFORMATIONLEVEL_ENTITY_ID_REFBYENTITY_REFBYPROPERTY) {
				be = new CannotDeleteIsReferencedException(entity, id, c.getReferencedByEntityName(), c
						.getReferencedByPropertyName());
			}
		}

		throw be;
	}

}
