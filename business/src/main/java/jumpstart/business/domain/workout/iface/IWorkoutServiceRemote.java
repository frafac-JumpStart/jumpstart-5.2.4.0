package jumpstart.business.domain.workout.iface;

import java.util.List;

import jumpstart.business.commons.exception.BusinessException;
import jumpstart.business.commons.exception.DoesNotExistException;
import jumpstart.business.domain.workout.Building;
import jumpstart.business.domain.workout.Department;
import jumpstart.business.domain.workout.Room;
import jumpstart.business.domain.workout.StringThing;
import jumpstart.business.domain.workout.Student;
import jumpstart.business.domain.workout.Teacher;

/**
 * The <code>IWorkoutServiceRemote</code> bean exposes the business methods in the interface.
 */
public interface IWorkoutServiceRemote {

	// Building

	Building createBuilding(Building building) throws BusinessException;

	void changeBuilding(Building building) throws BusinessException;

	void deleteBuilding(Building building) throws BusinessException;

	void deleteAllBuildings() throws BusinessException;

	Building findBuilding(Long id) throws DoesNotExistException;

	Building findBuildingAndItsRooms(Long id) throws DoesNotExistException;

	List<Building> findBuildings();

	// Room

	Room addRoom(Room room, boolean checkCountryUnchanged) throws BusinessException;

	void changeRoom(Room room) throws BusinessException;

	void removeRoom(Room room) throws BusinessException;

	void removeAllRooms() throws BusinessException;

	Room findRoom(Long id) throws DoesNotExistException;

	List<Room> findRooms();

	// Department

	Department createDepartment(Department department) throws BusinessException;

	void changeDepartment(Department department) throws BusinessException;

	void deleteDepartment(Department department) throws BusinessException;

	void deleteAllDepartments() throws BusinessException;

	Department findDepartment(Long id) throws DoesNotExistException;

	Department findDepartmentAndItsTeachers(Long id) throws DoesNotExistException;

	List<Department> findDepartments();

	// Teacher

	Teacher createTeacher(Teacher teacher, boolean checkDepartmentUnchanged) throws BusinessException;

	void changeTeacher(Teacher teacher) throws BusinessException;

	void deleteTeacher(Teacher teacher) throws BusinessException;

	void deleteAllTeachers() throws BusinessException;

	Teacher findTeacher(Long id) throws DoesNotExistException;

	// "Shallowish" means join fetch the immediate ManyToOne references.
	Teacher findTeacherShallowish(Long id) throws DoesNotExistException;

	List<Teacher> findTeachers();

	// Student

	Student createStudent(Student student, boolean checkTeacherUnchanged) throws BusinessException;

	void changeStudent(Student student) throws BusinessException;

	void deleteStudent(Student student) throws BusinessException;

	void deleteAllStudents() throws BusinessException;

	Student findStudent(Long id) throws DoesNotExistException;

	// StringThing

	StringThing createStringThing(StringThing stringThing) throws BusinessException;

	void deleteAllStringThings() throws BusinessException;

	StringThing findStringThing(String id) throws DoesNotExistException;

}
