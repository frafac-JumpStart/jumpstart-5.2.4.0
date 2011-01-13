package jumpstart.business.domain.security.iface;

import java.io.Serializable;

@SuppressWarnings("serial")
public class RoleSearchFields implements Serializable {

	private String _name = "";
	private Integer _version = null;

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("RoleSearchFields: [");
		buf.append("name=" + _name + ", ");
		buf.append("version=" + _version);
		buf.append("]");
		return buf.toString();
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public Integer getVersion() {
		return _version;
	}

	public void setVersion(Integer version) {
		_version = version;
	}

}
