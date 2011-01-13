package jumpstart.business.domain.security.iface;

import jumpstart.business.commons.exception.BusinessException;
import jumpstart.business.domain.security.Role;
import jumpstart.business.domain.security.User;
import jumpstart.business.domain.security.UserRole;

/**
 * The <code>ISecurityManagerServiceRemote</code> bean exposes the business methods in the interface.
 */
public interface ISecurityManagerServiceRemote {

	/*******************************************************************************************************************
	 * BUSINESS METHODS
	 ******************************************************************************************************************/

	/**
	 * This method provides a way for users to change their own userPassword.
	 */
	void changeUserPassword(Long id, String currentPassword, String newPassword) throws BusinessException;

	/**
	 * This method provides a way for security officers to "reset" the userPassword.
	 */
	void changeUserPassword(Long id, String newPassword) throws BusinessException;

	/*******************************************************************************************************************
	 * PERSISTENCE METHODS
	 ******************************************************************************************************************/

	// User
	
	Long createUser(User user, String password) throws BusinessException;

	void changeUser(User user) throws BusinessException;

	void deleteUser(User user) throws BusinessException;

	// Role
	
	Role createRole(Role role) throws BusinessException;

	void changeRole(Role role) throws BusinessException;

	void deleteRole(Role role) throws BusinessException;

	// UserRole

	UserRole addUserRole(Long userId, Long roleId, UserRole userRole) throws BusinessException;

	UserRole addUserRole(User user, Role role, UserRole userRole) throws BusinessException;

	void changeUserRole(UserRole userRole) throws BusinessException;

	void removeUserRole(UserRole userRole) throws BusinessException;

}
