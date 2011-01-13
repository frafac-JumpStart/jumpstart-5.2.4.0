package jumpstart.business.domain.security;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import jumpstart.business.commons.exception.BusinessException;
import jumpstart.business.commons.exception.ValueRequiredException;
import jumpstart.business.domain.base.BaseEntity;
import jumpstart.util.StringUtil;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "name" }) })
@SuppressWarnings("serial")
public class Role extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	protected Long id;

	@Version
	@Column(nullable = false)
	protected Integer version;

	@Column(length = 20)
	protected String name;

	/**
	 * Role is in a COMPOSITION relationship with UserRole in which Role is the parent.
	 */
	@OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	protected Set<UserRole> userRoles = new HashSet<UserRole>();

	// Default constructor is required by EJB3.
	public Role() {
	}

	public Role(String name) {
		super();
		this.name = name;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.getClass().getSimpleName() + ": ");
		buf.append("[");
		buf.append("id=" + id + DIVIDER);
		buf.append("name=" + name + DIVIDER);
		buf.append("userRoles.size=" + toStringLazy(userRoles, "size") + DIVIDER);
		buf.append("version=" + version);
		buf.append("]");
		return buf.toString();
	}

	// The need for an equals() method is discussed at http://www.hibernate.org/109.html

	@Override
	public boolean equals(Object obj) {
		return (obj == this) || (obj instanceof Role) && id != null && id.equals(((Role) obj).getId());
	}

	// The need for a hashCode() method is discussed at http://www.hibernate.org/109.html

	@Override
	public int hashCode() {
		return getId() == null ? super.hashCode() : getId().hashCode();
	}

	@Override
	public Serializable getIdForMessages() {
		return getId();
	}

	@PrePersist
	@PreUpdate
	public void validate() throws BusinessException {

		if (StringUtil.isEmpty(name)) {
			throw new ValueRequiredException(this, "Role_name");
		}

	}

	@PreRemove
	void validateRemove() throws BusinessException {
		// Check business rules here, eg.
		// if (entity.getParts().size() > 0) {
		// throw new CannotDeleteIsNotEmptyException(this, id, "Part");
		// }
	}

	public Long getId() {
		return id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<UserRole> getUserRoles() {
		return Collections.unmodifiableSet(userRoles);
	}

	public void addUserRole(UserRole userRole) {
		userRole.setRole(this);
	}

	/**
	 * Beware - in JPA to persist this change you must merge or remove the child. Merging the parent will not cascade to
	 * the child because it no longer has a reference to the child.
	 */
	public void removeUserRole(UserRole userRole) {
		userRole.setRole(null);
	}

	public void internalAddUserRole(UserRole userRole) {
		userRoles.add(userRole);
	}

	/**
	 * Beware - in JPA to persist this change you must merge or remove the child. Merging the parent will not cascade to
	 * the child because it no longer has a reference to the child.
	 */
	public void internalRemoveUserRole(UserRole userRole) {
		userRoles.remove(userRole);
	}
}
