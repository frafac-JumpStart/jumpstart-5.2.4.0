package jumpstart.business.domain.security;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import jumpstart.business.commons.exception.AuthenticationException;
import jumpstart.business.commons.exception.BusinessException;
import jumpstart.business.commons.exception.GenericBusinessException;
import jumpstart.business.commons.exception.ValueRequiredException;
import jumpstart.business.domain.base.BaseEntity;
import jumpstart.util.StringUtil;

@Entity
@Table(name="Users", uniqueConstraints = { @UniqueConstraint(columnNames = { "loginId" }) })
@SuppressWarnings("serial")
public class User extends BaseEntity {
	
	static public final String ADMIN_LOGINID = "admin";

	static public final String[] SALUTATIONS = { "Ms", "Mrs", "Mr", "Dr", "Prof" };

	static public enum PageStyle {
		BOXY, WIDE
	}

	static public final String[] DATE_PATTERNS = { "yyyy-MM-dd", "dd MMM yy", "dd MMM yyyy", "dd/MM/yy", "dd/MM/yyyy",
			"MMM dd yy", "MMM dd yyyy", "MM/dd/yy", "MM/dd/yyyy" };
	
	static public final String defaultDateInputPattern = "dd/MM/yy";
	static public final String defaultDateViewPattern = "dd MMM yyyy";
	static public final String defaultDateListPattern = "yyyy-MM-dd";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long id;

	@Version
	@Column(nullable = false)
	private Integer version;

	@Column(length = 15, nullable = false)
	private String loginId;

	@Column(length = 6)
	private String salutation;

	@Column(length = 20, nullable = false)
	private String firstName;

	@Column(length = 20, nullable = false)
	private String lastName;

	@Column(length = 80)
	private String emailAddress;

	private boolean active;

	@Enumerated(EnumType.ORDINAL)
	protected PageStyle pageStyle = PageStyle.BOXY;

	@Column(length = 20, nullable = false)
	protected String dateInputPattern = "dd/MM/yyyy";

	@Column(length = 20, nullable = false)
	protected String dateViewPattern = "dd MMM yyyy";

	@Column(length = 20, nullable = false)
	protected String dateListPattern = "yyyy-MM-dd";

	@Temporal(TemporalType.DATE)
	private Date expiryDate;

	/**
	 * User is in a ONE-TO-ONE MANDATORY relationship with UserPassword in which User is the parent. UserPassword exists
	 * solely to keep the password out of User. User can be detached but UserPassword is never detached.
	 */
	// Use LAZY fetch - we NEVER want this User object detached with the password in it!!!
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	// Do not use @PrimaryKeyJoinColumn because it ALWAYS eager fetches (in JBoss implementation at least).
	@JoinColumn(name = "userPasswordId")
	private UserPassword userPassword = new UserPassword();

	/**
	 * User is in a COMPOSITION relationship with UserRole in which User is the parent.
	 */
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	protected Set<UserRole> userRoles = new HashSet<UserRole>();

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.getClass().getSimpleName() + ": ");
		buf.append("[");
		buf.append("id=" + id + DIVIDER);
		buf.append("loginId=" + loginId + DIVIDER);
		buf.append("salutation=" + salutation + DIVIDER);
		buf.append("firstName=" + firstName + DIVIDER);
		buf.append("lastName=" + lastName + DIVIDER);
		buf.append("emailAddress=" + emailAddress + DIVIDER);
		buf.append("expiryDate=" + expiryDate + DIVIDER);
		buf.append("active=" + active + DIVIDER);
		buf.append("pageStyle=" + pageStyle + DIVIDER);
		buf.append("dateInputPattern=" + dateInputPattern + DIVIDER);
		buf.append("dateViewPattern=" + dateViewPattern + DIVIDER);
		buf.append("dateListPattern=" + dateListPattern + DIVIDER);
		buf.append("userRoles.size=" + sizeLazy(userRoles) + DIVIDER);
		buf.append("version=" + version);
		buf.append("]");
		return buf.toString();
	}

	// The need for an equals() method is discussed at http://www.hibernate.org/109.html

	@Override
	public boolean equals(Object obj) {
		return (obj == this) || (obj instanceof User) && id != null && id.equals(((User) obj).getId());
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

		if (StringUtil.isEmpty(loginId)) {
			throw new ValueRequiredException(this, "User_loginId");
		}

		if (StringUtil.isEmpty(firstName)) {
			throw new ValueRequiredException(this, "User_firstName");
		}

		if (StringUtil.isEmpty(lastName)) {
			throw new ValueRequiredException(this, "User_lastName");
		}

		if (dateInputPattern == null) {
			throw new ValueRequiredException(this, "User_dateInputPattern");
		}

		if (dateViewPattern == null) {
			throw new ValueRequiredException(this, "User_dateViewPattern");
		}

		if (dateListPattern == null) {
			throw new ValueRequiredException(this, "User_dateListPattern");
		}

		// Validate semantics...

		if (expiryDate != null && loginId.equals(ADMIN_LOGINID)) {
			throw new GenericBusinessException("User_expirydate_not_permitted_for_user", new Object[] { ADMIN_LOGINID });
		}

	}

	@PreRemove
	void validateRemove() throws BusinessException {
		// Check business rules here, eg.
		// if (entity.getParts().size() > 0) {
		// throw new CannotDeleteIsNotEmptyException(this, id, "Part");
		// }
	}

	public void authenticate(String password) throws AuthenticationException {
		this.userPassword.authenticate(password);
	}

	/**
	 * This method provides a way for users to change their own passwords.
	 */
	void changePassword(String currentPassword, String newPassword) throws BusinessException {
		this.userPassword.changePassword(currentPassword, newPassword);
	}
	
	public Set<UserRole> getUserRoles() {
		return Collections.unmodifiableSet(userRoles);
	}

	public void addUserRole(UserRole userRole) {
		userRole.setUser(this);
	}

	/**
	 * Beware - in JPA to persist this change you must merge or remove the child. Merging the parent will not cascade to
	 * the child because it no longer has a reference to the child.
	 */
	public void removeUserRole(UserRole userRole) {
		userRole.setUser(null);
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

	public Long getId() {
		return id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	/**
	 * This method provides a way for security officers to "reset" the userPassword.
	 */
	void setPassword(String newPassword) throws BusinessException {
		this.userPassword.setPassword(newPassword);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getSalutation() {
		return salutation;
	}

	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public PageStyle getPageStyle() {
		return pageStyle;
	}

	public void setPageStyle(PageStyle pageStyle) {
		this.pageStyle = pageStyle;
	}

	public String getDateInputPattern() {
		return dateInputPattern;
	}

	public void setDateInputPattern(String pattern) {
		this.dateInputPattern = pattern;
	}

	public String getDateViewPattern() {
		return dateViewPattern;
	}

	public void setDateViewPattern(String pattern) {
		this.dateViewPattern = pattern;
	}

	public String getDateListPattern() {
		return dateListPattern;
	}

	public void setDateListPattern(String pattern) {
		this.dateListPattern = pattern;
	}
}
