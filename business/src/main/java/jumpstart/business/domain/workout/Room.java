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
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.persistence.Version;

import jumpstart.business.commons.exception.BusinessException;
import jumpstart.business.commons.exception.ReferenceNotMutableException;
import jumpstart.business.commons.exception.ValueRequiredException;
import jumpstart.business.domain.base.BaseEntity;
import jumpstart.util.StringUtil;

@Entity
@SuppressWarnings("serial")
public class Room extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;

	@Version
	@Column(nullable = false)
	protected Integer version;

	@Column(length = 20, nullable = false)
	protected String name;

	/**
	 * Room is in a COMPOSITION relationship with Building in which Building is the parent.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "buildingId", nullable = false)
	protected Building building;

	@Transient
	protected transient Building _loaded_building = null;

	public Room() {
	}

	public Room(String name, Building building) throws BusinessException {
		super();
		this.name = name;
		this.building = building;
		if (building != null) {
			building.addRoom(this);
		}
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.getClass().getSimpleName() + ": ");
		buf.append("[");
		buf.append("id=" + id + DIVIDER);
		buf.append("name=" + name + DIVIDER);
		buf.append("building=" + toStringLazy(building, "getId") + DIVIDER);
		buf.append("version=" + version);
		buf.append("]");
		return buf.toString();
	}

	// The need for an equals() method is discussed at http://www.hibernate.org/109.html

	@Override
	public boolean equals(Object obj) {
		return (obj == this) || (obj instanceof Room) && id != null && id.equals(((Room) obj).getId());
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

	@PostLoad
	void postLoad() {
		_loaded_building = building;
	}

	@PrePersist
	public void validate() throws BusinessException {

		if (StringUtil.isEmpty(name)) {
			throw new ValueRequiredException(this, "Room_name");
		}

		if (building == null) {
			throw new ValueRequiredException(this, "Room_building");
		}

	}

	@PreUpdate
	public void validateUpdate() throws BusinessException {

		// Cannot switch buildings because this is a COMPOSITION relationship.

		if (building != null && !(building.getId().equals(_loaded_building.getId()))) {
			throw new ReferenceNotMutableException(this, "Room_building", _loaded_building.getId(), building.getId());
		}
		
		validate();
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

	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		if (this.building != null) {
			/* Maintain the bidirectional relationship with my parent, Building. */
			this.building.internalRemoveRoom(this);
		}
		this.building = building;
		if (building != null) {
			/* Maintain the bidirectional relationship with my parent, Building. */
			building.internalAddRoom(this);
		}
	}

	/*
	 * This public method exists solely for use by ReferenceNotResettableExceptionTest. The real setBuilding method is
	 * deliberately package-scoped.
	 */
	public void setBuildingForReferenceNotResettableExceptionTest(Building building) {
		this.building = building;
	}
}
