package jumpstart.business.domain.security.iface;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UserRoleSearchFields implements Serializable {

	private Long _userId = null;
	private Long _roleId = null;
	private Integer _version = null;

	public UserRoleSearchFields() {
	}

	public UserRoleSearchFields(Long userId, Long roleId, Integer version) {
		_userId = userId;
		_roleId = roleId;
		_version = version;
	}

	public UserRoleSearchFields(UserRoleSearchFields copyFrom) {
		// No defensive copies are created here because there are no mutable object fields (String is immutable)
		this(copyFrom._userId, copyFrom._roleId, copyFrom._version);
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("UserRoleSearchFields: [");
		buf.append("userId=" + _userId + ", ");
		buf.append("roleId=" + _roleId + ", ");
		buf.append("version=" + _version);
		buf.append("]");
		return buf.toString();
	}

	public Long getUserId() {
		return _userId;
	}

	public void setUserId(Long userId) {
		_userId = userId;
	}

	public Long getRoleId() {
		return _roleId;
	}

	public void setRoleId(Long roleId) {
		_roleId = roleId;
	}

	public Integer getVersion() {
		return _version;
	}

	public void setVersion(Integer version) {
		_version = version;
	}

}
