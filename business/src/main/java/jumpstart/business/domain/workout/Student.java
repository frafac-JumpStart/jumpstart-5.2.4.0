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
import jumpstart.business.commons.exception.ValueRequiredException;
import jumpstart.business.domain.base.BaseEntity;
import jumpstart.util.StringUtil;

@Entity
@SuppressWarnings("serial")
public class Student extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;

	@Version
	@Column(nullable = false)
	protected Integer version;

	@Column(length = 20, nullable = false)
	protected String name;

	/**
	 * Student has an ASSOCIATION relationship with Teacher in which Student MANDATORILY has a Teacher.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "teacherId", nullable = false)
	protected Teacher teacher;

	public Student() {
	}

	public Student(String name, Teacher teacher) {
		super();
		this.name = name;
		this.teacher = teacher;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.getClass().getSimpleName() + ": ");
		buf.append("[");
		buf.append("id=" + id + DIVIDER);
		buf.append("name=" + name + DIVIDER);
		buf.append("teacher=" + toStringLazy(teacher, "getId") + DIVIDER);
		buf.append("version=" + version);
		buf.append("]");
		return buf.toString();
	}

	// The need for an equals() method is discussed at http://www.hibernate.org/109.html

	@Override
	public boolean equals(Object obj) {
		return (obj == this) || (obj instanceof Student) && id != null && id.equals(((Student) obj).getId());
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
			throw new ValueRequiredException(this, "Student_name");
		}

		if (teacher == null) {
			throw new ValueRequiredException(this, "Student_teacher");
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

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
}
