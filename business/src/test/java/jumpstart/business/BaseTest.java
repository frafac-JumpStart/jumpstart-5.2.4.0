package jumpstart.business;

import javax.naming.NamingException;
import javax.naming.NoInitialContextException;

import jumpstart.business.commons.exception.BusinessException;
import jumpstart.business.commons.interpreter.BusinessServiceExceptionInterpreter;
import jumpstart.business.domain.security.iface.ISecurityFinderServiceRemote;
import jumpstart.business.domain.security.iface.ISecurityManagerServiceRemote;
import jumpstart.business.domain.workout.iface.IWorkoutServiceRemote;
import jumpstart.client.BusinessServicesLocator2;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseTest {

	static protected boolean _servicesLocated = false;

	static protected IWorkoutServiceRemote _workoutService;
	static protected ISecurityFinderServiceRemote _securityFinderService;
	static protected ISecurityManagerServiceRemote _securityManagerService;

	static protected final Logger _logger = LoggerFactory.getLogger(BusinessServiceExceptionInterpreter.class);
	static protected BusinessServiceExceptionInterpreter _interpreter = new BusinessServiceExceptionInterpreter();

	static public void locateServices() {

		try {
			BusinessServicesLocator2 locator = new BusinessServicesLocator2(_logger);
			_workoutService = locator.getWorkoutServiceRemote();
			_securityFinderService = locator.getSecurityFinderServiceRemote();
			_securityManagerService = locator.getSecurityManagerServiceRemote();
		}
		catch (NoInitialContextException e) {
			throw new RuntimeException(
					"Could not locate services.  Is jndi.properties missing from the testing classpath?", e);
		}
		catch (NamingException e) {
			throw new RuntimeException(
					"Could not locate services.  Is ejb server started?  If using jboss, is jbossall-client.jar missing from the testing classpath?",
					e);
		}

	}

	@Before
	public void setUp() throws Exception {

		if (!_servicesLocated) {
			locateServices();
			_servicesLocated = true;
		}

		deleteAllWorkoutEntities();
	}

	public void deleteAllWorkoutEntities() throws BusinessException {
		_workoutService.deleteAllStudents();
		_workoutService.deleteAllTeachers();
		_workoutService.deleteAllDepartments();
		_workoutService.removeAllRooms();
		_workoutService.deleteAllBuildings();
		_workoutService.deleteAllStringThings();
	}

}
