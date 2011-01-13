package jumpstart.web.base.theapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import jumpstart.business.commons.exception.BusinessException;
import jumpstart.business.commons.exception.CannotDeleteIsReferencedException;
import jumpstart.business.commons.exception.DuplicateAlternateKeyException;
import jumpstart.business.commons.exception.DuplicatePrimaryKeyException;
import jumpstart.business.commons.exception.OptimisticLockException;
import jumpstart.business.commons.interpreter.BusinessServiceExceptionInterpreter;
import jumpstart.business.domain.security.User;
import jumpstart.client.IBusinessServicesLocator2;
import jumpstart.web.state.theapp.Visit;

import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

public class SimpleBasePage {

	// @SessionState is explained in http://tapestry.apache.org/tapestry5/guide/appstate.html
	@SessionState
	private Visit _visit;
	private boolean _visitExists;

	// _businessServicesLocator locates our business services (which are EJB3 session
	// beans). It is a global singleton registered in AppModule.
	@Inject
	private IBusinessServicesLocator2 _businessServicesLocator;

	private BusinessServiceExceptionInterpreter _businessServiceExceptionInterpreter = new BusinessServiceExceptionInterpreter();

	@Inject
	private Messages _messages;

	protected Messages getMessages() {
		return _messages;
	}

	public String getDateInputPattern() {
		return _visitExists ? _visit.getDateInputPattern() : User.defaultDateInputPattern;
	}

	public String getDateViewPattern() {
		return _visitExists ? _visit.getDateViewPattern() : User.defaultDateViewPattern;
	}

	public String getDateListPattern() {
		return _visitExists ? _visit.getDateListPattern() : User.defaultDateListPattern;
	}
	
	public DateFormat getDateInputFormat() {
		// If you want to make this static or move it into Visit, first read
		// http://thread.gmane.org/gmane.comp.java.tapestry.user/20925
		return new SimpleDateFormat(_visit.getDateInputPattern());
	}

	public DateFormat getDateViewFormat() {
		// If you want to make this static or move it into Visit, first read
		// http://thread.gmane.org/gmane.comp.java.tapestry.user/20925
		return new SimpleDateFormat(_visit.getDateViewPattern());
	}

	public DateFormat getDateListFormat() {
		// If you want to make this static or move it into Visit, first read
		// http://thread.gmane.org/gmane.comp.java.tapestry.user/20925
		return new SimpleDateFormat(_visit.getDateListPattern());
	}

	protected String interpretBusinessServicesExceptionForCreate(Exception e) {
		String message = "";
		BusinessException x = _businessServiceExceptionInterpreter.interpret(e);

		if (x instanceof DuplicatePrimaryKeyException) {
			message = getMessages().get("create_failed_duplicate_primary_key");
		}
		else if (x instanceof DuplicateAlternateKeyException) {
			DuplicateAlternateKeyException d = (DuplicateAlternateKeyException) x;
			message = getMessages().format("create_failed_duplicate_alternate_key", d.getTechnicalMessageText());
		}
		else {
			message = x.getMessage();
		}
		return message;
	}

	protected String interpretBusinessServicesExceptionForAdd(Exception e) {
		String message = "";
		BusinessException x = _businessServiceExceptionInterpreter.interpret(e);

		if (x instanceof OptimisticLockException) {
			message = getMessages().get("add_failed_optimistic_lock");
		}
		else if (x instanceof DuplicatePrimaryKeyException) {
			message = getMessages().get("add_failed_duplicate_primary_key");
		}
		else if (x instanceof DuplicateAlternateKeyException) {
			DuplicateAlternateKeyException d = (DuplicateAlternateKeyException) x;
			message = getMessages().format("add_failed_duplicate_alternate_key", d.getTechnicalMessageText());
		}
		else {
			message = x.getMessage();
		}
		return message;
	}

	protected String interpretBusinessServicesExceptionForChange(Exception e) {
		String message = "";
		BusinessException x = _businessServiceExceptionInterpreter.interpret(e);

		if (x instanceof OptimisticLockException) {
			message = getMessages().get("change_failed_optimistic_lock");
		}
		else if (x instanceof DuplicateAlternateKeyException) {
			DuplicateAlternateKeyException d = (DuplicateAlternateKeyException) x;
			message = getMessages().format("change_failed_duplicate_alternate_key", d.getTechnicalMessageText());
		}
		else {
			message = x.getMessage();
		}
		return message;
	}

	protected String interpretBusinessServicesExceptionForRemove(Exception e) {
		String message = "";
		BusinessException x = _businessServiceExceptionInterpreter.interpret(e);

		if (x instanceof OptimisticLockException) {
			message = getMessages().get("remove_failed_optimistic_lock");
		}
		else if (x instanceof CannotDeleteIsReferencedException) {
			CannotDeleteIsReferencedException c = (CannotDeleteIsReferencedException) x;
			message = getMessages().format("remove_failed_is_referenced",
					new Object[] { c.getReferencedByEntityName() });
		}
		else {
			message = x.getMessage();
		}
		return message;
	}

	protected String interpretBusinessServicesExceptionForDelete(Exception e) {
		String message = "";
		BusinessException x = _businessServiceExceptionInterpreter.interpret(e);

		if (x instanceof OptimisticLockException) {
			message = getMessages().get("delete_failed_optimistic_lock");
		}
		else if (x instanceof CannotDeleteIsReferencedException) {
			CannotDeleteIsReferencedException c = (CannotDeleteIsReferencedException) x;
			message = getMessages().format("delete_failed_is_referenced",
					new Object[] { c.getReferencedByEntityName() });
		}
		else {
			message = x.getMessage();
		}
		return message;
	}

	protected IBusinessServicesLocator2 getBusinessServicesLocator() {
		return _businessServicesLocator;
	}

	public Visit getVisit() {
		return _visit;
	}

	public boolean isVisitExists() {
		return _visitExists;
	}

}
