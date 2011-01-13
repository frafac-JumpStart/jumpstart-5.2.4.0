package jumpstart.business.domain.security.iface;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class UserSearchFields implements Serializable {

	private String _loginId = "";
	private String _salutation = "";
	private String _firstName = "";
	private String _lastName = "";
	private String _emailAddress = "";
	private Date _expiryDate = null;
	private Boolean _active = null;
	private Integer _version = null;

	public UserSearchFields() {
	}

	public UserSearchFields(String loginId, String salutation, String firstName, String lastName, String emailAddress,
			Date expiryDate, Boolean active, Integer version) {
		super();
		_loginId = loginId;
		_salutation = salutation;
		_firstName = firstName;
		_lastName = lastName;
		_emailAddress = emailAddress;
		_expiryDate = expiryDate;
		_active = active;
		_version = version;
	}

	public UserSearchFields(UserSearchFields copyFrom) {
		// No defensive copies are created here, since there are no mutable object fields (String is immutable)
		this(copyFrom._loginId, copyFrom._salutation, copyFrom._firstName, copyFrom._lastName, copyFrom._emailAddress,
				copyFrom._expiryDate, copyFrom._active, copyFrom._version);
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("UserSearchFields: [");
		buf.append("loginId=" + _loginId + ", ");
		buf.append("salutation=" + _salutation + ", ");
		buf.append("firstName=" + _firstName + ", ");
		buf.append("lastName=" + _lastName + ", ");
		buf.append("emailAddress=" + _emailAddress + ", ");
		buf.append("expiryDate=" + _expiryDate + ", ");
		buf.append("active=" + _active + ", ");
		buf.append("version=" + _version);
		buf.append("]");
		return buf.toString();
	}

	public String getLoginId() {
		return _loginId;
	}

	public void setLoginId(String loginId) {
		_loginId = loginId;
	}

	public String getSalutation() {
		return _salutation;
	}

	public void setSalutation(String salutation) {
		_salutation = salutation;
	}

	public String getFirstName() {
		return _firstName;
	}

	public void setFirstName(String firstName) {
		_firstName = firstName;
	}

	public String getLastName() {
		return _lastName;
	}

	public void setLastName(String lastName) {
		_lastName = lastName;
	}

	public String getEmailAddress() {
		return _emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		_emailAddress = emailAddress;
	}

	public Date getExpiryDate() {
		return _expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		_expiryDate = expiryDate;
	}

	public Boolean getActive() {
		return _active;
	}

	public void setActive(Boolean active) {
		_active = active;
	}

	public Integer getVersion() {
		return _version;
	}

	public void setVersion(Integer version) {
		_version = version;
	}

}
