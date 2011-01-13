package jumpstart.business.domain.security;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import jumpstart.business.commons.exception.BusinessException;
import jumpstart.business.commons.exception.ReferenceNotMutableException;
import jumpstart.business.commons.exception.ValueRequiredException;
import jumpstart.business.domain.base.BaseEntity;

/**
 * UserRole resolves a many-to-many between User and Role, so
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "userId", "roleId" }) })
@SuppressWarnings("serial")
public class UserRole extends BaseEntity {
	static public String STATUS_ACTIVE = "uro_active";
	static public String STATUS_INACTIVE = "uro_inactive";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	protected Long id;

	@Version
	@Column(nullable = false)
	protected Integer version;

	/**
	 * UserRole is in a COMPOSITION relationship with User in which User is the parent.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", nullable = false)
	protected User user;

	/**
	 * UserRole is in a COMPOSITION relationship with Role in which Role is the parent.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "roleId", nullable = false)
	protected Role role;

	private boolean active;

	@Transient
	protected transient User _loaded_user = null;

	@Transient
	protected transient Role _loaded_role = null;

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.getClass().getSimpleName() + ": ");
		buf.append("[");
		buf.append("id=" + id + DIVIDER);
		buf.append("user=" + toStringLazy(user, "getId") + DIVIDER);
		buf.append("role=" + toStringLazy(role, "getId") + DIVIDER);
		buf.append("active=" + active + DIVIDER);
		buf.append("version=" + version);
		buf.append("]");
		return buf.toString();
	}

	// The need for an equals() method is discussed at http://www.hibernate.org/109.html

	@Override
	public boolean equals(Object obj) {
		return (obj == this) || (obj instanceof UserRole) && id != null && id.equals(((UserRole) obj).getId());
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
	public void validate() throws BusinessException {

		if (user == null) {
			throw new ValueRequiredException(this, "UserRole_user");
		}

		if (role == null) {
			throw new ValueRequiredException(this, "UserRole_role");
		}

	}

	@PostLoad
	void postLoadOfUserRole() {
		_loaded_user = user;
		_loaded_role = role;
	}

	@PreUpdate
	void validateUpdate() throws BusinessException {

		// user can be set only once because it's a reference to the parent

		if (!(user.getId().equals(_loaded_user.getId()))) {
			throw new ReferenceNotMutableException(this, "UserRole_user", _loaded_user.getId(), user.getId());
		}

		// role can be set only once because it's a reference to the parent

		if (!(role.getId().equals(_loaded_role.getId()))) {
			throw new ReferenceNotMutableException(this, "UserRole_role", _loaded_role.getId(), role.getId());
		}
		validate();
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		if (this.user != null) {
			/* Maintain the bidirectional relationship with my parent, User. */
			this.user.internalRemoveUserRole(this);
		}
		this.user = user;
		if (user != null) {
			/* Maintain the bidirectional relationship with my parent, User. */
			user.internalAddUserRole(this);
		}
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		if (this.role != null) {
			/* Maintain the bidirectional relationship with my parent, Role. */
			this.role.internalRemoveUserRole(this);
		}
		this.role = role;
		if (role != null) {
			/* Maintain the bidirectional relationship with my parent, User. */
			role.internalAddUserRole(this);
		}
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
