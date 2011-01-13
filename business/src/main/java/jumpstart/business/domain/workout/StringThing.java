package jumpstart.business.domain.workout;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Version;

import jumpstart.business.commons.exception.BusinessException;
import jumpstart.business.commons.exception.ValueRequiredException;
import jumpstart.business.domain.base.BaseEntity;
import jumpstart.util.StringUtil;

@Entity
@SuppressWarnings("serial")
public class StringThing extends BaseEntity {

	@Id
	@Column(length = 3, nullable = false)
	protected String id;

	@Version
	@Column(nullable = false)
	protected Integer version;

	@Column(length = 20, nullable = false)
	protected String name;

	// Required by Hibernate
	public StringThing() {
	}

	public StringThing(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.getClass().getSimpleName() + ": ");
		buf.append("[");
		buf.append("id=" + id + DIVIDER);
		buf.append("name=" + name + DIVIDER);
		buf.append("version=" + version);
		buf.append("]");
		return buf.toString();
	}

	// The need for an equals() method is discussed at http://www.hibernate.org/109.html

	@Override
	public boolean equals(Object obj) {
		return (obj == this) || (obj instanceof StringThing) && id != null && id.equals(((StringThing) obj).getId());
	}

	// The need for an equals() method is discussed at http://www.hibernate.org/109.html

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

		if (StringUtil.isEmpty(id)) {
			throw new ValueRequiredException(this, "StringThing_id");
		}

		if (StringUtil.isEmpty(name)) {
			throw new ValueRequiredException(this, "StringThing_name");
		}

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
