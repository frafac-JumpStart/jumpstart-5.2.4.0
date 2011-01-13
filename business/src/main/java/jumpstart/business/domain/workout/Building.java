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
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

import jumpstart.business.commons.exception.BusinessException;
import jumpstart.business.commons.exception.ReferencesWrongSubsetException;
import jumpstart.business.commons.exception.ValueRequiredException;
import jumpstart.business.domain.base.BaseEntity;
import jumpstart.util.StringUtil;

@Entity
@SuppressWarnings("serial")
public class Building extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;

	@Version
	@Column(nullable = false)
	protected Integer version;

	@Column(length = 40, nullable = false)
	protected String name;

	/**
	 * Building is in a COMPOSITION relationship with Room in which Building is the parent.
	 */
	// Cascade ALL because this is a COMPOSITION relationship.
	@OneToMany(mappedBy = "building", fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@OrderBy("id")
	protected Set<Room> rooms = new HashSet<Room>();

	/**
	 * Building has an ASSOCIATION relationship with Room in which Building MANDATORILY has a "main" Room which must be
	 * one of the rooms it is composed of.
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mainRoomId", nullable = true)
	protected Room mainRoom;

	public Building() {
	}

	public Building(String name) {
		super();
		this.name = name;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.getClass().getSimpleName() + ": ");
		buf.append("[");
		buf.append("id=" + id + DIVIDER);
		buf.append("name=" + name + DIVIDER);
		buf.append("rooms.size=" + toStringLazy(rooms, "size") + DIVIDER);
		buf.append("mainRoom=" + toStringLazy(mainRoom, "getId") + DIVIDER);
		buf.append("version=" + version);
		buf.append("]");
		return buf.toString();
	}

	// The need for an equals() method is discussed at http://www.hibernate.org/109.html

	@Override
	public boolean equals(Object obj) {
		return (obj == this) || (obj instanceof Building) && id != null && id.equals(((Building) obj).getId());
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
			throw new ValueRequiredException(this, "Building_name");
		}

		// Validate business rules...
		// This is hard to explain - the room chosen for mainRoom must belong to this building

		if (mainRoom != null && !(mainRoom.getBuilding().equals(this))) {
			throw new ReferencesWrongSubsetException(this, "Building_mainRoom", mainRoom.getId().toString(),
					"Building", this.getId().toString(), mainRoom.getBuilding().getId().toString());
		}

	}

	@PreRemove
	void validateRemove() throws BusinessException {
		// Check business rules here, eg.
		// if (this.getChildren().size() > 0) {
		// throw new CannotDeleteIsNotEmptyException(this, id, "Child");
		// }
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

	public Set<Room> getRooms() {
		return Collections.unmodifiableSet(rooms);
	}

	public void addRoom(Room room) {
		room.setBuilding(this);
	}

	/**
	 * Beware - in JPA to persist this change you must merge or remove the child. Merging the parent will not cascade to
	 * the child because it no longer has a reference to the child.
	 */
	public void removeRoom(Room room) {
		room.setBuilding(null);
	}

	public void internalAddRoom(Room room) {
		rooms.add(room);
	}

	/**
	 * Beware - in JPA to persist this change you must merge or remove the child. Merging the parent will not cascade to
	 * the child because it no longer has a reference to the child.
	 */
	public void internalRemoveRoom(Room room) {
		rooms.remove(room);
	}

	public Room getMainRoom() {
		return mainRoom;
	}

	public void setMainRoom(Room mainRoom) {
		this.mainRoom = mainRoom;
	}
}
