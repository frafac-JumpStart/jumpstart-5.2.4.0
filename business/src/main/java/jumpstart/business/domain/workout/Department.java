package jumpstart.business.domain.workout;

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
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import jumpstart.business.commons.exception.BusinessException;
import jumpstart.business.commons.exception.CannotDeleteIsNotEmptyException;
import jumpstart.business.commons.exception.ValueRequiredException;
import jumpstart.business.domain.base.BaseEntity;
import jumpstart.util.StringUtil;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "name" }) })
@SuppressWarnings("serial")
public class Department extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;

	@Version
	@Column(nullable = false)
	protected Integer version;

	@Column(length = 40, nullable = false)
	protected String name;

	/**
	 * Department is in an AGGREGATION relationship with Teacher in which Department is the parent.
	 */
	// Do not cascade REMOVE because this is only an AGGREGATION relationship, not a COMPOSITION relationship.
	@OneToMany(mappedBy = "department", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@OrderBy("id")
	protected Set<Teacher> teachers = new HashSet<Teacher>();

	public Department() {
	}

	public Department(String name) {
		super();
		this.name = name;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.getClass().getSimpleName() + ": ");
		buf.append("[");
		buf.append("id=" + id + DIVIDER);
		buf.append("name=" + name + DIVIDER);
		buf.append("teachers.size=" + toStringLazy(teachers, "size") + DIVIDER);
		buf.append("version=" + version);
		buf.append("]");
		return buf.toString();
	}

	// The need for an equals() method is discussed at http://www.hibernate.org/109.html

	@Override
	public boolean equals(Object obj) {
		return (obj == this) || (obj instanceof Department) && id != null && id.equals(((Department) obj).getId());
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

	@PreRemove
	void validateRemove() throws BusinessException {

		// Department cannot be removed if it still has teachers - reassign the teachers first.

		if (getTeachers().size() > 0) {
			throw new CannotDeleteIsNotEmptyException(this, id, "Teacher");
		}
	}

	@PrePersist
	@PreUpdate
	public void validate() throws BusinessException {

		// Validate syntax...

		if (StringUtil.isEmpty(name)) {
			throw new ValueRequiredException(this, "Department_name");
		}

		// Validate semantics...

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
	
	public Set<Teacher> getTeachers() {
		return Collections.unmodifiableSet(teachers);
	}

	public void addTeacher(Teacher teacher) {
		teacher.setDepartment(this);
	}

	/**
	 * Beware - in JPA to persist this change you must merge or remove the child. Merging the parent will not cascade to
	 * the child because it no longer has a reference to the child.
	 */
	void removeTeacher(Teacher teacher) {
		teacher.setDepartment(null);
	}

	public void internalAddTeacher(Teacher teacher) {
		teachers.add(teacher);
	}

	/**
	 * Beware - in JPA to persist this change you must merge or remove the child. Merging the parent will not cascade to
	 * the child because it no longer has a reference to the child.
	 */
	void internalRemoveTeacher(Teacher teacher) {
		teachers.remove(teacher);
	}
}
