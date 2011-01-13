package jumpstart.business.domain.examples;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
@SuppressWarnings("serial")
public class ValidationException extends Exception {

	public ValidationException(String message) {
		super(message);
	}

}
