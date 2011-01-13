package jumpstart.business.domain.security;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import jumpstart.business.commons.exception.BusinessException;
import jumpstart.business.commons.exception.GenericBusinessException;
import jumpstart.business.domain.base.BaseService;
import jumpstart.business.domain.security.iface.ISecurityManagerServiceLocal;
import jumpstart.business.domain.security.iface.ISecurityManagerServiceRemote;

//@SecurityDomain("jumpstart")
@Stateless
@Local(ISecurityManagerServiceLocal.class)
@Remote(ISecurityManagerServiceRemote.class)
public class SecurityManagerService extends BaseService implements ISecurityManagerServiceLocal, ISecurityManagerServiceRemote {

	private final String _demoModeStr = System.getProperty("jumpstart.demo-mode");
	
	/*******************************************************************************************************************
	 * BUSINESS METHODS
	 ******************************************************************************************************************/

	/**
	 * This method provides a way for users to change their own userPassword.
	 */
	public void changeUserPassword(Long id, String currentPassword, String newPassword) throws BusinessException {
		assertNotADemo();
		User user = find(User.class, id);
		user.changePassword(currentPassword, newPassword);
		flushToWorkAroundOPENEJB_782();
	}

	/**
	 * This method provides a way for security officers to "reset" the userPassword.
	 */
	public void changeUserPassword(Long id, String newPassword) throws BusinessException {
		assertNotADemo();
		User user = find(User.class, id);
		user.setPassword(newPassword);
	}

	/*******************************************************************************************************************
	 * PERSISTENCE METHODS
	 ******************************************************************************************************************/

	// User
	public Long createUser(User user, String password) throws BusinessException {
		assertNotADemo();
		user.changePassword(null, password);
		persist(user);
		flushToWorkAroundOPENEJB_782();
		// DO NOT return the whole User, because its UserPassword is present in it
		return user.getId();
	}

	public void changeUser(User user) throws BusinessException {
		assertNotADemo();
		merge(user);
		flushToWorkAroundOPENEJB_782();
	}

	public void deleteUser(User user) throws BusinessException {
		assertNotADemo();
		user = merge(user);
		remove(user);
		flushToWorkAroundOPENEJB_782();
	}

	// Role

	public Role createRole(Role role) throws BusinessException {
		assertNotADemo();
		persist(role);
		flushToWorkAroundOPENEJB_782();
		return role;
	}

	public void changeRole(Role role) throws BusinessException {
		assertNotADemo();
		merge(role);
		flushToWorkAroundOPENEJB_782();
	}

	public void deleteRole(Role role) throws BusinessException {
		assertNotADemo();
		role = merge(role);
		remove(role);
		flushToWorkAroundOPENEJB_782();
	}

	// UserRole

	public UserRole addUserRole(Long userId, Long roleId, UserRole userRole) throws BusinessException {
		assertNotADemo();
		if (userRole.user != null) {
			throw new IllegalArgumentException();
		}
		if (userRole.role != null) {
			throw new IllegalArgumentException();
		}
		
		User user = find(User.class, userId);
		Role role = find(Role.class, roleId);

		return addUserRole(user, role, userRole);
	}

	public UserRole addUserRole(User user, Role role, UserRole userRole) throws BusinessException {
		assertNotADemo();
		if (userRole.user != null) {
			throw new IllegalArgumentException();
		}
		if (userRole.role != null) {
			throw new IllegalArgumentException();
		}

		user = merge(user);
		role = merge(role);

		user.addUserRole(userRole);
		role.addUserRole(userRole);
		
		persist(userRole);
		flushToWorkAroundOPENEJB_782();
		return userRole;
	}

	public void changeUserRole(UserRole userRole) throws BusinessException {
		assertNotADemo();
		merge(userRole);
		flushToWorkAroundOPENEJB_782();
	}

	public void removeUserRole(UserRole userRole) throws BusinessException {
		assertNotADemo();
		userRole = merge(userRole);
		remove(userRole);
		flushToWorkAroundOPENEJB_782();
	}

	/*******************************************************************************************************************
	 * PRIVATE METHODS
	 ******************************************************************************************************************/

	// These methods are solely to protect the JumpStart database in the demo site
	
	private void assertNotADemo() throws GenericBusinessException {
		if (isADemo()) {
			throw new GenericBusinessException("Demo_prohibited_function");
		}
	}

	private boolean isADemo() {
		boolean readonly = false;
		if (_demoModeStr != null) {
			if (_demoModeStr.equalsIgnoreCase("true")) {
				readonly = true;
			}
			else if (_demoModeStr.equalsIgnoreCase("false")) {
				readonly = false;
			}
			else {
				throw new IllegalStateException(
						"System property jumpstart.demo-mode has been set to \""
								+ _demoModeStr

								+ "\".  Please set it to \"true\" or \"false\".  If not specified at all then it will default to \"false\".");
			}
		}
		return readonly;
	}
}
