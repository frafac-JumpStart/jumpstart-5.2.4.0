package jumpstart.business.domain.security.iface;

import java.util.List;

import jumpstart.business.commons.exception.AuthenticationException;
import jumpstart.business.commons.exception.DoesNotExistException;
import jumpstart.business.domain.security.Role;
import jumpstart.business.domain.security.User;
import jumpstart.business.domain.security.UserRole;
import jumpstart.util.query.SearchOptions;

/**
 * The <code>ISecurityFinderServiceRemote</code> bean exposes the business methods in the interface.
 */
public interface ISecurityFinderServiceRemote {

	// User

	User findUser(Long id) throws DoesNotExistException;

	User findUserByLoginId(String loginId) throws DoesNotExistException;

	User findUserAndUserRoles(Long id) throws DoesNotExistException;
	
	List<User> findUsersShallowish();

	List<User> findUsersShallowish(UserSearchFields searchFields, SearchOptions searchOptions);

	User authenticateUser(String loginId, String password) throws AuthenticationException;

	// Role

	Role findRole(Long id) throws DoesNotExistException;

	Role findRoleAndUserRoles(Long id) throws DoesNotExistException;

	List<Role> findRolesShallowish();

	List<Role> findRolesShallowish(RoleSearchFields searchFields, SearchOptions searchOptions);

	// UserRole

	UserRole findUserRole(Long id) throws DoesNotExistException;

	UserRole findUserRoleShallowish(Long id) throws DoesNotExistException;

	List<UserRole> findUserRolesShallowish();

	List<UserRole> findUserRolesShallowish(UserRoleSearchFields searchFields, SearchOptions searchOptions);

	List<UserRole> findUserRolesShallowishByUser(Long userId);

	List<UserRole> findUserRolesShallowishByRole(Long roleId);
}
