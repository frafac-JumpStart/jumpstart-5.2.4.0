package jumpstart.business.commons.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
abstract public class BusinessException extends Exception {
	private static final long serialVersionUID = 1L;
}
