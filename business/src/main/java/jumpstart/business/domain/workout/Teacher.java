package jumpstart.business.domain.workout;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

import jumpstart.business.commons.exception.BusinessException;
import jumpstart.business.commons.exception.GenericBusinessException;
import jumpstart.business.commons.exception.ValueRequiredException;
import jumpstart.business.domain.base.BaseEntity;
import jumpstart.util.StringUtil;

@Entity
@SuppressWarnings("serial")
public class Teacher extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;

	@Version
	@Column(nullable = false)
	protected Integer version;

	@Column(length = 20, nullable = false)
	protected String name;

	/**
	 * Teacher is an AGGREGRATION relationship with Department in which Department is the parent.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "departmentId", nullable = true)
	protected Department department;

	/**
	 * Teacher has an ASSOCIATION relationship with Room in which Teacher OPTIONALLY has a "favourite" Room.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "favouriteRoomId", nullable = true)
	protected Room favouriteRoom;

	public Teacher() {
	}

	public Teacher(String name, Department department, Room favouriteRoom) {
		super();
		this.name = name;
		this.department = department;
		if (department != null) {
			department.addTeacher(this);
		}
		this.favouriteRoom = favouriteRoom;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.getClass().getSimpleName() + ": ");
		buf.append("[");
		buf.append("id=" + id + DIVIDER);
		buf.append("name=" + name + DIVIDER);
		buf.append("department=" + toStringLazy(department, "getId") + DIVIDER);
		buf.append("favouriteRoom=" + toStringLazy(favouriteRoom, "getId") + DIVIDER);
		buf.append("version=" + version);
		buf.append("]");
		return buf.toString();
	}

	// The need for an equals() method is discussed at http://www.hibernate.org/109.html

	@Override
	public boolean equals(Object obj) {
		return (obj == this) || (obj instanceof Teacher) && id != null && id.equals(((Teacher) obj).getId());
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
	@PreUpdate
	public void validate() throws BusinessException {

		if (StringUtil.isEmpty(name)) {
			throw new ValueRequiredException(this, "Teacher_name");
		}

		// This is a silly validation, put in here solely to help GenericBusinessExceptionTest!

		if (name.equals("silly")) {
			throw new GenericBusinessException("Teacher_name_is_silly");
		}

	}

	public Long getId() {
		return id;
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

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		if (this.department != null) {
			/* Maintain the bidirectional relationship with my parent, Department. */
			this.department.internalRemoveTeacher(this);
		}
		this.department = department;
		if (department != null) {
			/* Maintain the bidirectional relationship with my parent, Department. */
			department.internalAddTeacher(this);
		}
	}

	public Room getFavouriteRoom() {
		return favouriteRoom;
	}

	public void setFavouriteRoom(Room favouriteRoom) {
		this.favouriteRoom = favouriteRoom;
	}
}
