package jumpstart.business.domain.security;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.persistence.Version;

import jumpstart.business.commons.exception.AuthenticationException;
import jumpstart.business.commons.exception.BusinessException;
import jumpstart.business.commons.exception.GenericBusinessException;
import jumpstart.business.commons.exception.ValueRequiredException;
import jumpstart.business.domain.base.BaseEntity;
import jumpstart.util.StringUtil;

/**
 * UserPassword is in a mandatory one-to-one COMPOSITION relationship with User, which exists solely to
 * keep the password out of User. User can be detached but UserPassword is never detached. User is the parent.
 */
@Entity
@SuppressWarnings("serial")
public class UserPassword extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long id;

	@Version
	@Column(nullable = false)
	private Integer version;

	@OneToOne(mappedBy = "userPassword")
	private User user;

	@Column(length = 32)
	private String password;

	@Transient
	private transient String _loaded_password = null;

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.getClass().getSimpleName() + ": ");
		buf.append("[");
		buf.append("id=" + id + DIVIDER);
		buf.append("user=" + user + DIVIDER);
		buf.append("password=" + password + DIVIDER);
		buf.append("version=" + version);
		buf.append("]");
		return buf.toString();
	}

	// The need for an equals() method is discussed at http://www.hibernate.org/109.html

	@Override
	public boolean equals(Object obj) {
		return (obj == this) || (obj instanceof UserPassword) && getId() != null
				&& ((UserPassword) obj).getId().equals(this.getId());
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

		// Validate syntax...

		if (StringUtil.isEmpty(password)) {
			throw new ValueRequiredException(this, "User_password");
		}

		// Validate semantics...

	}

	@PostLoad
	void postLoadOfUserPassword() {
		_loaded_password = password;
	}

	public void authenticate(String password) throws AuthenticationException {

		if (password == null || !password.equals(this.password)) {
			throw new AuthenticationException("User_password_incorrect");
		}

	}

	/**
	 * This method provides a way for users to change their own passwords.
	 */
	void changePassword(String currentPassword, String newPassword) throws BusinessException {

		if (this.password != null && !currentPassword.equals(this.password)) {
			throw new AuthenticationException("User_password_incorrect");
		}

		setPassword(newPassword);
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

	/**
	 * This method provides a way for security officers to "reset" the userPassword.
	 */
	void setPassword(String newPassword) throws BusinessException {

		// Do all tests of userPassword size, content, history, etc. here...

		if (StringUtil.isEmpty(newPassword)) {
			throw new ValueRequiredException(this, "User_password");
		}

		if (_loaded_password != null && newPassword.equals(_loaded_password)) {
			throw new GenericBusinessException("User_newpassword_is_same");
		}

		this.password = newPassword;
	}

}
