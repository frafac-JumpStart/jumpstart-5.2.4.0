package jumpstart.business.commons.interpreter;

import java.sql.SQLException;

import javax.persistence.PersistenceException;

import jumpstart.business.commons.exception.BusinessException;

public interface IPersistenceExceptionInterpreter {

	public BusinessException interpret(PersistenceException e);
	
	// SQLException is leaking out of JBoss 5.0 in duplicate alternate key situations
	public BusinessException interpret(SQLException e);

}
