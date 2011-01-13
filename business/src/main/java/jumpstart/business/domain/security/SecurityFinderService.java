package jumpstart.business.domain.security;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import jumpstart.business.commons.exception.AuthenticationException;
import jumpstart.business.commons.exception.DoesNotExistException;
import jumpstart.business.domain.base.BaseService;
import jumpstart.business.domain.security.iface.ISecurityFinderServiceLocal;
import jumpstart.business.domain.security.iface.ISecurityFinderServiceRemote;
import jumpstart.business.domain.security.iface.RoleSearchFields;
import jumpstart.business.domain.security.iface.UserRoleSearchFields;
import jumpstart.business.domain.security.iface.UserSearchFields;
import jumpstart.util.query.ComparisonOperator;
import jumpstart.util.query.QueryBuilder;
import jumpstart.util.query.SearchOptions;

// @SecurityDomain("jumpstart")
@Stateless
@Local(ISecurityFinderServiceLocal.class)
@Remote(ISecurityFinderServiceRemote.class)
public class SecurityFinderService extends BaseService implements ISecurityFinderServiceLocal, ISecurityFinderServiceRemote {

	// User

	public User findUser(Long id) throws DoesNotExistException {
		User obj = find(User.class, id);
		return obj;
	}

	public User authenticateUser(String loginId, String password) throws AuthenticationException {
		try {
			User user;
			user = findUserByLoginId(loginId);
			user.authenticate(password);
			return user;
		}
		catch (DoesNotExistException e) {
			throw new AuthenticationException("User_loginId_unknown", loginId);
		}
	}

	public User findUserByLoginId(String loginId) throws DoesNotExistException {

		try {
			Query q = _em.createQuery("select u from User u where u.loginId = :loginId");
			q.setParameter("loginId", loginId);
			User obj = (User) q.getSingleResult();
			return obj;
		}
		catch (NoResultException e) {
			throw new DoesNotExistException(User.class, loginId);
		}
		catch (NonUniqueResultException e) {
			throw new IllegalStateException("Duplicate User found with loginId = " + loginId + ".", e);
		}
	}

	public User findUserAndUserRoles(Long id) throws DoesNotExistException {

		StringBuffer buf = new StringBuffer();
		buf.append("select distinct u from User u");
		buf.append(" left join fetch u.userRoles");
		buf.append(" where u.id = :id");

		Query q = _em.createQuery(buf.toString());
		q.setParameter("id", id);

		try {
			User obj = (User) q.getSingleResult();
			return obj;
		}
		catch (NoResultException e) {
			throw new DoesNotExistException(UserRole.class, id);
		}
		catch (NonUniqueResultException e) {
			throw new IllegalStateException("Duplicate User found with id = " + id + ".", e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<User> findUsersShallowish() {

		Query q = _em.createQuery("select u from User u order by u.loginId");
		List l = q.getResultList();
		return l;
	}

	@SuppressWarnings("unchecked")
	public List<User> findUsersShallowish(UserSearchFields search, SearchOptions options) {

		QueryBuilder builder = new QueryBuilder();
		builder.append("select u from User u");
		builder.appendEqualsSkipEmpty("u.active", search.getActive());
		builder.appendLikeIgnoreCaseSkipEmpty("u.loginId", search.getLoginId());
		builder.appendEqualsSkipEmpty("u.salutation", search.getSalutation());
		builder.appendLikeIgnoreCaseSkipEmpty("u.firstName", search.getFirstName());
		builder.appendLikeIgnoreCaseSkipEmpty("u.lastName", search.getLastName());
		builder.appendLikeIgnoreCaseSkipEmpty("u.emailAddress", search.getEmailAddress());
		builder.appendComparison("u.expiryDate", ComparisonOperator.EQ, search.getExpiryDate());
		builder.appendEqualsSkipEmpty("u.version", search.getVersion());

		if (options.getSortColumnNames().size() == 0) {
			builder.append(" order by u.loginId");
		}

		Query q = builder.createQuery(_em, options, "u");

		List l = q.getResultList();

		return l;
	}

	// Role

	public Role findRole(Long id) throws DoesNotExistException {
		Role obj = find(Role.class, id);
		return obj;
	}

	public Role findRoleAndUserRoles(Long id) throws DoesNotExistException {

		StringBuffer buf = new StringBuffer();
		buf.append("select distinct r from Role r");
		buf.append(" left join fetch r.userRoles");
		buf.append(" where r.id = :id");

		Query q = _em.createQuery(buf.toString());
		q.setParameter("id", id);

		try {
			Role obj = (Role) q.getSingleResult();
			return obj;
		}
		catch (NoResultException e) {
			throw new DoesNotExistException(UserRole.class, id);
		}
		catch (NonUniqueResultException e) {
			throw new IllegalStateException("Duplicate Role found with id = " + id + ".", e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Role> findRolesShallowish() {
		Query q = _em.createQuery("select r from Role r order by r.name");
		List l = q.getResultList();
		return l;
	}

	@SuppressWarnings("unchecked")
	public List<Role> findRolesShallowish(RoleSearchFields search, SearchOptions options) {

		QueryBuilder builder = new QueryBuilder();
		builder.append("select r from Role r");
		builder.appendLikeIgnoreCaseSkipEmpty("r.name", search.getName());
		builder.appendEqualsSkipEmpty("r.version", search.getVersion());

		if (options.getSortColumnNames().size() == 0) {
			builder.append(" order by r.name");
		}

		Query q = builder.createQuery(_em, options, "r");

		List l = q.getResultList();

		return l;
	}

	// UserRole

	public UserRole findUserRole(Long id) throws DoesNotExistException {
		UserRole obj = find(UserRole.class, id);
		return obj;
	}

	public UserRole findUserRoleShallowish(Long id) throws DoesNotExistException {

		StringBuffer buf = new StringBuffer();
		buf.append("select u from UserRole u");
		buf.append(" join fetch u.user");
		buf.append(" join fetch u.role");
		buf.append(" where u.id = :id");

		Query q = _em.createQuery(buf.toString());
		q.setParameter("id", id);

		try {
			UserRole obj = (UserRole) q.getSingleResult();
			return obj;
		}
		catch (NoResultException e) {
			throw new DoesNotExistException(UserRole.class, id);
		}
		catch (NonUniqueResultException e) {
			throw new IllegalStateException("Duplicate UserRole found with id = " + id + ".", e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<UserRole> findUserRolesShallowish() {

		StringBuffer buf = new StringBuffer();
		buf.append("select u from UserRole u");
		buf.append(" join fetch u.user");
		buf.append(" join fetch u.role");
		buf.append(" order by u.id");

		Query q = _em.createQuery(buf.toString());

		List l = q.getResultList();

		return l;
	}

	@SuppressWarnings("unchecked")
	public List<UserRole> findUserRolesShallowish(UserRoleSearchFields search, SearchOptions options) {

		QueryBuilder builder = new QueryBuilder();
		builder.append("select u from UserRole u");
		builder.append(" join fetch u.user");
		builder.append(" join fetch u.role");
		builder.appendEqualsSkipEmpty("u.user.id", search.getUserId());
		builder.appendEqualsSkipEmpty("u.role.id", search.getRoleId());
		builder.appendEqualsSkipEmpty("u.version", search.getVersion());

		if (options.getSortColumnNames().size() == 0) {
			builder.append(" order by u.id");
		}

		Query q = builder.createQuery(_em, options, "u");

		List l = q.getResultList();

		return l;
	}

	@SuppressWarnings("unchecked")
	public List<UserRole> findUserRolesShallowishByUser(Long userId) {

		StringBuffer buf = new StringBuffer();
		buf.append("select u from UserRole u");
		buf.append(" join fetch u.user");
		buf.append(" join fetch u.role");
		buf.append(" where u.user.id = :userId");
		buf.append(" order by u.id");

		Query q = _em.createQuery(buf.toString());
		q.setParameter("userId", userId);

		List l = q.getResultList();

		return l;
	}

	@SuppressWarnings("unchecked")
	public List<UserRole> findUserRolesShallowishByRole(Long roleId) {

		StringBuffer buf = new StringBuffer();
		buf.append("select u from UserRole u");
		buf.append(" join fetch u.user");
		buf.append(" join fetch u.role");
		buf.append(" where u.role.id = :roleId");
		buf.append(" order by u.id");

		Query q = _em.createQuery(buf.toString());
		q.setParameter("roleId", roleId);

		List l = q.getResultList();

		return l;
	}

}
